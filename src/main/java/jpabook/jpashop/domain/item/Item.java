package jpabook.jpashop.domain.item;

import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속관계 매핑
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //== 비즈니스 로직 ==//
    // 아래 로직은 stockQuantity 와 관련된 로직들.
    // stockQuantity 는 Item 엔티티가 가지고 있는 '상태' 이므로,
    // 이 '상태' 를 변경시키는 '행동' = 즉, 메서드는 엔티티 스스로 가지고 있는 것이 객체지향 관점에서 적절함.
    // (setStockQuantity() 로 외부에서 값을 계산해서 넣는것은 객체지향 관점에 맞지 않다)
    // -> **도메인 모델 패턴**
    /**
     * stock 증가
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity){
        int restStock = this.stockQuantity -= quantity;
        if(restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
