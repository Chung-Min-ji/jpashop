package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String name;

    @Embedded //내장타입
    private Address address;

    @OneToMany(mappedBy = "member") //order 입장에서는 ManyToOne
    // mappedBy : 연관관계의 거울 (Order 클래스의 member 필드로 맵핑됨)
    //            즉, 읽기전용. (등록, 수정은 연관관계의 주인만 가능)
    private List<Order> orders = new ArrayList<>();

}
