package com.postsservice.Controller;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.postsservice.Model.Category;
import com.postsservice.Model.Comment;
import com.postsservice.Model.Post;
import com.postsservice.Service.PostService;
import com.postsservice.Service.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@RestController
@RequestMapping(value = "/")
public class PostController {
    @Autowired
    PostServiceImpl postService;

    File file = ResourceUtils.getFile("classpath:key.json");
    InputStream in = new FileInputStream(file);

    Credentials credentials = GoogleCredentials.fromStream(in);
    Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId("kalve-349610").build().getService();
    Bucket bucket = storage.get("kalve-post-bucket");

    public PostController() throws IOException {
    }

    @GetMapping(value="/test")
    public String test(){
        String value = "ASDASD";
        byte[] bytes = value.getBytes(UTF_8);
        Blob blob = bucket.create("example",bytes);

        return blob.getBlobId().toString();
    }

    @GetMapping(value="/testGet/{blobId}")
    public byte[] testGet(@PathVariable String blobId){
        Blob blob = bucket.get("Basic CI for building the app.png");
        String value = new String(blob.getContent());

        return blob.getContent();
    }


    @PostMapping(value="/")
    public Post createNewPost(@RequestBody Post post){
        return postService.createNewPost(post);
    }

    @GetMapping(value="/")
    public List<Post> getAllPosts(){
        return postService.getAllPost();
    }

    @GetMapping(value = "getpost/{id}")
    public Post getPost(@PathVariable String id){
        //System.out.println(id);
        return postService.getPostById(Long.parseLong(id));
       // return null;
    }

    @DeleteMapping(value = "deletepost/{id}")
    public String deletePost(@PathVariable Long id){
        return postService.deletePost(id);
    }

    @GetMapping(value = "getbycategory/{category}")
    public List<Post> getPostsByCategory(@PathVariable String category){
        return postService.getPostsByCategory(Category.valueOf(category));
    }

    @PostMapping(value = "addcomment/{id}")
    public String addCommentToPost(@PathVariable String id, @RequestBody Comment comment){
        return postService.addCommentToPost(Long.parseLong(id), comment);
    }

    @DeleteMapping(value = "deletecomment/{id}")
    public String deleteComment(@PathVariable String id){
        return postService.deleteComment(Long.parseLong(id));
    }


}
