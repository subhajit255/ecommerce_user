package com.service.user.seeder;

import com.service.user.entities.Role;
import com.service.user.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleSeeder implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Role role = new Role();
        if (!roleRepository.existsById("USER")) {
            role.setName("USER");
            roleRepository.save(role);
        }
        if (!roleRepository.existsById("ADMIN")) {
            role.setName("ADMIN");
            roleRepository.save(role);
        }
    }
}
