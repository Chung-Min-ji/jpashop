package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter

// 기본 생성자 + setXXX() 로 생성하지 못하도록, 기본생성자 protected 로 설정.
// (객체생성은 오직 createOrderItem() 으로만 할 것!)
// JPA 스펙상, constructor 는 protected, public 만 허용하는데
// protected 는 곧 외부에서 기본 생성자를 사용하지 말라는 제약임.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    private int orderPrice; //주문가격
    private int count; //주문수량


    //== 생성 메서드 ==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }


    //== 비즈니스 로직 ==//
    /**
     * 주문 취소
     */
    public void cancel() {
        getItem().addStock(count); //재고수량 원복
    }

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
