package com.shop.jinleeshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue
    @Column
    private Long id;

    /*
        하나의 Cart에는 여러 개의 Item을 담을 수 있으므로 다대일 관계(@ManyToOne)로 매핑
    */
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    /*
        하나의 Item은 여러 Cart의 상품으로 담길 수 있으므로 다대일 관계(@ManyToOne)로 매핑
    */
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

}
