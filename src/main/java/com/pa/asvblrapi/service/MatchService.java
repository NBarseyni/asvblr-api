package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.CommentMatchDto;
import com.pa.asvblrapi.dto.MatchDto;
import com.pa.asvblrapi.entity.Match;
import com.pa.asvblrapi.entity.Team;
import com.pa.asvblrapi.exception.MatchDidNotTakePlaceException;
import com.pa.asvblrapi.exception.MatchNotFoundException;
import com.pa.asvblrapi.exception.TeamNotFoundException;
import com.pa.asvblrapi.mapper.MatchMapper;
import com.pa.asvblrapi.repository.MatchRepository;
import com.pa.asvblrapi.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamRepository teamRepository;

    public List<MatchDto> getAllMatches() {
        return MatchMapper.instance.toDto(this.matchRepository.findAll());
    }

    public MatchDto getMatch(Long id) {
        return MatchMapper.instance.toDto(this.matchRepository.findById(id)
                .orElseThrow(() -> new MatchNotFoundException(id)));
    }

    public List<MatchDto> getAllMatchesByIdTeam(Long idTeam) {
        return MatchMapper.instance.toDto(this.matchRepository.findAllByIdTeam(idTeam));
    }

    public MatchDto createMatch(MatchDto matchDto) throws TeamNotFoundException {
        Optional<Team> team = this.teamRepository.findById(matchDto.getIdTeam());
        if (!team.isPresent()) {
            throw new TeamNotFoundException(matchDto.getIdTeam());
        }
        Match match = new Match(matchDto.getDate(), matchDto.getPlace(), matchDto.isType(), matchDto.getOppositeTeam(),
                team.get());
        return MatchMapper.instance.toDto(this.matchRepository.save(match));
    }

    public MatchDto updateMatch(Long id, MatchDto matchDto) throws MatchNotFoundException, TeamNotFoundException {
        Optional<Match> match = this.matchRepository.findById(id);
        if (!match.isPresent()) {
            throw new MatchNotFoundException(id);
        }
        Optional<Team> team = this.teamRepository.findById(matchDto.getIdTeam());
        if (!team.isPresent()) {
            throw new TeamNotFoundException(matchDto.getIdTeam());
        }
        match.get().setDate(matchDto.getDate());
        match.get().setPlace(matchDto.getPlace());
        match.get().setType(matchDto.isType());
        match.get().setOppositeTeam(matchDto.getOppositeTeam());
        match.get().setTeam(team.get());
        return MatchMapper.instance.toDto(this.matchRepository.save(match.get()));
    }

    public MatchDto commentMatch(Long id, CommentMatchDto dto) throws MatchNotFoundException {
        Optional<Match> match = this.matchRepository.findById(id);
        if (!match.isPresent()) {
            throw new MatchNotFoundException(id);
        }
        if (match.get().getDate().after(new Date())) {
            throw new MatchDidNotTakePlaceException(id);
        }
        match.get().setComment(dto.getComment());
        match.get().setTechnicalRating(dto.getTechnicalRating());
        match.get().setCollectiveRating(dto.getCollectiveRating());
        match.get().setOffensiveRating(dto.getOffensiveRating());
        match.get().setDefensiveRating(dto.getDefensiveRating());
        match.get().setCombativenessRating(dto.getCombativenessRating());
        return MatchMapper.instance.toDto(this.matchRepository.save(match.get()));
    }

    public void deleteMatch(Long id) throws MatchNotFoundException {
        Optional<Match> match = this.matchRepository.findById(id);
        if (!match.isPresent()) {
            throw new MatchNotFoundException(id);
        }
        this.matchRepository.delete(match.get());
    }
}
