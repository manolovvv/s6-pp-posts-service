package com.postsservice.Service;

import com.postsservice.Model.Category;
import com.postsservice.Model.Comment;
import com.postsservice.Model.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    Post createNewPost(Post post,MultipartFile file) throws IOException;
    Post getPostById(Long id);
    List<Post> getAllPost();
    List<Post> getPostsByCategory(Category category);
    String deletePost(Long id);
    String addCommentToPost(Long id, Comment comment);
    String deleteComment(Long id);


}
