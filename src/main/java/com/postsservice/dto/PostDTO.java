package com.postsservice.dto;

import com.postsservice.Model.Category;
import com.postsservice.Model.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Long id;

    private String text;
    private String image;
    private LocalDate createdOn;

    private Long userId;
    private UserDTO user;
    private Category category;

    private List<Comment> comments;

}
