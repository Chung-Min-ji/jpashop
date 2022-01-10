package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 양방향 연관관계
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //OrderItem 클래스의 order 가 연관관계의 주인
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delevery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING) //EnumType.ORDINARY 가 default인데, 이 경우 필드값을 숫자로 카운트 함.
                                 //필드가 추가되었을 때 오류가 생길 수 있다.
                                 //반드시 EnumType.STRING 으로 지정할 것
    private OrderStatus status; //주문상태(ORDER, CANCEL)


    //== 연관관계 편의 메서드 ==//
    // 양방향 연관관계에서 세팅.
    // 양쪽에서 세팅해야 할 값을 원자적으로 한 메서드에서 세팅할 수 있다.
    // 이 연관관계 메서드는 주로 데이터를 조작하는 쪽에 선언해주는 것이 좋다.
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }


    //== 생성 메서드 ==//
    // 주문과 관련된 관심사는 모두 이 메서드에 응집시킴.
    // 외부에서 setXXX(), setXXX(), ... 를 통해 주문을 생성하는 것이 아니라,
    // 오직 이 메서드 하나로 주문과 관련된 모든 내용을 생성함.
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
//        for(OrderItem orderItem : orderItems){
//            order.addOrderItem(orderItem);
//        }
        Arrays.stream(orderItems).forEach(order::addOrderItem);

        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }


    //== 비즈니스 로직 ==//
    /**
     * 주문 취소
     */
    public void cancel(){
        if (delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }


    //== 조회 로직 ==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice(){
//        int totalPrice = 0;
//        for (OrderItem orderItem : orderItems){
//            totalPrice += orderItem.getTotalPrice();
//        }
//        return totalPrice;
        return orderItems
                .stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }
}
