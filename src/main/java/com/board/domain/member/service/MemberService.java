package com.board.domain.member.service;

import com.board.domain.member.dto.request.MemberServiceRequest;
import com.board.domain.member.entity.Member;
import com.board.domain.member.repository.MemberRepository;
import com.board.global.common.exception.ConflictException;
import com.board.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.board.global.common.enumeration.ErrorCode.EMAIL_ALREADY_REGISTERED;
import static com.board.global.common.enumeration.ErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));
    }

    public void createMember(MemberServiceRequest request) {
        memberRepository.save(request.toEntity());
    }

    public void validateAlreadyJoinedMember(String email) {
        Optional<Member> optMember = memberRepository.findByEmailAndDeletedFalse(email);

        if (optMember.isPresent()) {
            throw new ConflictException(EMAIL_ALREADY_REGISTERED);
        }
    }

    public Member getValidMemberById(Long id) {
        return memberRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));
    }

    public Member getValidMemberByEmail(String email) {
        return memberRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));
    }

}
