package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.*;
import com.pa.asvblrapi.entity.*;
import com.pa.asvblrapi.exception.*;
import com.pa.asvblrapi.mapper.PlayerMapper;
import com.pa.asvblrapi.mapper.TeamMapper;
import com.pa.asvblrapi.mapper.UserMapper;
import com.pa.asvblrapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final String UPLOADED_FOLDER = "src/main/resources/public/photos/";

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private JerseyRepository jerseyRepository;

    @Autowired
    private TeamCategoryRepository teamCategoryRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<Team> getAllTeam() {
        return this.teamRepository.findAll();
    }

    public TeamListDto getTeam(Long id) {
        return this.toTeamListDto(this.teamRepository.findById(id).orElseThrow(() -> new TeamNotFoundException(id)));
    }

    public List<TeamDto> getTeamsByPlayer(Long id) throws PlayerNotFoundException {
        Optional<Player> player = this.playerRepository.findById(id);
        if (!player.isPresent()) {
            throw new PlayerNotFoundException(id);
        }
        return TeamMapper.instance.toDto(this.teamRepository.findAllByPlayer(id));
    }

    public TeamDto createTeam(TeamDto teamDto) throws SeasonNotFoundException, TeamCategoryNotFoundException,
            UserNotFoundException {
        Optional<Season> season = this.seasonRepository.findCurrentSeason();
        if (!season.isPresent()) {
            throw new SeasonNotFoundException();
        }
        Optional<TeamCategory> teamCategory = this.teamCategoryRepository.findById(teamDto.getIdTeamCategory());
        if (!teamCategory.isPresent()) {
            throw new TeamCategoryNotFoundException(teamDto.getIdTeamCategory());
        }
        Team team = new Team(teamDto.getName(), season.get(), teamCategory.get());
        Team teamWithCoach = null;
        if (teamDto.getIdCoach() != null) {
            Optional<User> coach = this.userRepository.findById(teamDto.getIdCoach());
            if (!coach.isPresent()) {
                throw new UserNotFoundException(teamDto.getIdCoach());
            }
            teamWithCoach = this.teamRepository.save(team);
            this.setCoach(teamWithCoach.getId(), teamDto.getIdCoach());
        }
        if (teamWithCoach != null) {
            return TeamMapper.instance.toDto(this.teamRepository.save(teamWithCoach));
        }
        return TeamMapper.instance.toDto(this.teamRepository.save(team));
    }

    public TeamDto updateTeam(Long id, TeamDto teamDto) throws TeamNotFoundException, TeamCategoryNotFoundException {
        Optional<Team> team = this.teamRepository.findById(id);
        if (!team.isPresent()) {
            throw new TeamNotFoundException(id);
        }
        Optional<TeamCategory> teamCategory = this.teamCategoryRepository.findById(teamDto.getIdTeamCategory());
        if (!teamCategory.isPresent()) {
            throw new TeamCategoryNotFoundException(teamDto.getIdTeamCategory());
        }
        team.get().setName(teamDto.getName());
        team.get().setTeamCategory(teamCategory.get());
        return TeamMapper.instance.toDto(this.teamRepository.save(team.get()));
    }

    public TeamDto setPhoto(Long id, MultipartFile file) throws TeamNotFoundException, IOException {
        Optional<Team> team = this.teamRepository.findById(id);
        if (!team.isPresent()) {
            throw new TeamNotFoundException(id);
        }
        try {
            String fileName = file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + fileName);
            Files.write(path, bytes);
            Files.delete(Paths.get(UPLOADED_FOLDER + team.get().getPhoto()));
            team.get().setPhoto(fileName);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
        return TeamMapper.instance.toDto(this.teamRepository.save(team.get()));
    }

    public TeamDto setCoach(Long id, Long idCoach) throws TeamNotFoundException, UserNotFoundException {
        Optional<Team> team = this.teamRepository.findById(id);
        if (!team.isPresent()) {
            throw new TeamNotFoundException(id);
        }
        Optional<User> user = this.userRepository.findById(idCoach);
        if (!user.isPresent()) {
            throw new UserNotFoundException(idCoach);
        }

        Role roleCoach = this.roleRepository.findByName("ROLE_COACH");

        if (!user.get().getRoles().contains(roleCoach)) {
            user.get().getRoles().add(roleCoach);
        }
        // Remove duplicates roles (don't know why there are duplicates roles but remove it...)
        user.get().setRoles(user.get().getRoles().stream().distinct().collect(Collectors.toList()));
        this.userRepository.save(user.get());

        User oldCoach = team.get().getCoach();

        team.get().setCoach(user.get());
        Team updatedTeam = this.teamRepository.save(team.get());

        if (oldCoach != null) {
            oldCoach.getCoachedTeams().remove(team.get());
            if (oldCoach.getCoachedTeams().size() == 0) {
                oldCoach.getRoles().remove(roleCoach);
            }
            this.userRepository.save(oldCoach);
        }

        return TeamMapper.instance.toDto(updatedTeam);
    }

    public TeamDto setLeader(Long id, Long idLeader) throws TeamNotFoundException, PlayerNotFoundException, JerseyNotFoundException {
        Optional<Team> team = this.teamRepository.findById(id);
        if (!team.isPresent()) {
            throw new TeamNotFoundException(id);
        }
        Optional<Player> player = this.playerRepository.findById(idLeader);
        if (!player.isPresent()) {
            throw new PlayerNotFoundException(idLeader);
        }
        Optional<Jersey> jersey = this.jerseyRepository.findByIdTeamAndIdPlayer(id, idLeader);
        if (!jersey.isPresent()) {
            throw new JerseyNotFoundException(id, idLeader);
        }
        team.get().setLeader(jersey.get());
        return TeamMapper.instance.toDto(this.teamRepository.save(team.get()));
    }

    public List<TeamPlayerDto> getTeamPlayers(Long id) throws TeamNotFoundException {
        Optional<Team> team = this.teamRepository.findById(id);
        if (!team.isPresent()) {
            throw new TeamNotFoundException(id);
        }
        List<TeamPlayerDto> teamPlayerDtoList = new ArrayList<>();

        List<Jersey> jerseys = this.jerseyRepository.findByIdTeam(id);
        for (Jersey jersey : jerseys) {
            Player player = jersey.getPlayer();
            Position position = jersey.getPosition();
            TeamPlayerDto teamPlayerDto = new TeamPlayerDto(
                    player.getId(),
                    player.getFirstName(),
                    player.getLastName(),
                    player.getAddress(),
                    player.getPostcode(),
                    player.getCity(),
                    player.getEmail(),
                    player.getPhoneNumber(),
                    player.getBirthDate(),
                    player.getLicenceNumber(),
                    player.getRequestedJerseyNumber(),
                    jersey.getNumber(),
                    position.getId(),
                    position.getName(),
                    position.getShortName()
            );
            teamPlayerDtoList.add(teamPlayerDto);
        }
        return teamPlayerDtoList;
    }

    public List<UserDto> getTeamUsers(Long id) throws TeamNotFoundException {
        Optional<Team> team = this.teamRepository.findById(id);
        if (!team.isPresent()) {
            throw new TeamNotFoundException(id);
        }
        List<UserDto> users = new ArrayList<>();
        List<Jersey> jerseys = this.jerseyRepository.findByIdTeam(id);
        for (Jersey jersey : jerseys) {
            Player player = jersey.getPlayer();
            User user = player.getUser();
            users.add(UserMapper.instance.toDto(user));
        }
        if (team.get().getCoach() != null) {
            users.add(UserMapper.instance.toDto(team.get().getCoach()));
        }
        return users;
    }

    public List<PlayerDto> getPlayersNotInTeam(Long id) throws TeamNotFoundException {
        Optional<Team> team = this.teamRepository.findById(id);
        if (!team.isPresent()) {
            throw new TeamNotFoundException(id);
        }
        List<Player> players = new ArrayList<>();

        List<Jersey> jerseys = this.jerseyRepository.findByNotIdTeam(id);
        for (Jersey jersey : jerseys) {
            Player player = jersey.getPlayer();
            players.add(player);
        }
        return PlayerMapper.instance.toDto(players);
    }

    public List<TeamListDto> getTeamList() {
        List<Team> teams = this.teamRepository.findAll();
        return toTeamListDto(teams);
    }

    public List<TeamListDto> getTeamList(Long idPlayer) {
        List<Team> teams = this.teamRepository.findAllByPlayer(idPlayer);
        return toTeamListDto(teams);
    }

    public TeamPlayerDto addPlayer(Long id, AddPlayerTeamDto dto) throws TeamNotFoundException, PlayerNotFoundException,
            PositionNotFoundException {
        Optional<Team> team = this.teamRepository.findById(id);
        if (!team.isPresent()) {
            throw new TeamNotFoundException(id);
        }
        Optional<Player> player = this.playerRepository.findById(dto.getIdPlayer());
        if (!player.isPresent()) {
            throw new PlayerNotFoundException(dto.getIdPlayer());
        }
        Position position = this.positionRepository.findByName("Non défini");
        if (dto.getIdPosition() != null) {
            Optional<Position> positionOptional = this.positionRepository.findById(dto.getIdPosition());
            if (!positionOptional.isPresent()) {
                throw new PositionNotFoundException(dto.getIdPosition());
            }
            position = positionOptional.get();
        }

        Jersey jersey = new Jersey(dto.getNumber(), team.get(), position, player.get());
        this.jerseyRepository.save(jersey);
        return new TeamPlayerDto(
                player.get().getId(),
                player.get().getFirstName(),
                player.get().getLastName(),
                player.get().getAddress(),
                player.get().getPostcode(),
                player.get().getCity(),
                player.get().getEmail(),
                player.get().getPhoneNumber(),
                player.get().getBirthDate(),
                player.get().getLicenceNumber(),
                player.get().getRequestedJerseyNumber(),
                jersey.getNumber(),
                position.getId(),
                position.getName(),
                position.getShortName()
        );
    }

    public TeamPlayerDto updatePlayer(Long idTeam, Long idPlayer, UpdatePlayerTeamDto dto) throws TeamNotFoundException,
            PlayerNotFoundException, JerseyNotFoundException {
        Optional<Team> team = this.teamRepository.findById(idTeam);
        if (!team.isPresent()) {
            throw new TeamNotFoundException(idTeam);
        }
        Optional<Player> player = this.playerRepository.findById(idPlayer);
        if (!player.isPresent()) {
            throw new PlayerNotFoundException(idPlayer);
        }
        Optional<Jersey> jersey = this.jerseyRepository.findByIdTeamAndIdPlayer(idTeam, idPlayer);
        if (!jersey.isPresent()) {
            throw new JerseyNotFoundException(idTeam, idPlayer);
        }
        Position position = this.positionRepository.findByName("Non défini");
        if (dto.getIdPosition() != null) {
            Optional<Position> positionOptional = this.positionRepository.findById(dto.getIdPosition());
            if (!positionOptional.isPresent()) {
                throw new PositionNotFoundException(dto.getIdPosition());
            }
            position = positionOptional.get();
        }

        jersey.get().setPosition(position);
        jersey.get().setNumber(dto.getNumber());
        Jersey updatedJersey = this.jerseyRepository.save(jersey.get());

        return new TeamPlayerDto(
                player.get().getId(),
                player.get().getFirstName(),
                player.get().getLastName(),
                player.get().getAddress(),
                player.get().getPostcode(),
                player.get().getCity(),
                player.get().getEmail(),
                player.get().getPhoneNumber(),
                player.get().getBirthDate(),
                player.get().getLicenceNumber(),
                player.get().getRequestedJerseyNumber(),
                updatedJersey.getNumber(),
                position.getId(),
                position.getName(),
                position.getShortName()
        );
    }

    public void removePlayer(Long idTeam, Long idPlayer) throws TeamNotFoundException, PlayerNotFoundException,
            JerseyNotFoundException {
        Optional<Team> team = this.teamRepository.findById(idTeam);
        if (!team.isPresent()) {
            throw new TeamNotFoundException(idTeam);
        }
        Optional<Player> player = this.playerRepository.findById(idPlayer);
        if (!player.isPresent()) {
            throw new PlayerNotFoundException(idTeam);
        }
        Optional<Jersey> jersey = this.jerseyRepository.findByIdTeamAndIdPlayer(idTeam, idPlayer);
        if (!jersey.isPresent()) {
            throw new JerseyNotFoundException(idTeam, idPlayer);
        }
        this.jerseyRepository.delete(jersey.get());
    }

    public void deleteTeam(Long id) throws TeamNotFoundException, IOException {
        Optional<Team> team = this.teamRepository.findById(id);
        if (!team.isPresent()) {
            throw new TeamNotFoundException(id);
        }
        if (team.get().getPhoto() != null) {
            try {
                Files.delete(Paths.get(UPLOADED_FOLDER + team.get().getPhoto()));
            } catch (IOException e) {
                throw new IOException(e.getMessage());
            }
        }
        User coach = team.get().getCoach();
        if (coach != null) {
            coach.getCoachedTeams().remove(team.get());
            if (coach.getCoachedTeams().size() == 0) {
                coach.getRoles().remove(this.roleRepository.findByName("ROLE_COACH"));
            }
            this.userRepository.save(coach);
        }
        this.teamRepository.delete(team.get());
    }

    public void deleteAllTeams() throws IOException {
        List<Team> teams = this.teamRepository.findAll();
        for (Team team :
                teams) {
            if (team.getPhoto() != null) {
                try {
                    Files.delete(Paths.get(UPLOADED_FOLDER + team.getPhoto()));
                } catch (IOException e) {
                    throw new IOException(e.getMessage());
                }
            }
        }
        this.teamRepository.deleteAll();
    }

    // ===== OTHERS =====

    public TeamListDto toTeamListDto(Team team) {
        String coachFirstName = "";
        String coachLastName = "";
        if (team.getCoach() != null) {
            coachFirstName = team.getCoach().getFirstName();
            coachLastName = team.getCoach().getLastName();
        }
        return new TeamListDto(
                team.getId(),
                team.getName(),
                team.getTeamCategory().getName(),
                coachFirstName,
                coachLastName,
                this.jerseyRepository.nbPlayerByIdTeam(team.getId())
        );
    }

    public List<TeamListDto> toTeamListDto(List<Team> teams) {
        List<TeamListDto> teamListDtoList = new ArrayList<>();
        for (Team team : teams) {
            String coachFirstName = "";
            String coachLastName = "";
            if (team.getCoach() != null) {
                coachFirstName = team.getCoach().getFirstName();
                coachLastName = team.getCoach().getLastName();
            }
            TeamListDto teamListDto = new TeamListDto(
                    team.getId(),
                    team.getName(),
                    team.getTeamCategory().getName(),
                    coachFirstName,
                    coachLastName,
                    this.jerseyRepository.nbPlayerByIdTeam(team.getId())
            );
            teamListDtoList.add(teamListDto);
        }
        return teamListDtoList;
    }
}
