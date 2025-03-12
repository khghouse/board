package com.board.service.member.response;

import com.board.domain.member.Member;

public record MemberResponse(
        Long id,
        String email
) {
    public static MemberResponse of(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail()
        );
    }
}
