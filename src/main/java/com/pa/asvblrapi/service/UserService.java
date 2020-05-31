package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.UserDto;
import com.pa.asvblrapi.dto.UserDtoFirebase;
import com.pa.asvblrapi.entity.Privilege;
import com.pa.asvblrapi.entity.Role;
import com.pa.asvblrapi.entity.User;
import com.pa.asvblrapi.exception.UserNotFoundException;
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
import java.util.Optional;
import java.util.concurrent.ExecutionException;

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

    public Optional<User> getUser(Long id) {
        return this.userRepository.findById(id);
    }

    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

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

            UserDtoFirebase userDtoFirebase = new UserDtoFirebase(user.getId(), username, user.getFirstName(),
                    user.getLastName(), user.getEmail());
            this.firebaseService.saveUserDetails(userDtoFirebase);

            this.emailService.sendMessageCreateUser(user, password);

            return user;
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public User updateUser(Long id, UserDto userDto) throws UserNotFoundException, InterruptedException, ExecutionException {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()) {
            throw new UserNotFoundException(id);
        }
        try {
            user.get().setFirstName(userDto.getFirstName());
            user.get().setLastName(userDto.getLastName());
            user.get().setEmail(userDto.getEmail());
            User userSave = userRepository.save(user.get());
            this.firebaseService.updateUserDetails(new UserDtoFirebase(userSave.getId(), userSave.getUsername(),
                    userSave.getFirstName(), userSave.getLastName(), userSave.getEmail()));
            return userSave;
        }
        catch (InterruptedException e) {
            throw new InterruptedException(e.getMessage());
        }
        catch (ExecutionException e) {
            throw new ExecutionException(e.getCause());
        }
    }

    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return this.encoder.matches(oldPassword, user.getPassword());
    }

    public void changeUserPassword(User user, String password) {
        user.setPassword(this.encoder.encode(password));
        this.userRepository.save(user);
    }

    public void deleteUser(Long id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()) {
            throw new UserNotFoundException(id);
        }
        this.userRepository.delete(user.get());
        this.firebaseService.deleteUser(user.get().getUsername());
    }
}
