package com.shop.jinleeshop.service;

import com.shop.jinleeshop.dto.OrderDto;
import com.shop.jinleeshop.entity.Item;
import com.shop.jinleeshop.entity.Member;
import com.shop.jinleeshop.entity.Order;
import com.shop.jinleeshop.entity.OrderItem;
import com.shop.jinleeshop.repository.ItemRepository;
import com.shop.jinleeshop.repository.MemberRepository;
import com.shop.jinleeshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
/*
    비즈니스 로직을 담당하는 서비스 계층 클래스에 @Transactional 어노테이션을 선언하면
    로직을 처리하다 에러가 발생하면, 변경된 데이터 로직을 수행하기 이전 상태로 콜백시켜준다.
*/
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    public Long order(OrderDto orderDto, String email) {
        // 주문할 상품을 조회
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        // 현재 로그인한 회원의 이메일 정보를 이용해서 회원 정보를 조회
        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();
        // 주문할 상품 엔티티와 주문 수량을 이용하여 주문 상품 엔티티를 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        // 회원 정보와 주문할 상품 리스트 정보를 이용하여 주문 엔티티를 생성
        Order order = Order.createOrder(member, orderItemList);
        // 생성한 주문 엔티티 정보를 저장
        orderRepository.save(order);

        return order.getId();
    }

}
