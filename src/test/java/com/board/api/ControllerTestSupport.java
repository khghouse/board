package com.board.api;

import com.board.api.article.ArticleController;
import com.board.api.auth.AuthController;
import com.board.api.member.MemberController;
import com.board.provider.JwtTokenProvider;
import com.board.service.article.ArticleService;
import com.board.service.auth.AuthService;
import com.board.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        ArticleController.class,
        AuthController.class,
        MemberController.class
})
@WithMockUser
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected ArticleService articleService;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected MemberService memberService;

}
