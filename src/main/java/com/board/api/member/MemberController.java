package com.board.api.member;

import com.board.annotation.NoAuth;
import com.board.api.ApiResponse;
import com.board.api.member.request.MemberRequest;
import com.board.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @NoAuth
    @PostMapping
    public ApiResponse postMember(@Validated @RequestBody MemberRequest request) {
        return ApiResponse.ok(memberService.postMember(request.toServiceRequest()));
    }

}
