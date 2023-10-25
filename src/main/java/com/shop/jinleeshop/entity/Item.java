package com.shop.jinleeshop.entity;

import com.shop.jinleeshop.constant.ItemSellStatus;
import com.shop.jinleeshop.dto.ItemFormDto;
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

}
