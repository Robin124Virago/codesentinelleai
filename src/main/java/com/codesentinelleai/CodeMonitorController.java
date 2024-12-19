package com.codesentinelleai;

import com.codesentinelleai.entities.CommitDetails;
import com.codesentinelleai.repositories.CommitDetailsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/code-monitor")
public class CodeMonitorController {

    private final CommitDetailsRepository repository;

    // Constructor injection
    public CodeMonitorController(CommitDetailsRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveWebhook(@RequestBody Map<String, Object> payload) {
        System.out.println("Payload received: " + payload);

        // Procesare și salvare
        List<Map<String, Object>> commits = (List<Map<String, Object>>) payload.get("commits");
        if (commits == null || commits.isEmpty()) {
            return ResponseEntity.ok("No commits found in payload.");
        }

        for (Map<String, Object> commit : commits) {
            CommitDetails details = new CommitDetails();
            details.setBranch((String) payload.get("ref"));
            Map<String, Object> repositoryInfo = (Map<String, Object>) payload.get("repository");
            details.setRepoName((String) repositoryInfo.get("name"));
            details.setCommitId((String) commit.get("id"));
            details.setMessage((String) commit.get("message"));
            details.setAddedFiles((List<String>) commit.get("added"));
            details.setModifiedFiles((List<String>) commit.get("modified"));
            details.setRemovedFiles((List<String>) commit.get("removed"));

            // Salvează în baza de date
            repository.save(details);
        }

        return ResponseEntity.ok("Webhook processed and saved to database!");
    }
}

