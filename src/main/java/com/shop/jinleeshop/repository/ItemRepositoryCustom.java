package com.shop.jinleeshop.repository;

import com.shop.jinleeshop.dto.ItemSearchDto;
import com.shop.jinleeshop.dto.MainItemDto;
import com.shop.jinleeshop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    /*
        상품 조회 조건을 담고 있는 itemSearchDto 객체와
        페이징 정보를 담고 있는 pageable 객체를 파라미터로 받는 getAdminItemPage 메서드를 정의
        반환 데이터로 Page<Item> 객체를 반환
    */
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    // 메인 페이지에 보여줄 상품 리스트를 가져오는 메서드
    Page<MainItemDto> getMainITtemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}
