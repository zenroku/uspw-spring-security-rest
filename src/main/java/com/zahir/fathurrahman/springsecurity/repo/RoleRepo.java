package com.zahir.fathurrahman.springsecurity.repo;

import com.zahir.fathurrahman.springsecurity.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Long> {
    Role findByName(String name);
}
