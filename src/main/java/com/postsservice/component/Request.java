package com.postsservice.component;

import com.postsservice.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class Request {

    @Autowired
    private WebClient.Builder webClientBuilder;


    @Cacheable("userdetails")
    public UserDTO getUserDetailsById(Long id){
        if(id == null) return null;
        String url = String.format("http://localhost:8083/%s", id.toString());
        try {
            UserDTO user = webClientBuilder
                    .build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(UserDTO.class)
                    .block();
            return user;
        }
        catch (Exception ex){
            return null;
        }

    }
}

