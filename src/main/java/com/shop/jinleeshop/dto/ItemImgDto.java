package com.shop.jinleeshop.dto;

import com.shop.jinleeshop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    // dependency로 추가한 ModelMapper 객체를 멤버 변수로 추가한다.
    private static ModelMapper modelMapper = new ModelMapper();

    /*
        ItemImg 엔티티 객체를 파라미터로 받아서 ItemImg 객체의 자료형과 멤버변수의 이름이 같을 때
        ItemImgDto로 값을 복사해서 반환한다.
        static 메서드로 선언해 ItemImgDto 객체를 생성하지 않아도 호출할 수 있도록 하였다.
    */
    public static ItemImgDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg, ItemImgDto.class);
    }

}
