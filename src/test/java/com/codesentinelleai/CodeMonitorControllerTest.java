package com.codesentinelleai;

import com.codesentinelleai.controllers.CodeMonitorController;
//import com.codesentinelleai.entities.CommitDetails;
import com.codesentinelleai.repositories.CommitDetailsRepository;
//import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;

public class CodeMonitorControllerTest {

    private final CommitDetailsRepository repository = Mockito.mock(CommitDetailsRepository.class);
    private final CodeMonitorController controller = new CodeMonitorController(repository);

//    @Test
//    void testProcessCommit() {
//        // Mock payload
//        final Map<String, Object> payload = Map.of(
//            "ref", "refs/heads/main",
//            "repository", Map.of("name", "codesentinelleai"),
//            "commits", List.of(
//                Map.of(
//                    "id", "123abc",
//                    "message", "Test commit",
//                    "added", List.of("file1.java"),
//                    "modified", List.of("file2.java"),
//                    "removed", List.of("file3.java")
//                )
//            )
//        );
//
//        // Mock repository save
//        Mockito.when(repository.save(any(CommitDetails.class))).thenReturn(null);
//
//        // Execute
//        final ResponseEntity<String> response = controller.receiveWebhook(payload);
//
//        // Assert
//        assertEquals("Webhook processed and saved to database!", response.getBody());
//        Mockito.verify(repository, Mockito.times(1)).save(any(CommitDetails.class));
//    }
}
