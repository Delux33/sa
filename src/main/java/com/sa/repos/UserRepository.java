package com.sa.repos;

import com.sa.entity.User;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@Hidden
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByName(String userName);
}
