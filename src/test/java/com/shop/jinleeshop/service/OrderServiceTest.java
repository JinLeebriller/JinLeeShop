package com.shop.jinleeshop.service;

import com.shop.jinleeshop.constant.ItemSellStatus;
import com.shop.jinleeshop.dto.OrderDto;
import com.shop.jinleeshop.entity.Item;
import com.shop.jinleeshop.entity.Member;
import com.shop.jinleeshop.entity.Order;
import com.shop.jinleeshop.entity.OrderItem;
import com.shop.jinleeshop.repository.ItemRepository;
import com.shop.jinleeshop.repository.MemberRepository;
import com.shop.jinleeshop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    // 테스트를 위해서 주문할 상품과 회원 정보를 저장하는 메서드를 생성
    public Item saveItem() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    // 테스트를 위해서 주문할 상품과 회원 정보를 저장하는 메서드를 생성
    public Member saveMember() {
        Member member = new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("주문 테스트")
    public void order() {
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        // 주문할 상품과 상품 수량을 orderDto 객체에 세팅
        orderDto.setItemId(item.getId());
        orderDto.setCount(10);

        // 주문 로직 호출 결과 생성된 주문 번호를 orderId 변수에 저장
        Long orderId = orderService.order(orderDto, member.getEmail());

        // 주문 번호를 이용하여 저장된 주문 정보를 조회
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        List<OrderItem> orderItems = order.getOrderItems();

        // 주문한 상품의 총 가격을 구한다.
        int totalPrice = orderDto.getCount() * item.getPrice();

        // 주문한 상품의 총 가격과 데이터베이스에 저장된 상품의 가격을 비교하여 같으면 테스트가 성공적으로 종료
        assertEquals(totalPrice, order.getTotalPrice());
    }

}