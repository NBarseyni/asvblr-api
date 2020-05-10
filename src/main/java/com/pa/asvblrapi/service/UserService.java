package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.UserDtoFirebase;
import com.pa.asvblrapi.entity.Privilege;
import com.pa.asvblrapi.entity.Role;
import com.pa.asvblrapi.entity.User;
import com.pa.asvblrapi.repository.PrivilegeRepository;
import com.pa.asvblrapi.repository.RoleRepository;
import com.pa.asvblrapi.repository.UserRepository;
import com.pa.asvblrapi.spring.EmailServiceImpl;
import com.pa.asvblrapi.spring.RandomPasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailServiceImpl emailService;

    private final RandomPasswordGenerator randomPasswordGenerator = new RandomPasswordGenerator();

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private FirebaseService firebaseService;

    public User createUser(String firstName, String lastName, String email) throws Exception {
        try {
            String username = String.format("%s%s", firstName, lastName);
            int i = 1;
            while(userRepository.existsByUsername(username)) {
                username += i;
            }
            String password = randomPasswordGenerator.generatePassword();
            User user = userRepository.save(new User(username, firstName, lastName, email, encoder.encode(password)));

            Role role = roleRepository.findByName("ROLE_USER");
            List<Role> roles = new ArrayList<>(Arrays.asList(role));
            user.setRoles(roles);

            UserDtoFirebase userDtoFirebase = new UserDtoFirebase(username, user.getFirstName(), user.getLastName(), user.getEmail());
            this.firebaseService.saveUserDetails(userDtoFirebase);

            this.emailService.sendMessageCreateUser(user, password);

            return user;
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
