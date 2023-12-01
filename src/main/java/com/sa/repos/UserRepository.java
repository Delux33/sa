package com.sa.repos;

import com.sa.entity.User;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@Hidden
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByName(String userName);
}
