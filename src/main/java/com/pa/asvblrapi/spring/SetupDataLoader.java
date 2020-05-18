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
    private CategoryRepository categoryRepository;

    @Autowired
    private ClothingSizeRepository clothingSizeRepository;

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
        final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        final Privilege subscriptionManagement = createPrivilegeIfNotFound("SUBSCRIPTION_MANAGEMENT");
        // Create initial roles
        final List<Privilege> userPrivileges = new ArrayList<Privilege>(Arrays.asList(readPrivilege));
        final List<Privilege> adminPrivileges = new ArrayList<Privilege>(Arrays.asList(readPrivilege, subscriptionManagement));
        final List<Privilege> presidentPrivileges = new ArrayList<Privilege>(Arrays.asList(readPrivilege, subscriptionManagement));

        final Role userRole = createRoleIfNotFound("ROLE_USER", userPrivileges);
        final Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        final Role presidentRole = createRoleIfNotFound("ROLE_PRESIDENT", presidentPrivileges);

        try {
            createUserIfNotFound("testUser@test.com", "userTest", "Test", "Test",
                    "123456", new ArrayList<Role>(Arrays.asList(userRole)));
            createUserIfNotFound("testAdmin@test.com", "adminTest", "Test", "Test",
                    "123456", new ArrayList<Role>(Arrays.asList(adminRole)));
            createUserIfNotFound("testPresident@test.com", "presidentTest", "Test", "Test",
                    "123456", new ArrayList<Role>(Arrays.asList(presidentRole)));
        }
        catch (Exception e) {
            new Exception(e.getMessage());
        }

        // Create fake Categories
        createCategoryIfNotFound("Homme 6vs6");
        createCategoryIfNotFound("Homme 4vs4");
        createCategoryIfNotFound("Femme 6vs6");
        createCategoryIfNotFound("Femme 4vs4");

        // Create PaymentMode
        createPaymentModeIfNotFound("Chèque banquaire");
        createPaymentModeIfNotFound("Pass 92 : collégiens domiciliés aux Hauts-de-Seine");
        createPaymentModeIfNotFound("Coupons sport");
        createPaymentModeIfNotFound("Chèques vacances");
        createPaymentModeIfNotFound("Bons C.A.F");
        createPaymentModeIfNotFound("Espèces");

        // Create CloshingSize
        createClothingSizeIfNotFound("XS");
        createClothingSizeIfNotFound("S");
        createClothingSizeIfNotFound("M");
        createClothingSizeIfNotFound("L");
        createClothingSizeIfNotFound("XL");
        createClothingSizeIfNotFound("XXL");

        alreadySetup = true;
    }


    private final Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }

    private final Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
        }
        role.setPrivileges(privileges);
        role = roleRepository.save(role);
        return role;
    }

    private final User createUserIfNotFound(final String email, final String username, final String firstName,
                                            final String lastName, final String password, final Collection<Role> roles)
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
        }
        user.setRoles(roles);
        user = userRepository.save(user);
        try {
            this.firebaseService.saveUserDetails(new UserDtoFirebase(username, firstName, lastName, email));
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return user;
    }

    private final PaymentMode createPaymentModeIfNotFound(final String name) {
        PaymentMode paymentMode = paymentModeRepository.findByName(name);
        if (paymentMode == null) {
            paymentMode = new PaymentMode(name);
            paymentMode = paymentModeRepository.save(paymentMode);
        }
        return paymentMode;
    }

    private final Category createCategoryIfNotFound(final String name) {
        Category category = categoryRepository.findByName(name);
        if (category == null) {
            category = new Category(name);
            category = categoryRepository.save(category);
        }
        return category;
    }

    private ClothingSize createClothingSizeIfNotFound(final String name) {
        ClothingSize clothingSize = this.clothingSizeRepository.findByName(name);
        if (clothingSize == null) {
            clothingSize = new ClothingSize(name);
            clothingSize = this.clothingSizeRepository.save(clothingSize);
        }
        return clothingSize;
    }
}
