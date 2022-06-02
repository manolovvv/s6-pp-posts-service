package com.postsservice.Service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.postsservice.Model.Category;
import com.postsservice.Model.Comment;
import com.postsservice.Model.Post;
import com.postsservice.Repository.CommentRepository;
import com.postsservice.Repository.PostRepository;
import com.postsservice.component.Request;
import com.postsservice.dto.CommentDTO;
import com.postsservice.dto.PostDTO;
import com.postsservice.dto.UserDTO;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private final PostRepository postRepository;

    @Autowired
    private final CommentRepository commentRepository;

    @Autowired
    Request request;

    @Autowired
    JwtUtil jwtUtil;


    Storage storage = StorageOptions.getDefaultInstance().getService();
    Bucket bucket = storage.get("kalve-posts-bucket");


    public PostServiceImpl(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    private Long getIdFromJwtToken(String token){
        return jwtUtil.getIdFromToken(token);
    }


    @Override
    public Post createNewPost(Post post, MultipartFile image, String token) throws IOException {
        String jwtToken = token.substring(7);
        Long userId = getIdFromJwtToken(jwtToken);
        post.setUserId(userId);
        Post createdPost = postRepository.save(post);
        if( image != null && !image.isEmpty() ){

        String authenticatedURLImage = uploadImage(image, post.getId());
        createdPost.setImage(authenticatedURLImage);
        }
        return createdPost;
    }

    @Override
    public Post getPostById(Long id) {
        if(postRepository.existsById(id))
        {
            return postRepository.findById(id).get();
        }
        return null;


    }



    @Override
    public List<PostDTO> getAllPost() {
        List<Post> allPosts = postRepository.findAll();
        List<PostDTO> posts = new ArrayList<>();
        allPosts.forEach(post -> {
            PostDTO postDTO = new PostDTO();

            UserDTO user = request.getUserDetailsById(post.getUserId());
            postDTO.setUser(user);
            postDTO.setCategory(post.getCategory());
            postDTO.setComments(post.getComments());
            postDTO.setCreatedOn(post.getCreatedOn());
            postDTO.setImage(post.getImage());
            postDTO.setText(post.getText());
            postDTO.setId(post.getId());

            posts.add(postDTO);
        });
        return posts;
    }

    @Override
    public List<Post> getPostsByCategory(Category category) {
        return postRepository.findAllByCategory(category);
    }

    @Override
    public String deletePost(Long id, String token) {

        if(postRepository.existsById(id))
        {
            postRepository.deleteById(id);

            return "Successfully deleted";

        }
        return "No post find with that id";
    }

    @Override
    public String addCommentToPost(Long id, CommentDTO comment, String token){
        String jwtToken = token.substring(7);
        Long userId = getIdFromJwtToken(jwtToken);


        Comment entityComment = new Comment();
        entityComment.setText(comment.getText());
        entityComment.setUserId(userId);
        entityComment.setCreatedOn(comment.getCreatedOn());
        if(postRepository.existsById(id)){
            Post post = postRepository.findById(id).get();
            entityComment.setPost(post);
            List<Comment> comments = post.getComments();
            comments.add(entityComment);
            post.setComments(comments);
            postRepository.save(post);
            return "comment added";
        }
        return "post not found";
    }

    @Override
    public String deleteComment(Long id, String token) {

        if(commentRepository.existsById(id)){
            Comment comment = commentRepository.findById(id).get();
            Post post = comment.getPost();
            post.removeComment(comment);
            postRepository.save(post);
            return "deleted";

        };
        return "no comment with this id found";
    }

    private String uploadImage(MultipartFile image, Long id) throws IOException {
        byte[] bytes = image.getBytes();

       Blob blob = bucket.create(id.toString(),bytes,image.getContentType());

        return "https://storage.googleapis.com/kalve-post-bucket/"+id.toString();
    }
}
