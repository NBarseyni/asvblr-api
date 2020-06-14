package com.pa.asvblrapi.spring;

import com.pa.asvblrapi.dto.UserDtoFirebase;
import com.pa.asvblrapi.entity.*;
import com.pa.asvblrapi.repository.*;
import com.pa.asvblrapi.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentModeRepository paymentModeRepository;

    @Autowired
    private SubscriptionCategoryRepository subscriptionCategoryRepository;

    @Autowired
    private ClothingSizeRepository clothingSizeRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private TeamCategoryRepository teamCategoryRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private FirebaseService firebaseService;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (alreadySetup) {
            return;
        }

        // Create initial privileges
        final Privilege articleManagement = createPrivilegeIfNotFound("ARTICLE_MANAGEMENT");
        final Privilege signUp = createPrivilegeIfNotFound("SIGNUP");
        final Privilege documentManagement = createPrivilegeIfNotFound("DOCUMENT_MANAGEMENT");
        final Privilege matchManagement = createPrivilegeIfNotFound("MATCH_MANAGEMENT");
        final Privilege playerManagement = createPrivilegeIfNotFound("PLAYER_MANAGEMENT");
        final Privilege playerRead = createPrivilegeIfNotFound("PLAYER_READ");
        final Privilege userManagement = createPrivilegeIfNotFound("USER_MANAGEMENT");
        final Privilege userRead = createPrivilegeIfNotFound("USER_READ");
        final Privilege seasonManagement = createPrivilegeIfNotFound("SEASON_MANAGEMENT");
        final Privilege subscriptionManagement = createPrivilegeIfNotFound("SUBSCRIPTION_MANAGEMENT");
        final Privilege teamManagement = createPrivilegeIfNotFound("TEAM_MANAGEMENT");
        final Privilege teamManagementCoach = createPrivilegeIfNotFound("TEAM_MANAGEMENT_COACH");

        // Create initial roles
        final List<Privilege> presidentPrivileges = new ArrayList<Privilege>(Arrays.asList(articleManagement, signUp,
                documentManagement, matchManagement, playerManagement, userManagement, seasonManagement,
                subscriptionManagement, teamManagement));
        final List<Privilege> managerPrivileges = new ArrayList<Privilege>(Arrays.asList(articleManagement, documentManagement,
                matchManagement, playerManagement, subscriptionManagement, teamManagement));
        final List<Privilege> coachPrivileges = new ArrayList<Privilege>(Arrays.asList(matchManagement, teamManagementCoach));
        final List<Privilege> playerPrivileges = new ArrayList<Privilege>(Arrays.asList(playerRead));

        final Role playerRole = createRoleIfNotFound("ROLE_PLAYER", playerPrivileges);
        final Role coachRole = createRoleIfNotFound("ROLE_COACH", coachPrivileges);
        final Role managerRole = createRoleIfNotFound("ROLE_MANAGER", managerPrivileges);
        final Role presidentRole = createRoleIfNotFound("ROLE_PRESIDENT", presidentPrivileges);

        try {
            createUserIfNotFound("testUser@test.com", "userTest", "User", "Test",
                    "123456", new ArrayList<Role>(Arrays.asList(playerRole)));
            createUserIfNotFound("testAdmin@test.com", "adminTest", "Manager", "Test",
                    "123456", new ArrayList<Role>(Arrays.asList(managerRole)));
            createUserIfNotFound("testCoach@test.com", "coachTest", "Coach", "Test",
                    "123456", new ArrayList<Role>(Arrays.asList(coachRole)));
            createUserIfNotFound("testPresident@test.com", "presidentTest", "Président", "Test",
                    "123456", new ArrayList<Role>(Arrays.asList(presidentRole)));
        }
        catch (Exception e) {
            new Exception(e.getMessage());
        }

        // Create SubscriptionCategories
        createSubscriptionCategoryIfNotFound("Loisir");
        createSubscriptionCategoryIfNotFound("Loisir compétition");
        createSubscriptionCategoryIfNotFound("Départemental compétition");
        createSubscriptionCategoryIfNotFound("Régional compétition");

        // Create PaymentMode
        createPaymentModeIfNotFound("Chèque banquaire");
        createPaymentModeIfNotFound("Pass 92 : collégiens domiciliés aux Hauts-de-Seine");
        createPaymentModeIfNotFound("Coupons sport");
        createPaymentModeIfNotFound("Chèques vacances");
        createPaymentModeIfNotFound("Bons C.A.F");
        createPaymentModeIfNotFound("Espèces");

        // Create ClothingSize
        createClothingSizeIfNotFound("XS");
        createClothingSizeIfNotFound("S");
        createClothingSizeIfNotFound("M");
        createClothingSizeIfNotFound("L");
        createClothingSizeIfNotFound("XL");
        createClothingSizeIfNotFound("XXL");

        // Create Position
        createPositionIfNotFound("Attaquant ailier", "R4");
        createPositionIfNotFound("Centrale", "C");
        createPositionIfNotFound("Passeur", "Pa");
        createPositionIfNotFound("Pointu", "Pt");
        createPositionIfNotFound("Libéro", "L");
        createPositionIfNotFound("Non défini", "");

        // Create Team Category
        createTeamCategoryIfNotFound("National homme");
        createTeamCategoryIfNotFound("Pré-national homme");
        createTeamCategoryIfNotFound("Régional homme");
        createTeamCategoryIfNotFound("Pré-régional homme");
        createTeamCategoryIfNotFound("Départemental homme");
        createTeamCategoryIfNotFound("National femme");
        createTeamCategoryIfNotFound("Pré-national femme");
        createTeamCategoryIfNotFound("Régional homme");
        createTeamCategoryIfNotFound("Pré-régional femme");
        createTeamCategoryIfNotFound("Départemental femme");
        createTeamCategoryIfNotFound("Loisir mixte 6x6");
        createTeamCategoryIfNotFound("Loisir femme 4x4");
        createTeamCategoryIfNotFound("Loisir homme 4x4");
        createTeamCategoryIfNotFound("M20 garçons");
        createTeamCategoryIfNotFound("M20 filles");
        createTeamCategoryIfNotFound("M17 garçons");
        createTeamCategoryIfNotFound("M17 filles");
        createTeamCategoryIfNotFound("M15 garçons");
        createTeamCategoryIfNotFound("M15 filles");
        createTeamCategoryIfNotFound("M13 garçons");
        createTeamCategoryIfNotFound("M13 filles");
        createTeamCategoryIfNotFound("M13 mixte");
        createTeamCategoryIfNotFound("M11 mixte");
        createTeamCategoryIfNotFound("M9 mixte");
        createTeamCategoryIfNotFound("M7 mixte");
        createTeamCategoryIfNotFound("Baby-volley");
        createTeamCategoryIfNotFound("Sanofi");

        alreadySetup = true;
    }


    private Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }

    private Role createRoleIfNotFound(final String name, final List<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
        }
        role.setPrivileges(privileges);
        role = roleRepository.save(role);
        return role;
    }

    private User createUserIfNotFound(final String email, final String username, final String firstName,
                                            final String lastName, final String password, final List<Role> roles)
            throws Exception {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(encoder.encode(password));
            user.setEnabled(true);
            user.setRoles(roles);
            user = userRepository.save(user);
            try {
                this.firebaseService.saveUserDetails(new UserDtoFirebase(user.getId(), username, firstName, lastName, email));
            }
            catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }
        return user;
    }

    private PaymentMode createPaymentModeIfNotFound(final String name) {
        PaymentMode paymentMode = paymentModeRepository.findByName(name);
        if (paymentMode == null) {
            paymentMode = new PaymentMode(name);
            paymentMode = paymentModeRepository.save(paymentMode);
        }
        return paymentMode;
    }

    private SubscriptionCategory createSubscriptionCategoryIfNotFound(final String name) {
        SubscriptionCategory subscriptionCategory = subscriptionCategoryRepository.findByName(name);
        if (subscriptionCategory == null) {
            subscriptionCategory = new SubscriptionCategory(name);
            subscriptionCategory = subscriptionCategoryRepository.save(subscriptionCategory);
        }
        return subscriptionCategory;
    }

    private ClothingSize createClothingSizeIfNotFound(final String name) {
        ClothingSize clothingSize = this.clothingSizeRepository.findByName(name);
        if (clothingSize == null) {
            clothingSize = new ClothingSize(name);
            clothingSize = this.clothingSizeRepository.save(clothingSize);
        }
        return clothingSize;
    }

    private Position createPositionIfNotFound(final String name, final String shortName) {
        Position position = this.positionRepository.findByName(name);
        if (position == null) {
            position = new Position(name, shortName);
            position = this.positionRepository.save(position);
        }
        return position;
    }

    private TeamCategory createTeamCategoryIfNotFound(final String name) {
        TeamCategory teamCategory = this.teamCategoryRepository.findByName(name);
        if (teamCategory == null) {
            teamCategory = new TeamCategory(name);
            teamCategory = this.teamCategoryRepository.save(teamCategory);
        }
        return teamCategory;
    }
}
