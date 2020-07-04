package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.VisitDto;
import com.pa.asvblrapi.entity.Visit;
import com.pa.asvblrapi.repository.PlayerRepository;
import com.pa.asvblrapi.repository.SubscriptionRepository;
import com.pa.asvblrapi.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private VisitRepository visitRepository;

    public List<Object> getCountNbPlayersByCity() {
        return this.playerRepository.countNbPlayersByCity();
    }

    public List<Object> getCountNbPaymentMode() {
        return this.subscriptionRepository.countNbPaymentMode();
    }

    public List<Object> getNbPlayersByAgeByTrancheOf5() {
        return this.playerRepository.countNbPlayersByAgeByTrancheOf5();
    }

    public List<Object> getVisitStat() {
        return this.visitRepository.getVisitStats();
    }

    public void createVisit(VisitDto dto) {
        Visit visit = new Visit(dto.getPageCode());
        this.visitRepository.save(visit);
    }
}
