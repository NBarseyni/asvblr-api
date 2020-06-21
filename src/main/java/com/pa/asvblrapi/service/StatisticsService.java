package com.pa.asvblrapi.service;

import com.pa.asvblrapi.repository.PlayerRepository;
import com.pa.asvblrapi.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public List<Object> getCountNbPlayersByCity() {
        return this.playerRepository.countNbPlayersByCity();
    }

    public List<Object> getCountNbPaymentMode() {
        return this.subscriptionRepository.countNbPaymentMode();
    }
}
