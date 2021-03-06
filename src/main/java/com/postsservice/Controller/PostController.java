package com.postsservice.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postsservice.Model.Category;
import com.postsservice.Model.Post;
import com.postsservice.Service.PostService;
import com.postsservice.Service.PostServiceImpl;
import com.postsservice.dto.CommentDTO;
import com.postsservice.dto.PostDTO;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping(value = "/")
public class PostController {

    @Autowired
    PostService postService;


    @PostMapping(value="/")
    public Post createNewPost(@RequestHeader(value="Authorization") String token,@RequestParam(value = "image", required = false) MultipartFile image,@RequestParam(value = "model",required = true) String model) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        Post post = mapper.readValue(model, Post.class);

        return postService.createNewPost(post,image, token);
    }

    @GetMapping(value="/")
    public List<PostDTO> getAllPosts(){
        return postService.getAllPost();
    }

    @GetMapping(value = "getpost/{id}")
    public Post getPost(@PathVariable String id){
        return postService.getPostById(Long.parseLong(id));
    }

    @DeleteMapping(value = "deletepost/{id}")
    public String deletePost(@PathVariable Long id, @RequestHeader(value = "Authorization") String token){
        return postService.deletePost(id, token);
    }

    @GetMapping(value = "getbycategory/{category}")
    public List<Post> getPostsByCategory(@PathVariable String category){
        return postService.getPostsByCategory(Category.valueOf(category));
    }

    @PostMapping(value = "addcomment/{id}")
    public String addCommentToPost(@PathVariable String id, @RequestBody CommentDTO comment, @RequestHeader(value = "Authorization") String token){

        return postService.addCommentToPost(Long.parseLong(id), comment, token);
    }

    @DeleteMapping(value = "deletecomment/{id}")
    public String deleteComment(@PathVariable String id, @RequestHeader(value = "Authorization") String token){
        return postService.deleteComment(Long.parseLong(id),token);
    }


}
