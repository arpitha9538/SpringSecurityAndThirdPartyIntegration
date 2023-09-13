package org.sample.service;

import org.springframework.http.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.sample.config.MondayConfig;
import org.sample.entities.ProjectSpecs;
import org.sample.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class IntegrationService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    MondayConfig mondayConfig;

    @Autowired
    ProjectRepository projectRepository;

    /**
    *  This method fetches data from Monday.com API.
    * 
    */
    public ResponseEntity<String> getBoardDetails(String query){
        String apiUrl = "https://api.monday.com/v2";
         
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + mondayConfig.getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> requestEntity = new HttpEntity<>(query, headers);

        ResponseEntity<String> response  = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

       return response;

    }

     public void fetchProjects() {

        String query = "{ \"query\": \"{ boards { name id } }\" }";
        ResponseEntity<String> responseEntity = getBoardDetails(query);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String responseBody = responseEntity.getBody();

            JSONObject responseJson = new JSONObject(responseBody);
            JSONArray boardsArray = responseJson.getJSONObject("data").getJSONArray("boards");

            boardsArray.toList().stream()
            .map(JSONObject.class::cast)
            .forEach(boardJson -> {
                ProjectSpecs project = ProjectSpecs.builder()
                .id(boardJson.getLong("id"))
                .name(boardJson.getString("name"))
                .build();

                projectRepository.save(project);
            });
             List<ProjectSpecs> res = projectRepository.findAll();
                System.out.println(res);
        }
    }

    public void updateProjectName(String newName) {

        String BOARD_ID = "YOUR_BOARD_ID";
        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Create the request body with the new project name
        String requestBody = "{\"query\":\"mutation { change_column_value (board_id: " + BOARD_ID + ", item_id: " + ITEM_ID + ", column_id: \"name\", value: \\\"" + newName + "\\\"\") { id } }\"}";
        ResponseEntity<String> response = getBoardDetails(requestBody);

        // Handle the response (you can log or process it as needed)
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Project name updated successfully in Monday.com");
        } else {
            System.err.println("Failed to update project name in Monday.com. Response: " + response.getBody());
        }
    }
}
