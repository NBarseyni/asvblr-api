package com.pa.asvblrapi.service;

import com.pa.asvblrapi.entity.Position;
import com.pa.asvblrapi.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PositionService {
    @Autowired
    private PositionRepository positionRepository;

    public List<Position> getAllPosition() {
        return this.positionRepository.findAll();
    }

    public Optional<Position> getPosition(Long id) {
        return this.positionRepository.findById(id);
    }
}
