package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.dto.RoleDto;
import com.pa.asvblrapi.exception.RoleNotFoundException;
import com.pa.asvblrapi.mapper.RoleMapper;
import com.pa.asvblrapi.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("")
    public List<RoleDto> getRoles() {
        return RoleMapper.instance.toDto( this.roleRepository.findAll());
    }

    @GetMapping("/{id}")
    public RoleDto getRole(@PathVariable Long id) {
        return RoleMapper.instance.toDto(this.roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id)));
    }
}
