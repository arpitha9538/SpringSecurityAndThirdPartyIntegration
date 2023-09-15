package org.sample.service;

import org.sample.exception.BoardsNotFoundException;
import org.sample.exception.ProfileNotFoundException;
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
import java.util.Map;

@Service
public class IntegrationService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    MondayConfig mondayConfig;

    @Autowired
    ProjectRepository projectRepository;

    /**
     * This method fetches data from Monday.com API.
     */
    public ResponseEntity<String> getBoardDetails(String query) {
        String apiUrl = "https://api.monday.com/v2";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + mondayConfig.getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> requestEntity = new HttpEntity<>(query, headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        return response;

    }

    public List<ProjectSpecs> fetchProjects() throws BoardsNotFoundException {

        String query = "{ \"query\": \"{ boards { name id } }\" }";
        ResponseEntity<String> responseEntity = getBoardDetails(query);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String responseBody = responseEntity.getBody();

            JSONObject responseJson = new JSONObject(responseBody);
            JSONArray boardsArray = responseJson.getJSONObject("data").getJSONArray("boards");

            boardsArray.toList().stream()
                    .map(item -> {
                        if (item instanceof JSONObject) {
                            return (JSONObject) item;
                        } else if (item instanceof Map<?, ?>) {
                            return new JSONObject((Map<?, ?>) item);
                        } else {
                            throw new IllegalArgumentException("Unsupported type in the array");
                        }
                    })
                    .forEach(boardJson -> {
                        ProjectSpecs project = ProjectSpecs.builder()
                                .id(boardJson.getLong("id"))
                                .name(boardJson.getString("name"))
                                .build();

                        projectRepository.save(project);
                    });
        } else {
            throw new BoardsNotFoundException("Failed to fetch the boards from the given api key " + responseEntity.getBody());
        }
        return projectRepository.findAll();
    }

    public void createBoardViaAPI(String boardName) throws  ProfileNotFoundException {

        String requestBody = String.format("{ \"query\": \"mutation { create_board (board_name: \\\"%s\\\", board_kind: public) { id } }\" }", boardName);

        ResponseEntity<String> response = getBoardDetails(requestBody);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Project name updated successfully in Monday.com");
        } else {
            throw new ProfileNotFoundException("Failed to create a board from the API " + response.getBody());
        }
    }
}
