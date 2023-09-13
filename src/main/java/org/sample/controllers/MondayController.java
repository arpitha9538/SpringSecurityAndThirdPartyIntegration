package org.sample.controllers;
import java.util.Map;

import org.sample.service.IntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/projects")
public class MondayController {

    @Autowired
    IntegrationService integrationService;

@GetMapping
    public void getAllProjects(){
        integrationService.fetchProjects();
}

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload) {
        // Extract project ID and updated name from the payload received from Monday.com webhook
        Long projectId = Long.parseLong(payload.get("projectId").toString());
        String updatedName = payload.get("updatedName").toString();

        // Update the project name in your Java application's data store
       // projectMap.put(projectId, updatedName);

        return ResponseEntity.ok("Project name updated successfully.");
    }
}
