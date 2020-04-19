package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.TeamDto;
import com.pa.asvblrapi.entity.Team;
import com.pa.asvblrapi.exception.TeamNotFoundException;
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

    public List<Team> getAllTeam() {
        return this.teamRepository.findAll();
    }

    public Optional<Team> getTeam(Long id) {
        return this.teamRepository.findById(id);
    }

    public Team createTeam(TeamDto teamDto) {
        Team team = new Team(teamDto.getName());
        return this.teamRepository.save(team);
    }

    public Team updateTeam(Long id, TeamDto teamDto) throws TeamNotFoundException {
        Optional<Team> team = this.teamRepository.findById(id);
        if(!team.isPresent()) {
            throw new TeamNotFoundException(id);
        }
        team.get().setName(teamDto.getName());
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
