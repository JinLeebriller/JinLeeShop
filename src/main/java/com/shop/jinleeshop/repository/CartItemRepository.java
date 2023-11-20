package com.shop.jinleeshop.repository;

import com.shop.jinleeshop.dto.CartDetailDto;
import com.shop.jinleeshop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 카트 아이디와 상품 아이디를 이용해서 상품이 장바구니에 들어있는지 조회
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    // 생성자를 이용하여 DTO를 반환할 때는 new 키워드와 해당 DTO의 패키지, 클래스명을 적어준다.
    // 또한 생성자의 파라미터 순서는 DTO 클래스에 명시한 순으로 넣어주어야 한다.
    @Query("select new com.shop.jinleeshop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl)" +
        "from CartItem ci, ItemImg im " +
        "join ci.item i " +
        "where ci.cart.id = :cartId " +  // :cartId는 findCartDetailDtoList메서드의 파라미터
        "and im.item.id = ci.item.id " +
        "and im.repimgYn = 'Y' " +
        "order by ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(Long cartId);

}

