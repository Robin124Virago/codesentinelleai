package com.codesentinelleai.controllers;

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

    public CodeMonitorController(CommitDetailsRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveWebhook(@RequestBody Map<String, Object> payload) {
        log.info("Payload received: {}", payload);

        final List<Map<String, Object>> commits = (List<Map<String, Object>>) payload.get("commits");
        if (commits == null || commits.isEmpty()) {
            log.warn("No commits found in payload.");
            return ResponseEntity.ok("No commits found in payload.");
        }

        for (final Map<String, Object> commit : commits) {
            try {
                final CommitDetails details = processCommit(payload, commit);
                repository.save(details);

                final List<String> modifiedFiles = details.getModifiedFiles();
                if (modifiedFiles != null && !modifiedFiles.isEmpty()) {
                    analyzeModifiedFiles(modifiedFiles);
                }
            } catch (final Exception e) {
                log.error("Error processing commit: {}", commit, e);
            }
        }

        return ResponseEntity.ok("Webhook processed and saved to database!");
    }

    @GetMapping("/commits")
    public ResponseEntity<List<CommitDetails>> getAllCommits() {
        final List<CommitDetails> commits = repository.findAll();
        return ResponseEntity.ok(commits);
    }

    private CommitDetails processCommit(Map<String, Object> payload, Map<String, Object> commit) {
        final CommitDetails details = new CommitDetails();
        details.setBranch((String) payload.get("ref"));
        final Map<String, Object> repositoryInfo = (Map<String, Object>) payload.get("repository");
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

            scanForCriticalChanges(modifiedFiles);
        }
    }


    private boolean runCheckstyleAnalysis(String filePath) {
        try {
            final ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("sh", "-c", "./gradlew check");
            processBuilder.directory(new File(System.getProperty("user.dir")));
            processBuilder.redirectErrorStream(true);

            final Process process = processBuilder.start();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            final StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
                output.append(line).append("\n");
            }

            final int exitCode = process.waitFor();
            if (exitCode == 0) {
                log.info("Checkstyle analysis passed for file: {}", filePath);
                return true;
            } else {
                log.warn("Checkstyle analysis failed for file: {}", filePath);
                log.warn("Checkstyle Output: \n{}", output);
                return false;
            }
        } catch (final Exception e) {
            log.error("Error running Checkstyle analysis on file: {}", filePath, e);
            return false;
        }
    }

    private void scanForCriticalChanges(List<String> modifiedFiles) {
        final List<String> sensitiveKeywords = List.of("password", "API_KEY", "secret", "token");
        for (String file : modifiedFiles) {
            log.info("Scanning file for critical changes: {}", file);

            try {
                final File targetFile = new File(file);
                if (!targetFile.exists()) {
                    log.warn("File does not exist: {}", file);
                    continue;
                }

                try (BufferedReader reader = new BufferedReader(new java.io.FileReader(targetFile))) {
                    String line;
                    int lineNumber = 0;
                    while ((line = reader.readLine()) != null) {
                        lineNumber++;
                        for (String keyword : sensitiveKeywords) {
                            if (line.contains(keyword)) {
                                log.warn("Critical keyword '{}' found in file '{}' at line {}!", keyword, file, lineNumber);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Error scanning file for critical changes: {}", file, e);
            }
        }
    }


}
