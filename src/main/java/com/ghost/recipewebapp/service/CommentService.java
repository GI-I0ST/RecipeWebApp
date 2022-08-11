package com.ghost.recipewebapp.service;

import com.ghost.recipewebapp.entity.Comment;

public interface CommentService {
    Long addComment(Long recipeId, Comment comment);
    void deleteCommentById(Long id);
}
