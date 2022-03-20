package com.postsservice.Service;

import com.postsservice.Model.Category;
import com.postsservice.Model.Comment;
import com.postsservice.Model.Post;
import com.postsservice.Repository.CommentRepository;
import com.postsservice.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private final PostRepository postRepository;

    @Autowired
    private final CommentRepository commentRepository;


    public PostServiceImpl(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }


    @Override
    public Post createNewPost(Post post) {
        return postRepository.save(post);
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
    public List<Post> getAllPost() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getPostsByCategory(Category category) {
        return postRepository.findAllByCategory(category);
    }

    @Override
    public String deletePost(Long id) {
        if(postRepository.existsById(id))
        {
            postRepository.deleteById(id);
            return "Successfully deleted";
        }
        return "No post find with that id";
    }

    @Override
    public String addCommentToPost(Long id, Comment comment){
        if(postRepository.existsById(id)){
            Post post = postRepository.findById(id).get();
            comment.setPost(post);
            List<Comment> comments = post.getComments();
            comments.add(comment);
            post.setComments(comments);
            postRepository.save(post);
            return "comment added";
        }
        return "post not found";
    }

    @Override
    public String deleteComment(Long id) {
        if(commentRepository.existsById(id)){
            Comment comment = commentRepository.findById(id).get();
            Post post = comment.getPost();
            post.removeComment(comment);
            postRepository.save(post);
            return "deleted";
        };
        return "no comment with this id found";
    }
}
