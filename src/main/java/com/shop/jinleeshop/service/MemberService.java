package com.shop.jinleeshop.service;

import com.shop.jinleeshop.entity.Member;
import com.shop.jinleeshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
/*
    비즈니스 로직을 담당하는 서비스 계층 클래스에 @Transactional 어노테이션을 선언하면
    로직을 처리하다 에러가 발생하면, 변경된 데이터 로직을 수행하기 이전 상태로 콜백시켜준다.
*/
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

}
