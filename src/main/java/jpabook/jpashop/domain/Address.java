package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

// 값 타입(Embeddable)은 기본적으로 immutable 해야 함.
// 생성할 때만 값이 세팅(=초기화)되고, Setter 는 제공하지 않는다.(따라서 AllArgsConstructor 필요)
@Embeddable
@AllArgsConstructor
// JPA 는 객체를 생성할 떄 리플렉션, 프록시 등의 기술을 사용해야 하므로 Default Constructor 반드시 필요함.
// JPA 스펙에서는 public 또는 protected 까지 허용하지만,
// public 이면 여러 곳에서 생성될 수 있으므로 protected 로 설정하는 것이 더 안전하다.
// (default constructor 없으면 컴파일 에러)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;
}
