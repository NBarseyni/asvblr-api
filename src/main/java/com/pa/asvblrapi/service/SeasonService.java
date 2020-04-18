package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.SeasonDto;
import com.pa.asvblrapi.entity.Season;
import com.pa.asvblrapi.exception.SeasonNotFoundException;
import com.pa.asvblrapi.repository.SeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
public class SeasonService {

    @Autowired
    private SeasonRepository seasonRepository;

    public List<Season> getAllSeasons() {
        return this.seasonRepository.findAll();
    }

    public Optional<Season> getSeason(Long id) {
        return this.seasonRepository.findById(id);
    }

    public Season createSeason(SeasonDto seasonDto) {
        Season season = new Season(seasonDto.getName());
        return this.seasonRepository.save(season);
    }

    public Season updateSeason(Long id, SeasonDto seasonDto) throws SeasonNotFoundException {
        Optional<Season> season = this.seasonRepository.findById(id);
        if (!season.isPresent()) {
            throw new SeasonNotFoundException(id);
        }
        season.get().setName(seasonDto.getName());
        return this.seasonRepository.save(season.get());
    }

    public void deleteSeason(Long id) throws SeasonNotFoundException, AccessDeniedException {
        Optional<Season> season = this.seasonRepository.findById(id);

        if(!season.isPresent()) {
            throw new SeasonNotFoundException(id);
        }
        this.seasonRepository.delete(season.get());
    }
}
