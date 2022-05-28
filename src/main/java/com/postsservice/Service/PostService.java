package com.postsservice.Service;

import com.postsservice.Model.Category;
import com.postsservice.Model.Comment;
import com.postsservice.Model.Post;
import com.postsservice.dto.CommentDTO;
import com.postsservice.dto.PostDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    Post createNewPost(Post post,MultipartFile file) throws IOException;
    Post getPostById(Long id);
    List<PostDTO> getAllPost();
    List<Post> getPostsByCategory(Category category);
    String deletePost(Long id);
    String addCommentToPost(Long id, CommentDTO comment);
    String deleteComment(Long id);


}
