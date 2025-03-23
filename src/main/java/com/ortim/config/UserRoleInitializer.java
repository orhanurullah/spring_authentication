package com.ortim.config;

import com.ortim.core.utils.EntityConstants;
import com.ortim.model.Role;
import com.ortim.model.User;
import com.ortim.repository.RoleRepository;
import com.ortim.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserRoleInitializer implements CommandLineRunner {

    @Value("${settings.root.email}")
    private String rootEmail;

    @Value("${settings.root.password}")
    private String rootPassword;

    @Value("${settings.root.username}")
    private String rootUsername;

    @Value("${settings.root.name}")
    private String rootName;

    @Value("${settings.root.lastname}")
    private String rootLastname;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void run(String... args) {
        initializeRoles();
        initializeRootUser();
    }

    private void initializeRootUser(){
        if(userRepository.findByEmailAddress(rootEmail).isPresent()){
            log.info("Root user already exists: {}", rootEmail);
            return;
        }
        Role baseRole = roleRepository.findByName(EntityConstants.baseRoleName);
        Role secondaryRole = roleRepository.findByName(EntityConstants.secondaryRoleName);
        Role primaryRole = roleRepository.findByName(EntityConstants.primaryRoleName);
        Set<Role> roles = new HashSet<>(Arrays.asList(baseRole, secondaryRole, primaryRole));
        User user = User.builder()
                .email(rootEmail)
                .username(rootUsername)
                .name(rootName)
                .lastName(rootLastname)
                .password(passwordEncoder.encode(rootPassword))
                .isDeleted(false)
                .isActive(true)
                .roles(roles)
                .build();
        userRepository.save(user);
        log.info("Root user created: {}", rootEmail);
    }

    private void initializeRoles() {
        List<String> roleNames = Arrays.asList(
                EntityConstants.primaryRoleName,
                EntityConstants.secondaryRoleName,
                EntityConstants.baseRoleName
        );

        List<String> roleDescriptions = Arrays.asList(
                EntityConstants.primaryRoleDescription,
                EntityConstants.secondaryRoleDescription,
                EntityConstants.baseRoleDescription
        );

        for (int i = 0; i < roleNames.size(); i++) {
            String roleName = roleNames.get(i);
            String roleDescription = roleDescriptions.get(i);

            if (!roleRepository.existsByNameIgnoreCase(roleName)) {
                try {
                    Role role = Role.builder()
                            .name(roleName)
                            .description(roleDescription)
                            .build();

                    roleRepository.save(role);
                    log.info("Role created: {}", roleName);
                } catch (Exception e) {
                    log.error("Error creating role {}: {}", roleName, e.getMessage());
                }
            }
        }
    }
}