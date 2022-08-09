package com.ghost.recipewebapp.repository;

import com.ghost.recipewebapp.entity.Step;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StepRepository extends CrudRepository<Step, Long> {
}