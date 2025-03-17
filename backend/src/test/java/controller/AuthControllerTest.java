package controller;

import trendtrack.domain.user.*;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import trendtrack.business.AuthService;
import trendtrack.TrendTrackApplication;
import trendtrack.controller.AuthController;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import trendtrack.configuration.security.token.AccessTokenDecoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = {TrendTrackApplication.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private AccessTokenDecoder accessTokenDecoder;

    @Test
    void login_shouldReturn201_WhenRequestIsValid() throws Exception {
        //arrange
        LoginRequest loginRequest = new LoginRequest("username", "password");
        LoginResponse loginResponse = new LoginResponse("token");

        when(authService.login(loginRequest)).thenReturn(loginResponse);

        //act n assert
        mockMvc.perform(post("/tokens/login")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                    "username": "username",
                                    "password": "password"
                                }
                               """)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("username").password("password")))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                                        {"accessToken": "token"}
                                    """));

        verify(authService).login(loginRequest);
    }

    @Test
    void login_shouldReturn400_WhenRequestIsInvalid() throws Exception {
        //arrange
        String invalidRequest = """
                 {
                     "username": "",
                     "password": ""
                 }
                """;

        when(authService.login(any())).thenThrow(new IllegalArgumentException("Invalid request"));

        //act n assert
        mockMvc.perform(post("/tokens/login")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(invalidRequest)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("username").password("password")))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any());
    }
}