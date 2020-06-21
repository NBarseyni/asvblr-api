package com.pa.asvblrapi.service;

import com.pa.asvblrapi.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {
    @Autowired
    private PlayerRepository playerRepository;

    public List<Object> getCountNbPlayersByCity() {
        return this.playerRepository.countNbPlayersByCity();
    }
}
