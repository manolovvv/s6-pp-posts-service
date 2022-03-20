package com.postsservice.Service;

import com.postsservice.Model.Category;
import com.postsservice.Model.Comment;
import com.postsservice.Model.Post;

import java.util.List;

public interface PostService {
    Post createNewPost(Post post);
    Post getPostById(Long id);
    List<Post> getAllPost();
    List<Post> getPostsByCategory(Category category);
    String deletePost(Long id);
    String addCommentToPost(Long id, Comment comment);
    String deleteComment(Long id);

}
