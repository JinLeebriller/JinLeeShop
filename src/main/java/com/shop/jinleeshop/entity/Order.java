package com.shop.jinleeshop.entity;

import com.shop.jinleeshop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // 한 명의 Member는 여러 번 주문할 수 있으므로 Order 엔티티 기준에서 다대일 단방향 매핑을 한다.
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // 주문일
    private LocalDateTime orderDate;

    // 주문상태
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    /*
        OrderItem 엔티티와 일대다 매핑
        외래키(order_id)가 order_item 테이블에 있으므로 연관 관계의 주인은 OrderItem 엔티티.
        Order 엔티티가 주인이 아니므로 mappedBy 속성으로 연관 관계의 주인을 설정해야 한다.
        속성의 값으로 "order"를 적어준 이유는 OrderItem에 있는 Order에 의해 관리된다는 의미이다.
        즉, 연관 관계의 주인의 필드인 order를 mappedBy의 값으로 세팅

        cascade(영속성 전이) 속성 : 부모 엔티티(One)의 영속성 상태 변화를 자식 엔티티(Many)에 모두 전이하는 CasecadeTypeAll 옵션 설정

        orphanRemoval : 부모 엔티티와 연관 관계가 끊어진 자식 엔티티를 제거
    */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    // 하나의 Order가 여러 개의 OrderItem을 가지므로 List 자료형을 사용해서 매핑
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

}
