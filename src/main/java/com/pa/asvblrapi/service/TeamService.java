package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.TeamDto;
import com.pa.asvblrapi.entity.Season;
import com.pa.asvblrapi.entity.Team;
import com.pa.asvblrapi.exception.SeasonNotFoundException;
import com.pa.asvblrapi.exception.TeamNotFoundException;
import com.pa.asvblrapi.repository.SeasonRepository;
import com.pa.asvblrapi.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    public List<Team> getAllTeam() {
        return this.teamRepository.findAll();
    }

    public Optional<Team> getTeam(Long id) {
        return this.teamRepository.findById(id);
    }

    public Team createTeam(TeamDto teamDto) throws SeasonNotFoundException {
        Optional<Season> season = this.seasonRepository.findById(teamDto.getIdSeason());
        if(!season.isPresent()) {
            throw new SeasonNotFoundException(teamDto.getIdSeason());
        }
        Team team = new Team(teamDto.getName(), teamDto.getPhoto(), season.get());
        return this.teamRepository.save(team);
    }

    public Team updateTeam(Long id, TeamDto teamDto) throws TeamNotFoundException {
        Optional<Team> team = this.teamRepository.findById(id);
        if(!team.isPresent()) {
            throw new TeamNotFoundException(id);
        }
        Optional<Season> season = this.seasonRepository.findById(teamDto.getIdSeason());
        if(!season.isPresent()) {
            throw new SeasonNotFoundException(teamDto.getIdSeason());
        }
        team.get().setName(teamDto.getName());
        team.get().setPhoto(teamDto.getPhoto());
        team.get().setSeason(season.get());
        return this.teamRepository.save(team.get());
    }

    public void deleteTeam(Long id) throws TeamNotFoundException, AccessDeniedException {
        Optional<Team> team = this.teamRepository.findById(id);
        if(!team.isPresent()) {
            throw new TeamNotFoundException(id);
        }
        this.teamRepository.delete(team.get());
    }
}
