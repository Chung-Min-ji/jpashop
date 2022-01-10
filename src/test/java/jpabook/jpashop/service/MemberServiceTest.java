package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional //테스트 클래스에 @Transactional 어노테이션 붙이면 자동 Rollback
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
//    @Rollback(false)
    public void 회원가입() throws Exception{
        Member member = new Member();
        member.setName("kim");

        Long savedId = memberService.join(member);

        em.flush(); //flush(): persist context 에 있는 내용을 db에 반영. insert 쿼리 조회할 수 있다. (결과는 rollback 되지만 쿼리 확인하기 위해..)
        assertEquals(member, memberRepository.findOne(savedId));

    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예약() throws Exception{
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        memberService.join(member1);
        memberService.join(member2); //예외 발생해야 함.

        fail("예외가 발생해야 한다.");
    }

}