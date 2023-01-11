package com.zahir.fathurrahman.springsecurity.repo;

import com.zahir.fathurrahman.springsecurity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {
    User findByUsername(String username);
}
