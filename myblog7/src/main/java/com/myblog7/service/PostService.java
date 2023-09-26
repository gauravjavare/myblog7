
package com.myblog7.service;

import com.myblog7.payload.*;

import java.util.List;

public interface PostService {

    public PostDto savePost(PostDto postDto);


    void deletePost(long id);

    PostDto updatePost(long id, PostDto postDto);

    PostDto getPostById(long id);

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDirection);
}
