package com.shop.jinleeshop.service;

import com.shop.jinleeshop.entity.Member;
import com.shop.jinleeshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
/*
    비즈니스 로직을 담당하는 서비스 계층 클래스에 @Transactional 어노테이션을 선언하면
    로직을 처리하다 에러가 발생하면, 변경된 데이터 로직을 수행하기 이전 상태로 콜백시켜준다.
*/
@Transactional
@RequiredArgsConstructor
// UserDetailService 인터페이스는 데이터베이스에서 회원 정보를 가져오는 역할을 담당한다.
public class MemberService implements UserDetailsService {

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

    // 회원 정보를 조회하여 사용자의 정보와 권한을 갖는 UserDetails 인터페이스를 반환
    // User 클래스는 UserDetails 인터페이스를 구현하고 있는 클래스
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if(member == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
