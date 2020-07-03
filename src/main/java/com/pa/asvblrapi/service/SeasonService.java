package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.SeasonDto;
import com.pa.asvblrapi.entity.Season;
import com.pa.asvblrapi.exception.SeasonNotFoundException;
import com.pa.asvblrapi.mapper.SeasonMapper;
import com.pa.asvblrapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
public class SeasonService {

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DriveRepository driveRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private JerseyRepository jerseyRepository;

    @Autowired
    private TeamService teamService;

    public List<Season> getAllSeasons() {
        return this.seasonRepository.findAll();
    }

    public Optional<Season> getSeason(Long id) {
        return this.seasonRepository.findById(id);
    }

    public SeasonDto getCurrentSeason() throws SeasonNotFoundException {
        Optional<Season> season = this.seasonRepository.findCurrentSeason();
        if (!season.isPresent()) {
            throw new SeasonNotFoundException();
        }
        return SeasonMapper.instance.toDto(season.get());
    }

    public Season createSeason(SeasonDto seasonDto) throws IOException {
        Season season = new Season(seasonDto.getName());
        Season newSeason = this.seasonRepository.save(season);
        this.setCurrentSeason(newSeason.getId());
        this.deletePastSeasonData();
        return newSeason;
    }

    private void deletePastSeasonData() throws IOException {
        this.driveRepository.deleteAllUserDrive();
        this.driveRepository.deleteAll();
        this.matchRepository.deleteAll();
        this.jerseyRepository.deleteAll();
        this.teamService.deleteAllTeams();
        this.userService.removeAllRightCoachAndDeleteNotPlayerUsers();
    }

    public Season updateSeason(Long id, SeasonDto seasonDto) throws SeasonNotFoundException {
        Optional<Season> season = this.seasonRepository.findById(id);
        if (!season.isPresent()) {
            throw new SeasonNotFoundException(id);
        }
        season.get().setName(seasonDto.getName());
        return this.seasonRepository.save(season.get());
    }

    public void setCurrentSeason(Long id) throws SeasonNotFoundException {
        List<Season> seasons = this.seasonRepository.findAll();
        for (Season value : seasons) {
            value.setCurrentSeason(false);
            this.seasonRepository.save(value);
        }

        Optional<Season> season = this.seasonRepository.findById(id);
        if (!season.isPresent()) {
            throw new SeasonNotFoundException(id);
        }
        season.get().setCurrentSeason(true);
        this.seasonRepository.save(season.get());
    }

    public void deleteSeason(Long id) throws SeasonNotFoundException, AccessDeniedException {
        Optional<Season> season = this.seasonRepository.findById(id);

        if (!season.isPresent()) {
            throw new SeasonNotFoundException(id);
        }
        this.seasonRepository.delete(season.get());
    }
}
