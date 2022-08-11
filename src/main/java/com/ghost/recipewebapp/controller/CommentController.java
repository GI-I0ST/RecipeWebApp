package com.ghost.recipewebapp.controller;

import com.ghost.recipewebapp.entity.Comment;
import com.ghost.recipewebapp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping("/new")
    public ResponseEntity<?> addNewComment(@RequestParam(name = "recipe_id") Long recipeId, @RequestBody @Valid Comment comment) {
        Long id = commentService.addComment(recipeId, comment);

        return new ResponseEntity<>(Map.of("id", id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommentById(@PathVariable Long id) {
        commentService.deleteCommentById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
