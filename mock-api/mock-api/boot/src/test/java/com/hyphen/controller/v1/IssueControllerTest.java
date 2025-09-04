// boot/src/test/java/com/hyphen/boot/api/IssueControllerTest.java
package com.hyphen.controller.v1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class IssueControllerTest {
    @Autowired
    MockMvc mvc;

    @Test
    void returnsIssues() throws Exception {
        mvc.perform(get("/api/issues")).andExpect(status().isOk());
    }
}
