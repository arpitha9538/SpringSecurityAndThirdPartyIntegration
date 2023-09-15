package org.sample.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.sample.config.SecurityConfig;
import org.sample.controllers.AuthController;
import org.sample.controllers.MondayController;
import org.sample.repository.ProjectRepository;
import org.sample.service.IntegrationService;
import org.sample.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest({ AuthController.class })
@Import({ SecurityConfig.class, TokenService.class })
class AuthControllerTest {

    @MockBean
    MondayController mondayController;

    @MockBean
    ProjectRepository projectRepository;

    @InjectMocks
    IntegrationService integrationService;

    @Autowired
    MockMvc mvc;

    @Test
    void tokenWhenAnonymousThenStatusIsUnauthorized() throws Exception {
        this.mvc.perform(post("/token")).andExpect(status().isUnauthorized());
    }

    @Test
    void tokenWithBasicThenGetToken() throws Exception {
        MvcResult result = this.mvc.perform(post("/token").with(httpBasic("arpitha", "password"))).andExpect(status().isOk()).andReturn();

        assertThat(result.getResponse().getContentAsString()).isNotEmpty();
    }

    @Test
    void rootWhenUnauthenticatedThen401() throws Exception {
        this.mvc.perform(get("/")).andExpect(status().isUnauthorized());
    }

    @Test
    public void rootWithBasicStatusIsUnauthorized() throws Exception {
        this.mvc.perform(get("/").with(httpBasic("arpitha", "password"))).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void rootWithMockUserStatusIsOK() throws Exception {
        this.mvc.perform(get("/")).andExpect(status().isOk());
    }

}