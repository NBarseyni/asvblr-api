package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.*;
import com.pa.asvblrapi.entity.*;
import com.pa.asvblrapi.exception.*;
import com.pa.asvblrapi.mapper.PlayerMapper;
import com.pa.asvblrapi.mapper.TeamMapper;
import com.pa.asvblrapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.annotation.Target;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<Team> getAllTeam() {
        return this.teamRepository.findAll();
    }

    public Optional<Team> getTeam(Long id) {
        return this.teamRepository.findById(id);
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
        if (teamDto.getIdCoach() != null) {
            Optional<User> coach = this.userRepository.findById(teamDto.getIdCoach());
            if(!coach.isPresent()) {
                throw new UserNotFoundException(teamDto.getIdCoach());
            }
            team.setCoach(coach.get());
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
        }
        catch (IOException e) {
            throw new IOException(e.getMessage());
        }
        return TeamMapper.instance.toDto(this.teamRepository.save(team.get()));
    }

    public TeamDto setCoach(Long id, Long idCoach) throws TeamNotFoundException, UserNotFoundException {
        Optional<Team> team = this.teamRepository.findById(id);
        if  (!team.isPresent()) {
            throw new TeamNotFoundException(id);
        }
        Optional<User> user = this.userRepository.findById(idCoach);
        if (!user.isPresent()) {
            throw new UserNotFoundException(idCoach);
        }
        team.get().setCoach(user.get());
        return TeamMapper.instance.toDto(this.teamRepository.save(team.get()));
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
                    jersey.getNumber(),
                    position.getName(),
                    position.getShortName()
            );
            teamPlayerDtoList.add(teamPlayerDto);
        }
        return teamPlayerDtoList;
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
        List<TeamListDto> teamListDtoList = new ArrayList<>();
        List<Team> teams = this.teamRepository.findAll();
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
        Optional<Position> position = this.positionRepository.findById(dto.getIdPosition());
        if (!position.isPresent()) {
            throw new PositionNotFoundException(dto.getIdPosition());
        }
        Jersey jersey = new Jersey(dto.getNumber(), team.get(), position.get(), player.get());
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
                jersey.getNumber(),
                position.get().getName(),
                position.get().getShortName()
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
        Optional<Position> position = this.positionRepository.findById(dto.getIdPosition());
        if (!position.isPresent()) {
            throw new PositionNotFoundException(dto.getIdPosition());
        }
        jersey.get().setPosition(position.get());
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
                updatedJersey.getNumber(),
                position.get().getName(),
                position.get().getShortName()
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
        try {
            Files.delete(Paths.get(UPLOADED_FOLDER + team.get().getPhoto()));
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
        this.teamRepository.delete(team.get());
    }
}
