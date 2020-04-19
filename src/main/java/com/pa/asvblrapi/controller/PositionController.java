package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.entity.Position;
import com.pa.asvblrapi.exception.PositionNotFoundException;
import com.pa.asvblrapi.service.PositionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/positions")
public class PositionController {
    private final PositionService positionService;

    PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping("/")
    public List<Position> getPositions() {
        return this.positionService.getAllPosition();
    }

    @GetMapping("/{id}")
    public Position getPosition(@PathVariable Long id) {
        return this.positionService.getPosition(id)
                .orElseThrow(() -> new PositionNotFoundException(id));
        
    }
}
