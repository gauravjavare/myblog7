package com.myblog7.controller;

import com.myblog7.entity.Comment;
import com.myblog7.payload.CommentDto;
import com.myblog7.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {
    private CommentService commentService;
    public CommentController(CommentService commentService) {

        this.commentService = commentService;
    }
    //http://localhost:8080/api/post/postId/comment
@PostMapping("/post/{postId}/comment")
public ResponseEntity<CommentDto>createComment(@PathVariable("postId")long postId, @RequestBody CommentDto commentDto){
    CommentDto dto = commentService.createComment(postId, commentDto);
    return new ResponseEntity<>(dto, HttpStatus.CREATED);
}
@GetMapping("/post/{postId}/comments")
public ResponseEntity<List<CommentDto>>findBYPostId(@PathVariable("postId")long postId){
    List<CommentDto> commentDtos = commentService.findByPostId(postId);
    return new ResponseEntity<>(commentDtos,HttpStatus.OK);
}
@GetMapping("/post/{postId}/comment/{commentId}")
public ResponseEntity<CommentDto>getCommentById(@PathVariable("postId")long postId,
                                                @PathVariable("commentId")long commentId){
    CommentDto commentDto = commentService.getCommentById(postId, commentId);
    return new ResponseEntity<>(commentDto,HttpStatus.OK);
}
@GetMapping("/comments")
public ResponseEntity<List<CommentDto>>getAllComments(){
    List<CommentDto> commentDtos = commentService.getAllComments();
    return new ResponseEntity<>(commentDtos,HttpStatus.OK );
}
@PutMapping("/post/{postId}/comment/{commentId}")
public ResponseEntity<CommentDto>updateComment(@PathVariable("postId")long postId,
                                               @RequestBody CommentDto commentDto,
                                               @PathVariable("commentId")long commentId){
    CommentDto dto = commentService.updateComment(postId, commentDto, commentId);
    return new ResponseEntity<>(dto,HttpStatus.OK);
}
@DeleteMapping("/post/{postId}/comment/{commentId}")
public ResponseEntity<String>deleteComment(@PathVariable("postId")long postId,
                                           @PathVariable("commentId")long commentId){
        commentService.deleteComment(postId,commentId);

        return new ResponseEntity<>("Comment is Deleted",HttpStatus.OK);
}

}
