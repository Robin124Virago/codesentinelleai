package com.codesentinelleai;

import com.codesentinelleai.entities.CommitDetails;
import com.codesentinelleai.repositories.CommitDetailsRepository;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/code-monitor")
@Slf4j
public class CodeMonitorController {

    private final CommitDetailsRepository repository;

    // Constructor injection
    public CodeMonitorController(CommitDetailsRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveWebhook(@RequestBody Map<String, Object> payload) {
        log.info("Payload received: {}", payload);

        // Procesare și salvare
        List<Map<String, Object>> commits = (List<Map<String, Object>>) payload.get("commits");
        if (commits == null || commits.isEmpty()) {
            log.warn("No commits found in payload.");
            return ResponseEntity.ok("No commits found in payload.");
        }

        for (Map<String, Object> commit : commits) {
            try {
                CommitDetails details = processCommit(payload, commit);
                repository.save(details);

                // Analiza statică a fișierelor modificate
                List<String> modifiedFiles = details.getModifiedFiles();
                if (modifiedFiles != null && !modifiedFiles.isEmpty()) {
                    analyzeModifiedFiles(modifiedFiles);
                }
            } catch (Exception e) {
                log.error("Error processing commit: {}", commit, e);
            }
        }

        return ResponseEntity.ok("Webhook processed and saved to database!");
    }

    @GetMapping("/commits")
    public ResponseEntity<List<CommitDetails>> getAllCommits() {
        List<CommitDetails> commits = repository.findAll();
        return ResponseEntity.ok(commits);
    }

    private CommitDetails processCommit(Map<String, Object> payload, Map<String, Object> commit) {
        CommitDetails details = new CommitDetails();
        details.setBranch((String) payload.get("ref"));
        Map<String, Object> repositoryInfo = (Map<String, Object>) payload.get("repository");
        details.setRepoName((String) repositoryInfo.get("name"));
        details.setCommitId((String) commit.get("id"));
        details.setMessage((String) commit.get("message"));
        details.setAddedFiles((List<String>) commit.get("added"));
        details.setModifiedFiles((List<String>) commit.get("modified"));
        details.setRemovedFiles((List<String>) commit.get("removed"));

        log.info("Processed commit ID: {}", details.getCommitId());
        return details;
    }

    private void analyzeModifiedFiles(List<String> modifiedFiles) {
        for (String file : modifiedFiles) {
            log.info("Running static analysis on file: {}", file);
            runCheckstyleAnalysis(file);
        }
    }

    private void runCheckstyleAnalysis(String filePath) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("sh", "-c", "./gradlew check");

            // Setează directorul de lucru la proiect
            processBuilder.directory(new File(System.getProperty("user.dir")));
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                log.info("Checkstyle analysis passed for file: {}", filePath);
            } else {
                log.warn("Checkstyle analysis failed for file: {}", filePath);
            }
        } catch (Exception e) {
            log.error("Error running Checkstyle analysis on file: {}", filePath, e);
        }
    }
}
