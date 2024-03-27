package com.board.api.member;

import com.board.api.ApiResponse;
import com.board.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @Deprecated
    @GetMapping("/user")
    public ApiResponse getUser() {
        return ApiResponse.ok("사용자 페이지입니다.");
    }

    @Deprecated
    @GetMapping("/admin")
    public ApiResponse getAdmin() {
        return ApiResponse.ok("어드민 페이지입니다.");
    }
    
}
