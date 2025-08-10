package com.board.support;

import com.board.domain.article.controller.ArticleController;
import com.board.domain.article.controller.ArticleLikeController;
import com.board.domain.auth.controller.AuthController;
import com.board.domain.comment.controller.CommentController;
import com.board.global.security.JwtTokenProvider;
import com.board.domain.article.service.ArticleLikeService;
import com.board.domain.article.service.ArticleService;
import com.board.domain.auth.service.AuthService;
import com.board.domain.comment.service.CommentService;
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
