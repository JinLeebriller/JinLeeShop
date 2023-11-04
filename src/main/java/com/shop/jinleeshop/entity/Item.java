package com.shop.jinleeshop.entity;

import com.shop.jinleeshop.constant.ItemSellStatus;
import com.shop.jinleeshop.dto.ItemFormDto;
import com.shop.jinleeshop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    // 상품 코드
    private Long id;

    @Column(nullable = false, length = 50)
    // 상품명
    private String itemNm;

    @Column(name="price", nullable = false)
    // 가격
    private int price;

    @Column(nullable = false)
    // 재고수량
    private int stockNumber;

    @Lob
    @Column(nullable = false)
    // 상품 상세 설명
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    // 상품 판매 상태
    private ItemSellStatus itemSellStatus;

    /*
    // 등록 시간
    private LocalDateTime regTime;

    // 수정 시간
    private LocalDateTime updateTime;
    */

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    // 상품을 주문할 경우 상품의 재고를 감소시키는 로직
    // 엔티티 클래스 안에 비즈니스 로직을 메서드로 작성하면 코드의 재사용과 데이터의 변경 포인트를 한군데로 모을 수 있다는 장점이 있다.
    public void removeStock(int stockNumber) {
        // 상품의 재고 수량에서 주문 후 남은 재고 수량을 구한다.
        int restStock = this.stockNumber - stockNumber;
        // 상품의 재고가 주문 수량보다 작을 경우 재고 부족 예외를 발생시킨다.
        if(restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        // 주문 후 남은 재고 수량을 상품의 현재 재고 값으로 할당
        this.stockNumber = restStock;
    }

}
