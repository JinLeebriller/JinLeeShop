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

}
