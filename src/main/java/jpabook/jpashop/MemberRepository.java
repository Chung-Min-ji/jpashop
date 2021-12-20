package jpabook.jpashop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Member member){
        em.persist(member);
        // save는 말 그대로 save가 목적. return은 없이 설계하는 것이 SOP관점에서 좋은 설계.
        // 단, id정도는 return해주 면 조회 등에 쓰기 좋으므로 id만 return해줌. (member 자체를 리턴하지 않는다)
        return member.getId();
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }
}
