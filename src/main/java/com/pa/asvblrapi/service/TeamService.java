package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.*;
import com.pa.asvblrapi.entity.*;
import com.pa.asvblrapi.exception.*;
import com.pa.asvblrapi.mapper.PlayerMapper;
import com.pa.asvblrapi.mapper.TeamMapper;
import com.pa.asvblrapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.annotation.Target;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

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

    public TeamDto createTeam(TeamDto teamDto) throws SeasonNotFoundException, TeamCategoryNotFoundException, UserNotFoundException {
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
            String coachName;
            if (team.getCoach() != null) {
                coachName = String.format("%s %s", team.getCoach().getFirstName(), team.getCoach().getLastName());
            } else {
                coachName = "";
            }
            TeamListDto teamListDto = new TeamListDto(
                    team.getId(),
                    team.getName(),
                    team.getTeamCategory().getName(),
                    coachName,
                    this.jerseyRepository.nbPlayerByIdTeam(team.getId())
            );
            teamListDtoList.add(teamListDto);
        }
        return teamListDtoList;
    }

    public void addPlayer(Long id, AddPlayerTeamDto dto) throws TeamNotFoundException, PlayerNotFoundException,
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
    }

    public void removePlayer(Long idTeam, Long idPlayer) throws TeamNotFoundException, PlayerNotFoundException {
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

    public void deleteTeam(Long id) throws TeamNotFoundException, AccessDeniedException {
        Optional<Team> team = this.teamRepository.findById(id);
        if (!team.isPresent()) {
            throw new TeamNotFoundException(id);
        }
        this.teamRepository.delete(team.get());
    }
}
