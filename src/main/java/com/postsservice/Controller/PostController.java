package com.postsservice.Controller;

import com.postsservice.Model.Category;
import com.postsservice.Model.Comment;
import com.postsservice.Model.Post;
import com.postsservice.Service.PostService;
import com.postsservice.Service.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/posts")
public class PostController {
    @Autowired
    PostServiceImpl postService;

    @PostMapping
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
