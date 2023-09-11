package org.sample.service;

import org.springframework.http.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.sample.config.MondayConfig;
import org.sample.entities.ProjectSpecs;
import org.sample.model.Items;
import org.sample.model.Project;
import org.sample.repository.ProjectRepository;
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

    @Autowired
    ProjectRepository projectRepository;


    public ResponseEntity<String> getBoards(){
        String apiUrl = "https://api.monday.com/v2";
        String query = "{ \"query\": \"{ boards { name id } }\" }";

       // String url = apiUrl + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + mondayConfig.getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity<?> requestEntity = new HttpEntity<>(query, headers);

        ResponseEntity<String> response  = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

       return response;

    }

     public void saveBoardsToDatabase() {
        ResponseEntity<String> responseEntity = getBoards(); // Assuming getBoards method fetches data from Monday.com API
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String responseBody = responseEntity.getBody();

            // Parse the JSON response
            JSONObject responseJson = new JSONObject(responseBody);
            JSONArray boardsArray = responseJson.getJSONObject("data").getJSONArray("boards");

            for (int i = 0; i < boardsArray.length(); i++) {
                JSONObject boardJson = boardsArray.getJSONObject(i);
                Long boardId = boardJson.getLong("id");
                String boardName = boardJson.getString("name");

                // Create a Project entity and save it to the database
                ProjectSpecs project = new ProjectSpecs();
                project.setId(boardId);
                project.setName(boardName);

                projectRepository.save(project);

                List<ProjectSpecs> res = projectRepository.findAll();
                System.out.println(res);
            }
        }
    }
}
