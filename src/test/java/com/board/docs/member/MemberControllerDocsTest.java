package com.board.docs.member;

import com.board.api.member.MemberController;
import com.board.docs.RestDocsSupport;
import com.board.service.member.MemberService;

import static org.mockito.Mockito.mock;

public class MemberControllerDocsTest extends RestDocsSupport {

    private final MemberService memberService = mock(MemberService.class);

    @Override
    protected Object initController() {
        return new MemberController(memberService);
    }

}
