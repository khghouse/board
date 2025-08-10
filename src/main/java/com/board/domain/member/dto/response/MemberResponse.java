package com.board.domain.member.dto.response;

import com.board.domain.member.entity.Member;

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
