package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.AddPlayerTeamDto;
import com.pa.asvblrapi.dto.TeamDto;
import com.pa.asvblrapi.dto.TeamPlayerDto;
import com.pa.asvblrapi.entity.*;
import com.pa.asvblrapi.exception.*;
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

    public List<Team> getAllTeam() {
        return this.teamRepository.findAll();
    }

    public Optional<Team> getTeam(Long id) {
        return this.teamRepository.findById(id);
    }

    public Team createTeam(TeamDto teamDto) throws SeasonNotFoundException {
        Optional<Season> season = this.seasonRepository.findCurrentSeason();
        if (!season.isPresent()) {
            throw new SeasonNotFoundException();
        }
        Team team = new Team(teamDto.getName(), season.get());
        if (teamDto.getIdCoach() != null) {
            Optional<User> coach = this.userRepository.findById(teamDto.getIdCoach());
            if(!coach.isPresent()) {
                throw new UserNotFoundException(teamDto.getIdCoach());
            }
            team.setCoach(coach.get());
        }
        return this.teamRepository.save(team);
    }

    public Team updateTeam(Long id, TeamDto teamDto) throws TeamNotFoundException, SeasonNotFoundException {
        Optional<Team> team = this.teamRepository.findById(id);
        if (!team.isPresent()) {
            throw new TeamNotFoundException(id);
        }
        team.get().setName(teamDto.getName());
        return this.teamRepository.save(team.get());
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
