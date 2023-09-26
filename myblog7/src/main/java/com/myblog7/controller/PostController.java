package com.myblog7.controller;

import com.myblog7.entity.*;
import com.myblog7.payload.*;
import com.myblog7.service.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {

        this.postService = postService;
    }
    //http://localhost:8080/api/post
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> savePost(@Valid @RequestBody PostDto postDto, BindingResult result){
        if(result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        PostDto dto = postService.savePost(postDto);
        return new ResponseEntity<>(dto,HttpStatus.CREATED) ;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String>deletePost(@PathVariable("id") long id){
        postService.deletePost(id);
        return new ResponseEntity<>("Post is Deleted",HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto>updatePost(@PathVariable("id") long id,@RequestBody  PostDto postDto ){
        PostDto dto = postService.updatePost(id, postDto);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PostDto>getPostById(@PathVariable("id") long id){
        PostDto dto = postService.getPostById(id);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
    //http://localhost:8080/api/post?pageNo=0&pageSize=5&sortBy=title&sortDirection=desc
    @GetMapping
    public ResponseEntity<PostResponse>getAllPost(
            @RequestParam(value="pageNo",defaultValue = "0",required = false) int pageNo,
            @RequestParam(value="pageSize",defaultValue = "3",required = false)int pageSize,
            @RequestParam(value="sortBy",defaultValue = "id",required = false)String sortBy,
            @RequestParam(value="sortDirection", defaultValue = "asc", required = false) String sortDirection
    ){
        PostResponse postResponse = postService.getAllPosts( pageNo , pageSize,sortBy,sortDirection);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }


}
