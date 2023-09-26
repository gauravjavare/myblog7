package com.myblog7.service.impl;

import com.myblog7.entity.Comment;
import com.myblog7.entity.Post;
import com.myblog7.exception.BlogApiException;
import com.myblog7.exception.ResourceNotFound;
import com.myblog7.payload.CommentDto;
import com.myblog7.repository.CommentRepository;
import com.myblog7.repository.PostRepository;
import com.myblog7.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepo;
    private PostRepository PostRepo;

    public CommentServiceImpl(CommentRepository commentRepo, PostRepository postRepo, ModelMapper modelMapper) {
        this.commentRepo = commentRepo;
        PostRepo = postRepo;
        this.modelMapper = modelMapper;
    }

    private ModelMapper modelMapper;

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
//        System.out.println("Name: " + commentDto.getName());
//        System.out.println("Email: " + commentDto.getEmail());
//        System.out.println("Body: " + commentDto.getBody());
        Comment comment=mapToEntity(commentDto);
        Post post = PostRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Post Not Found With id: " + postId));
        comment.setPost(post);
        Comment savedComment = commentRepo.save(comment);
        CommentDto dto=mapToDto(savedComment);
        return dto;
    }

    @Override
    public List<CommentDto> findByPostId(long postId) {
        Post post = PostRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Post Not Found With Id " + postId));
        List<Comment> entityComments = commentRepo.findByPostId(postId);
        List<CommentDto> commentDtos = entityComments.stream().map(this::mapToDto).collect(Collectors.toList());
        return commentDtos ;
    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        Post post = PostRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Post Not Found With Id " + postId));
        Comment comment = commentRepo.findById(commentId).orElseThrow(
                () -> new ResourceNotFound("Comment Not Found with Id " + commentId));
        if(comment.getPost().getId()!=(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment does not belong to Post");
        }
        CommentDto dto = mapToDto(comment);
        return dto;
        }

    @Override
    public CommentDto updateComment(long postId, CommentDto commentDto, long commentId) {
        Post post = PostRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Post Not Found With Id " + postId));
        Comment comment = commentRepo.findById(commentId).orElseThrow(
                () -> new ResourceNotFound("Comment Not Found with Id " + commentId));
        if(comment.getPost().getId()!=(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment does not belong to Post");
        }
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        Comment savedComment = commentRepo.save(comment);
        CommentDto dto = mapToDto(savedComment);
        return dto;
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        Post post = PostRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Post Not Found With I " + postId));
        Comment comment = commentRepo.findById(commentId).orElseThrow(
                () -> new ResourceNotFound("Comment Not Found with Id " + commentId));
        if (comment.getPost().getId() != (post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment does not belong to Post");
        }
               commentRepo.delete(comment);
    }

    @Override
    public List<CommentDto> getAllComments() {
        List<Comment> comments = commentRepo.findAll();
        List<CommentDto> commentDtos = comments.stream().map(this::mapToDto).collect(Collectors.toList());
        return commentDtos ;
    }


    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment = modelMapper.map(commentDto, Comment.class);
        return comment;
    }
    private CommentDto mapToDto(Comment savedComment) {
        CommentDto dto = modelMapper.map(savedComment, CommentDto.class);
        return dto;
    }

}
