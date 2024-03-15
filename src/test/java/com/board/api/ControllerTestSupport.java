package com.board.api;

import com.board.api.article.ArticleController;
import com.board.api.auth.AuthController;
import com.board.provider.JwtTokenProvider;
import com.board.service.article.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        ArticleController.class,
        AuthController.class
})
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected ArticleService articleService;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

}
