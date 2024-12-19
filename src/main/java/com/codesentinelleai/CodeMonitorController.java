package com.codesentinelleai;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/code-monitor")
public class CodeMonitorController {

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveWebhook(@RequestBody Map<String, Object> payload) {
        String branch = (String) payload.get("ref");
        Map<String, Object> repository = (Map<String, Object>) payload.get("repository");
        String repoName = (String) repository.get("name");
        String repoUrl = (String) repository.get("url");

        List<Map<String, Object>> commits = (List<Map<String, Object>>) payload.get("commits");
        for (Map<String, Object> commit : commits) {
            String commitId = (String) commit.get("id");
            String message = (String) commit.get("message");
            List<String> addedFiles = (List<String>) commit.get("added");
            List<String> modifiedFiles = (List<String>) commit.get("modified");
            List<String> removedFiles = (List<String>) commit.get("removed");

            System.out.println("Commit: " + commitId);
            System.out.println("Message: " + message);
            System.out.println("Added files: " + addedFiles);
            System.out.println("Modified files: " + modifiedFiles);
            System.out.println("Removed files: " + removedFiles);
        }

        return ResponseEntity.ok("Webhook processed successfully!");
    }
}
