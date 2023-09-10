package org.sample.service;

import org.springframework.http.*;
import org.sample.config.MondayConfig;
import org.sample.model.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class IntegrationService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    MondayConfig mondayConfig;


    public void getItemsInBoard(String boardId){
        String apiUrl = "https://api.monday.com/v2";
        String query = "{ \"query\": \"{ boards { name id } }\" }";

       // String url = apiUrl + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + mondayConfig.getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);
        //UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
          //      .queryParam("limit", 100)
            //    .queryParam("columns", "[\"notes\",\"status\"]")
              //  .queryParam("ids", "[" + boardId + "]");

        HttpEntity<?> requestEntity = new HttpEntity<>(query, headers);

        ResponseEntity response  = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        if(response.getStatusCode().is2xxSuccessful()){
            System.out.println("***** "+response.getBody());
        }else {
            throw new RuntimeException("Failed to get items");
        }



    }
}
