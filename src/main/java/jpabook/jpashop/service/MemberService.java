package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /*
    * 회원가입
    **/
    @Transactional(readOnly = false) // 쓰기전용이므로 readOnly=false 지정. 단, false 가 default 이므로 생략 가능하다. (클래스레벨에서 readOnly=true 지정했음)
    public Long join(Member member){
        validateDuplicateMember(member); //중복회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    /*
    * 중복 회원 검증 메서드
    **/
    public void validateDuplicateMember(Member member){
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

    }

    /*
    * 회원 전체 조회
    * */
//    @Transactional(readOnly = true) //읽기 전용의 경우 readOnly옵션을 넣어주는 것이 성능상 더 좋음 (클래스 레벨에서 어노테이션 선언했으므로 생략)
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    /*
    * 회원 한 명 조회
    * */
//    @Transactional(readOnly = true)
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
