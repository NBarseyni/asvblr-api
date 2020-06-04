package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.entity.TeamCategory;
import com.pa.asvblrapi.exception.TeamCategoryNotFoundException;
import com.pa.asvblrapi.repository.TeamCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/team-categories")
public class TeamCategoryController {

    @Autowired
    private TeamCategoryRepository teamCategoryRepository;

    @GetMapping("")
    public List<TeamCategory> getCategories() {
        return this.teamCategoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public TeamCategory getCategory(@PathVariable Long id) {
        return this.teamCategoryRepository.findById(id)
                .orElseThrow(() -> new TeamCategoryNotFoundException(id));
    }
}
