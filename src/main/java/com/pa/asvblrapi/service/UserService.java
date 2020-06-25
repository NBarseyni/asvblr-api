package com.pa.asvblrapi.service;

import com.google.common.collect.Iterables;
import com.pa.asvblrapi.dto.SendMailDto;
import com.pa.asvblrapi.dto.UserDto;
import com.pa.asvblrapi.dto.UserDtoFirebase;
import com.pa.asvblrapi.entity.*;
import com.pa.asvblrapi.exception.UserAlreadyManagerException;
import com.pa.asvblrapi.exception.UserIsPlayerException;
import com.pa.asvblrapi.exception.UserIsPresidentException;
import com.pa.asvblrapi.exception.UserNotFoundException;
import com.pa.asvblrapi.mapper.UserMapper;
import com.pa.asvblrapi.repository.PasswordResetTokenRepository;
import com.pa.asvblrapi.repository.RoleRepository;
import com.pa.asvblrapi.repository.UserRepository;
import com.pa.asvblrapi.spring.EmailServiceImpl;
import com.pa.asvblrapi.spring.RandomPasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public List<UserDto> getAllUser() {
        List<User> users = this.userRepository.findAll();
        return UserMapper.instance.toDto(users);
    }

    public Optional<User> getUser(Long id) {
        return this.userRepository.findById(id);
    }

    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public User createUserSubscription(String firstName, String lastName, String email) throws Exception {
        try {
            String username = this.createUsername(firstName, lastName);
            String password = randomPasswordGenerator.generatePassword();
            User user = userRepository.save(new User(username, firstName, lastName, email, encoder.encode(password)));

            Role role = roleRepository.findByName("ROLE_PLAYER");
            saveUserFirebaseAndSendEmail(username, password, user, role);
            return user;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public User createUser(String firstName, String lastName, String email) throws Exception {
        try {
            String username = this.createUsername(firstName, lastName);
            String password = this.randomPasswordGenerator.generatePassword();
            User user = this.userRepository.save(new User(username, firstName, lastName, email, encoder.encode(password)));
            saveUserFirebaseAndSendEmail(username, password, user);
            return user;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void saveUserFirebaseAndSendEmail(String username, String password, User user, Role role)
            throws InterruptedException, ExecutionException, MessagingException {
        List<Role> roles = new ArrayList<>(Collections.singletonList(role));
        user.setRoles(roles);

        UserDtoFirebase userDtoFirebase = new UserDtoFirebase(user.getId(), username, user.getFirstName(),
                user.getLastName(), user.getEmail());
        this.firebaseService.saveUserDetails(userDtoFirebase);
        this.emailService.sendMessageCreateUser(user, password);
    }

    private void saveUserFirebaseAndSendEmail(String username, String password, User user)
            throws ExecutionException, InterruptedException, MessagingException {
        UserDtoFirebase userDtoFirebase = new UserDtoFirebase(user.getId(), username, user.getFirstName(),
                user.getLastName(), user.getEmail());
        this.firebaseService.saveUserDetails(userDtoFirebase);
        this.emailService.sendMessageCreateUser(user, password);
    }

    public String createUsername(String firstName, String lastName) {
        String username = String.format("%s%s", firstName, lastName);
        int i = 1;
        while (this.userRepository.existsByUsername(username)) {
            username += i;
        }
        return username;
    }

    public User updateUser(Long id, UserDto userDto) throws UserNotFoundException, InterruptedException, ExecutionException {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
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
        } catch (InterruptedException e) {
            throw new InterruptedException(e.getMessage());
        } catch (ExecutionException e) {
            throw new ExecutionException(e.getCause());
        }
    }

    public UserDto giveManagerRight(Long id) throws UserNotFoundException, UserAlreadyManagerException {
        Optional<User> user = this.userRepository.findById(id);
        if (!user.isPresent()) {
            throw new UserNotFoundException(id);
        }
        Role role = this.roleRepository.findByName("ROLE_MANAGER");
        if (user.get().getRoles().contains(role)) {
            throw new UserAlreadyManagerException(id);
        }
        user.get().getRoles().add(role);
        return UserMapper.instance.toDto(this.userRepository.save(user.get()));
    }

    public UserDto removeManagerRight(Long id) throws UserNotFoundException {
        Optional<User> user = this.userRepository.findById(id);
        if (!user.isPresent()) {
            throw new UserNotFoundException(id);
        }
        Role role = this.roleRepository.findByName("ROLE_MANAGER");
        user.get().getRoles().remove(role);
        return UserMapper.instance.toDto(this.userRepository.save(user.get()));
    }

    public UserDto givePresidentRight(Long id) throws UserNotFoundException {
        Optional<User> user = this.userRepository.findById(id);
        if (!user.isPresent()) {
            throw new UserNotFoundException(id);
        }
        Role role = this.roleRepository.findByName("ROLE_PRESIDENT");
        user.get().getRoles().add(role);

        User oldPresident = this.userRepository.findPresident();
        oldPresident.getRoles().remove(role);
        this.userRepository.save(oldPresident);
        // Remove duplicates roles (don't know why there are duplicates roles but remove it...)
        user.get().setRoles(user.get().getRoles().stream().distinct().collect(Collectors.toList()));
        return UserMapper.instance.toDto(this.userRepository.save(user.get()));
    }

    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return this.encoder.matches(oldPassword, user.getPassword());
    }

    public UserDto changeUserPassword(User user, String password) {
        user.setPassword(this.encoder.encode(password));
        if (!user.isPasswordChanged()) {
            user.setPasswordChanged(true);
        }
        User userSave = this.userRepository.save(user);
        return UserMapper.instance.toDto(userSave);
    }

    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        this.passwordResetTokenRepository.save(myToken);
    }

    public void deleteUser(Long id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new UserNotFoundException(id);
        }
        if (user.get().getRoles().contains(this.roleRepository.findByName("ROLE_PRESIDENT"))) {
            throw new UserIsPresidentException(id);
        }
        if (user.get().getPlayer() != null) {
            throw new UserIsPlayerException(id);
        }
        if (user.get().getRoles().contains(this.roleRepository.findByName("ROLE_COACH"))) {
            for (Team team :
                    user.get().getCoachedTeams()) {
                team.setCoach(null);
            }
            user.get().getCoachedTeams().clear();
        }
        this.userRepository.delete(user.get());
        this.firebaseService.deleteUser(user.get().getUsername());
    }

    public List<User> convertListIdUserToUser(List<Long> idsUser) throws UserNotFoundException {
        List<User> users = new ArrayList<>();
        for (Long id:
                idsUser) {
            Optional<User> user = this.userRepository.findById(id);
            if (!user.isPresent()) {
                throw new UserNotFoundException(id);
            }
            users.add(user.get());
        }
        return users;
    }
}
