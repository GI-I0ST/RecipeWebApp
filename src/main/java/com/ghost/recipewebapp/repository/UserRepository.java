package com.ghost.recipewebapp.repository;

import com.ghost.recipewebapp.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}