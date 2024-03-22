package com.board.service.member;

import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.exception.BusinessException;
import com.board.provider.JwtTokenProvider;
import com.board.service.member.request.MemberServiceRequest;
import com.board.service.member.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public MemberResponse postMember(MemberServiceRequest request) {
        validateJoinedMemberByEmail(request.getEmail());

        Member member = memberRepository.save(request.toEntity());
        return MemberResponse.of(member, jwtTokenProvider.createAccessToken(member.getId()));
    }

    private void validateJoinedMemberByEmail(String email) {
        Optional<Member> optMember = memberRepository.findByEmailAndDeletedFalse(email);

        if (optMember.isPresent()) {
            throw new BusinessException("이미 가입된 이메일입니다.");
        }
    }

}
