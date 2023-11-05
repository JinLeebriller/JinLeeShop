package com.shop.jinleeshop.entity;

import com.shop.jinleeshop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class OrderItem extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    // 하나의 Item이 여러 OrderItem으로 들어갈 수 있으므로 OrderItem 기준으로 다대일 단방향 매핑
    // 지연 로딩 방식 사용을 위해 (fetch = FetchType.LAZY) 추가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // 한 번의 Order에 여러 개의 OrderItem을 주문할 수 있으므로 OrderItem 엔티티와 Order 엔티티를 다대일 단방향 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    // 주문 가격
    private int orderPrice;

    // 수량
    private int count;

    /*
    private LocalDateTime regTime;

    private LocalDateTime updateTime;
    */

    // 주문할 상품과 주문 수량을 통해 OrderItem 객체를 만드는 메서드
    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();
        // 주문할 상품과 주문 수량을 세팅
        orderItem.setItem(item);
        orderItem.setCount(count);
        // 현재 시간 기준으로 상품 가격을 주문 가격으로 세팅(상품 가격은 시간, 쿠폰, 할인 적용에 따라 달라질 수 있지만 여기선 고려 X)
        orderItem.setOrderPrice(item.getPrice());
        // 주문 수량만큼 상품의 재고 수량을 감소
        item.removeStock(count);
        return orderItem;
    }

    // 주문 가격과 주문 수량을 곱해서 해당 상품을 주문한 총 가격을 계산하는 메서드
    public int getTotalPrice(){
        return orderPrice * count;
    }

    // 주문 취소 시 주문 수량만큼 상품의 재고를 더해주는 메서드
    public void cancel() {
        this.getItem().addStock(count);
    }

}
