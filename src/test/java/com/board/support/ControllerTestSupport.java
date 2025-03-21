package com.board.support;

import com.board.api.article.ArticleController;
import com.board.api.article.ArticleLikeController;
import com.board.api.auth.AuthController;
import com.board.api.comment.CommentController;
import com.board.provider.JwtTokenProvider;
import com.board.service.article.ArticleLikeService;
import com.board.service.article.ArticleService;
import com.board.service.auth.AuthService;
import com.board.service.comment.CommentService;
import com.board.support.security.WithCustomSecurityUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        ArticleController.class,
        AuthController.class,
        CommentController.class,
        ArticleLikeController.class
})
@WithCustomSecurityUser
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
    protected CommentService commentService;

    @MockBean
    protected ArticleLikeService articleLikeService;

}
