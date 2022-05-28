package com.postsservice.integration;

import com.google.gson.Gson;
import com.postsservice.Model.Category;
import com.postsservice.Model.Comment;
import com.postsservice.Model.Post;
import com.postsservice.Repository.CommentRepository;
import com.postsservice.Repository.PostRepository;
import com.postsservice.dto.UserDTO;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import com.postsservice.PostsServiceApplication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PostsServiceApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude=SecurityAutoConfiguration.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
//@AutoConfigureTestDatabase
public class PostRestControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @After
    public void resetDb() {
        postRepository.deleteAll();
        commentRepository.deleteAll();
    }




    @Test
    public void whenValidInput_thenCreatePost() throws IOException, Exception {
        Post post = new Post();
        post.setText("Test");
        String model = "{\"text\":\"test\"}\"";
        MultipartFile file = new MockMultipartFile("file","Test".getBytes());
        mvc.perform(post("/").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("model", model)
                )))));

        System.out.println("test123");
        List<Post> found = postRepository.findAll();
        assertThat(found).extracting(Post::getText).containsOnly("test");
    }

    @Test
    public void getAllPosts() throws IOException, Exception {
        createTestPost("test1");
        createTestPost("test2");


        UserDTO userDTO = new UserDTO();
        userDTO.setFamilyName("Manolov");
        userDTO.setFirstName("Moni");
        userDTO.setGender("male");
        userDTO.setTelephoneNumber("08958080");


        mvc.perform(get("/").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].text", is("test1")))
                .andExpect(jsonPath("$[1].text", is("test2")));
    }

    @Test
    public void deletePost() throws IOException, Exception {
        createTestPost("test1");



       mvc.perform(delete("/deletepost/{id}", postRepository.findAll().get(0).getId()).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted"));

    }

    @Test
    public void getAllPostsByCategory() throws IOException, Exception {
        Post post = new Post();
        post.setText("fly");
        post.setCategory(Category.FLY);
        postRepository.saveAndFlush(post);

        mvc.perform(get("/getbycategory/{category}", "FLY").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].text", is("fly")));
    }

    @Test
    public void addComment() throws IOException, Exception {
        Post post = new Post();
        post.setText("carp");
        post.setCategory(Category.CARP);
        postRepository.saveAndFlush(post);
        Long id  = postRepository.findAllByCategory(Category.CARP).get(0).getId();

        Comment comment = new Comment();
        comment.setText("123");

        Gson gson = new Gson();
        String json = gson.toJson(comment);

        mvc.perform(post("/addcomment/{id}", id).contentType(MediaType.APPLICATION_JSON).content(json))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("comment added"));
    }



    private void createTestPost(String text) {
        Post post = new Post();
        post.setText(text);
        postRepository.saveAndFlush(post);
    }


}
