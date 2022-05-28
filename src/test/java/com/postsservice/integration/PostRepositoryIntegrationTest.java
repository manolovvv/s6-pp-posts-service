package com.postsservice.integration;

import com.postsservice.Model.Post;
import com.postsservice.Repository.CommentRepository;
import com.postsservice.Repository.PostRepository;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class PostRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void whenFindById_thenReturnPost(){
        Post post = new Post();
        post.setText("text");
        postRepository.findAll();
        entityManager.persistAndFlush(post);

        Post found = postRepository.getOne(1L);
        assertThat(found.getId()).isEqualTo(post.getId());
    }

    @Test
    public void whenInvalidId_thenReturnNull(){
        Optional<Post> fromDb = postRepository.findById(3L);

        assertThat(fromDb.isEmpty()).isEqualTo(true);
    }
}
