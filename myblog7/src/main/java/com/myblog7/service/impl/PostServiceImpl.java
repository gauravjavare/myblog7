package com.myblog7.service.impl;

import com.myblog7.entity.*;
import com.myblog7.exception.ResourceNotFound;
import com.myblog7.payload.*;
import com.myblog7.repository.*;
import com.myblog7.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository PostRepo;
    private ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepo, ModelMapper modelMapper) {
        this.PostRepo = postRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostDto savePost(PostDto postDto){
        Post post = mapToEntity(postDto);
        Post savedPost = PostRepo.save(post);
        PostDto dto = mapToDto(savedPost);
        return dto;
    }

    @Override
    public void deletePost(long id) {

        PostRepo.deleteById(id);
    }

    @Override
    public PostDto updatePost(long id, PostDto postDto) {
        Post post = PostRepo.findById(id).orElseThrow(
                () -> new ResourceNotFound("Post Not Found with Id " + id));
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        Post save = PostRepo.save(post);
        PostDto dto = mapToDto(save);
        return dto;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = PostRepo.findById(id).orElseThrow(
                () -> new RuntimeException("Post Not Found with id " + id));
        PostDto dto = mapToDto(post);
        return dto;
    }
    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<Post> pagePosts = PostRepo.findAll(pageable);
        List<Post> posts = pagePosts.getContent();
        List<PostDto> postDtos = posts.stream() //.map(post->mapToDto(post)) using Lambda Expression
                .map(this::mapToDto) // Assuming mapToDto is a method in the same class
                .collect(Collectors.toList());
        PostResponse postResponse= new PostResponse();
        postResponse.setPostDto(postDtos);
        postResponse.setPageNo(pagePosts.getNumber());
        postResponse.setPageSize(pagePosts.getSize());
        postResponse.setTotalElements(pagePosts.getTotalElements());
        postResponse.setTotalPages(pagePosts.getTotalPages());
        postResponse.setLastPage(pagePosts.isLast());
        return postResponse;
    }

    public Post mapToEntity(PostDto postDto){
        Post post = modelMapper.map(postDto, Post.class);
//        Post post = new Post();
//        post.setId(postDto.getId());
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;
    }
    public PostDto mapToDto(Post savedPost){
        PostDto dto = modelMapper.map(savedPost, PostDto.class);
//        PostDto dto = new PostDto();
//        dto.setId(savedPost.getId());
//        dto.setTitle(savedPost.getTitle());
//        dto.setDescription(savedPost.getDescription());
//        dto.setContent(savedPost.getContent());
        return dto;
    }






}
