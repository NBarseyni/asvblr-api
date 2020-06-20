package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.dto.UserDto;
import com.pa.asvblrapi.entity.Privilege;
import com.pa.asvblrapi.entity.Role;
import com.pa.asvblrapi.entity.User;
import com.pa.asvblrapi.jwt.JwtUtils;
import com.pa.asvblrapi.mapper.UserMapper;
import com.pa.asvblrapi.payload.request.LoginRequest;
import com.pa.asvblrapi.payload.request.SignupRequest;
import com.pa.asvblrapi.payload.response.JwtResponse;
import com.pa.asvblrapi.payload.response.MessageResponse;
import com.pa.asvblrapi.repository.RoleRepository;
import com.pa.asvblrapi.repository.UserRepository;
import com.pa.asvblrapi.service.UserDetailsImpl;
import com.pa.asvblrapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        /*
        List<String> privileges = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
         */
        User user = userRepository.findByUsername(userDetails.getUsername());
        Collection<Role> roles = user.getRoles();

        List<String> rolesString = new ArrayList<>();
        List<String> privileges = new ArrayList<>();
        for (Role role :
                roles) {
            rolesString.add(role.getName());
            for (Privilege privilege :
                    role.getPrivileges()) {
                if (!privileges.contains(privilege.getName())) {
                    privileges.add(privilege.getName());
                }
            }
        }
        
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getEmail(),
                user.isPasswordChanged(),
                rolesString,
                privileges));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : Email is already in use !"));
        }

        try {
            User user = this.userService.createUser(signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getEmail());
            UserDto userDto = UserMapper.instance.toDto(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
