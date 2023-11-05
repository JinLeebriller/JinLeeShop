package com.shop.jinleeshop.service;

import com.shop.jinleeshop.dto.OrderDto;
import com.shop.jinleeshop.dto.OrderHistDto;
import com.shop.jinleeshop.dto.OrderItemDto;
import com.shop.jinleeshop.entity.*;
import com.shop.jinleeshop.repository.ItemImgRepository;
import com.shop.jinleeshop.repository.ItemRepository;
import com.shop.jinleeshop.repository.MemberRepository;
import com.shop.jinleeshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private final ItemImgRepository itemImgRepository;

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

    // 주문 목록을 조회하는 로직
    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {

        // 유저의 아이디와 페이징 조건을 이용하여 주문 목록을 조회
        List<Order> orders = orderRepository.findOrders(email, pageable);
        // 유저의 주문 총 개수를 조회
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        // 주문 리스트를 순회하면서 구매 이력 페이지에 전달할 DTO를 생성
        for(Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for(OrderItem orderItem : orderItems) {
                // 주문한 상품의 대표 이미지를 조회
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtos.add(orderHistDto);
        }

        // 페이지 구현 객체를 생성하여 반환
        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

}
