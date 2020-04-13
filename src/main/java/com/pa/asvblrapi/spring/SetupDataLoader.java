package com.pa.asvblrapi.spring;

import com.pa.asvblrapi.entity.Category;
import com.pa.asvblrapi.entity.PaymentMode;
import com.pa.asvblrapi.entity.Privilege;
import com.pa.asvblrapi.entity.Role;
import com.pa.asvblrapi.repository.CategoryRepository;
import com.pa.asvblrapi.repository.PaymentModeRepository;
import com.pa.asvblrapi.repository.PrivilegeRepository;
import com.pa.asvblrapi.repository.RoleRepository;
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
    private PaymentModeRepository paymentModeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder encoder;

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

        createRoleIfNotFound("ROLE_USER", userPrivileges);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_PRESIDENT", presidentPrivileges);

        // Create fake Categories
        createCategoryIfNotFound("Homme 6vs6");
        createCategoryIfNotFound("Homme 4vs4");
        createCategoryIfNotFound("Femme 6vs6");
        createCategoryIfNotFound("Femme 4vs4");

        // Create PaymentMode
        createPaymentModeIfNotFound("Chèques classiques de Banque");
        createPaymentModeIfNotFound("Pass 92 : collégiens domiciliés aux Hauts-de-Seine");
        createPaymentModeIfNotFound("Coupons sport");
        createPaymentModeIfNotFound("Chèques vacances");
        createPaymentModeIfNotFound("Bons C.A.F");
        createPaymentModeIfNotFound("Espèces");

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
}