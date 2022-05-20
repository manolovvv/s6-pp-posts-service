package com.postsservice.Service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.postsservice.Model.Category;
import com.postsservice.Model.Comment;
import com.postsservice.Model.Post;
import com.postsservice.Repository.CommentRepository;
import com.postsservice.Repository.PostRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private final PostRepository postRepository;

    @Autowired
    private final CommentRepository commentRepository;

    File file = ResourceUtils.getFile("classpath:key.json");
    InputStream in = new FileInputStream(file);

    Credentials credentials = GoogleCredentials.fromStream(in);
    Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId("kalve-349610").build().getService();
    Bucket bucket = storage.get("kalve-post-bucket");


    public PostServiceImpl(PostRepository postRepository, CommentRepository commentRepository) throws IOException {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @KafkaListener(topics= {"reporting"}, groupId="reporting")
    public void consume(ConsumerRecord<String, String> record) {
        System.out.println("received = " + record.value() + " with key " + record.key());
    }



    @Override
    public Post createNewPost(Post post, MultipartFile image) throws IOException {
        Post createdPost = postRepository.save(post);
        if(!image.isEmpty()){

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

    private String uploadImage(MultipartFile image, Long id) throws IOException {
        byte[] bytes = image.getBytes();

        Blob blob = bucket.create(id.toString(),bytes,image.getContentType());
        System.out.println(blob.getMediaLink());
        System.out.println(blob.getSelfLink());
        System.out.println(blob.getMetadata());
        return "https://storage.googleapis.com/kalve-post-bucket/"+id.toString();
    }
}
