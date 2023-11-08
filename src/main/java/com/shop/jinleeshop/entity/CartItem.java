package com.shop.jinleeshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "cart_item")
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column
    private Long id;

    /*
        하나의 Cart에는 여러 개의 Item을 담을 수 있으므로 다대일 관계(@ManyToOne)로 매핑
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    /*
        하나의 Item은 여러 Cart의 상품으로 담길 수 있으므로 다대일 관계(@ManyToOne)로 매핑
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

    // 장바구니에 담을 상품 엔티티를 생성하는 메서드
    public static CartItem createCartItem(Cart cart, Item item, int count) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }

    /*
        장바구니에 기존에 담겨 있는 상품인데,
        해당 상품을 추가로 장바구니에 담을 때 기존 수량에 현재 담을 수량을 더해줄 때 사용할 메서드
    */
    public void addCount(int count) {
        this.count += count;
    }

}
