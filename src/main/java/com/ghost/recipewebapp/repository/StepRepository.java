package com.ghost.recipewebapp.repository;

import com.ghost.recipewebapp.entity.Step;
import org.springframework.data.repository.CrudRepository;

public interface StepRepository extends CrudRepository<Step, Long> {
}