package com.shop.jinleeshop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // @OneToOne 어노테이션을 이용해 Member 엔티티와 일대일로 매핑
    /*
        엔티티를 조회할 때 해당 엔티티와 매핑된 엔티티도 한 번에 조회하는 것을 '즉시 로딩'이라고 한다.
        일대일(@OneToOne), 다대일(@ManyToOne)로 매핑할 경우 즉시 로딩을 기본 Fetch 전략으로 설정된다.
        아래 어노테이션에 속성을 더하지 않으면 @OneToOne(fetch = FetchType.EAGER(즉시 로딩)) 설정과 동일하다.
    */
    @OneToOne(fetch = FetchType.LAZY)
    // @JoinColumn 어노테이션을 이용해 매핑할 외래키를 지정
    @JoinColumn(name = "member_id")
    private Member member;

}
