package com.shop.jinleeshop.dto;

import com.shop.jinleeshop.constant.ItemSellStatus;
import com.shop.jinleeshop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상품설명은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    // 상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    /*
        상품의 이미지 아이디를 저장하는 리스트.
        상품 등록 시에는 아직 상품의 이미지를 저장하지 않았기 떄문에 아무 값도 들어가 있찌 않고
        수정 시에 이미지 아이디를 담아둘 용도로 사용
    */
    private List<Long> itemImgIds = new ArrayList<>();

    // modelMapper를 이용하여 엔티티 객체와 DTO 객체 간의 데이터를 복사하여 복사한 객체를 반환
    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem() {
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item) {
        return modelMapper.map(item, ItemFormDto.class);
    }

}
