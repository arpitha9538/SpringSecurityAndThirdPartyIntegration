package org.sample.controllers;

import java.util.List;

import org.sample.entities.ProjectSpecs;
import org.sample.exception.BoardsNotFoundException;
import org.sample.exception.ProfileNotFoundException;
import org.sample.exception.ProjectNotFoundException;
import org.sample.model.VerificationRequestResponse;
import org.sample.service.IntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/projects")
public class MondayController {

    @Autowired
    IntegrationService integrationService;

    /**
     * This method will fetch all the projects from Monday.com profile.
     * @throws BoardsNotFoundException
     */
    @GetMapping
    public List<ProjectSpecs> getAllProjects() throws BoardsNotFoundException {

        return integrationService.fetchProjects();
    }

    /**
     *This method will handle the initial test challenge sent from Monday.com
     * to create a webhook. Once it is verified, same endpoint can be used to update the
     * details sent by Monday.com about any changes has been done on the user's board.
     */
    @PostMapping("/webhookVerification")
    public ResponseEntity<Object> handleWebhook(@RequestBody VerificationRequestResponse verificationRequest) {
        // Check if the request contains a "challenge" field
        if (verificationRequest != null && verificationRequest.getChallenge() != null) {
            return ResponseEntity.ok(new VerificationRequestResponse(verificationRequest.getChallenge()));
        }
        return (ResponseEntity<Object>) ResponseEntity.badRequest();
    }

    /**
     * This method will create a new project/board on Monday.com.
     */
    @PostMapping("/createBoard")
    public ResponseEntity<String> createBoard(@RequestParam String boardName) {
        try {
            integrationService.createBoardViaAPI(boardName);
            return ResponseEntity.ok("Board creation request submitted successfully.");
        } catch (ProfileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
