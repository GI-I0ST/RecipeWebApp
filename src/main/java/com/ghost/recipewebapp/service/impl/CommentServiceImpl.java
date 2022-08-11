package com.ghost.recipewebapp.service.impl;

import com.ghost.recipewebapp.entity.Comment;
import com.ghost.recipewebapp.entity.Recipe;
import com.ghost.recipewebapp.repository.CommentRepository;
import com.ghost.recipewebapp.repository.RecipeRepository;
import com.ghost.recipewebapp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Long addComment(Long recipeId, Comment comment) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        comment.setRecipe(recipe);
        recipe.getCommentsList().add(comment);

        commentRepository.save(comment);

        return comment.getId();
    }

    @Override
    public void deleteCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        comment.getRecipe().getCommentsList().remove(comment);
        commentRepository.delete(comment);
    }
}
