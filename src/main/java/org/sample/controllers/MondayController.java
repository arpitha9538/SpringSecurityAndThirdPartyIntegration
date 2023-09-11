package org.sample.controllers;
import org.sample.model.Project;
import org.sample.service.IntegrationService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/projects")
public class MondayController {

    @Autowired
    IntegrationService integrationService;

@GetMapping
    public void getAllProjects(){
integrationService.saveBoardsToDatabase();
}

}
