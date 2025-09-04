package com.hyphen.controller.v1;


import com.hyphen.Issue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class IssueController {

    private static final Logger log = LogManager.getLogger(IssueController.class);

    // Smell: Hardcoded data for demonstration purposes
    // 1: In a real application, this data would come from the repository or another service
    // 2: a controller should ideally delegate to a service layer
    // ** This is acceptable for a simple demo or prototype, but not for production code **
    @GetMapping("/issues")
    public List<Issue> getIssues() {
        log.info("Fetching issues...");

        return List.of(
                new Issue(1, "Outdated dependency", "medium", "2025-08-20T10:15:00Z"),
                new Issue(2, "SQL injection risk", "high", "2025-08-21T08:03:00Z"),
                new Issue(3, "Weak TLS settings", "low", "2025-08-22T12:45:00Z")
        );
    }
}
