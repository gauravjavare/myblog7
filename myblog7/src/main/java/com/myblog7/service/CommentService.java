package com.myblog7.service;

import com.myblog7.entity.Comment;
import com.myblog7.payload.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(long postId, CommentDto commentDto);

    public List<CommentDto> findByPostId(long postId);

    public CommentDto getCommentById(long postId, long commentId);

    public CommentDto updateComment(long postId, CommentDto commentDto, long commentId);

  public  void deleteComment(long postId, long commentId);

    public List<CommentDto> getAllComments();
}
