package com.shop.jinleeshop.service;

import com.shop.jinleeshop.dto.CartDetailDto;
import com.shop.jinleeshop.dto.CartItemDto;
import com.shop.jinleeshop.entity.Cart;
import com.shop.jinleeshop.entity.CartItem;
import com.shop.jinleeshop.entity.Item;
import com.shop.jinleeshop.entity.Member;
import com.shop.jinleeshop.repository.CartItemRepository;
import com.shop.jinleeshop.repository.CartRepository;
import com.shop.jinleeshop.repository.ItemRepository;
import com.shop.jinleeshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Long addCart(CartItemDto cartItemDto, String email) {
        // 장바구니에 담을 상품 엔티티를 조회
        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        // 현재 로그인한 회원 엔티티를 조회
        Member member = memberRepository.findByEmail(email);

        // 현재 로그인한 회원의 장바구니 엔티티를 조회
        Cart cart = cartRepository.findByMemberId(member.getId());
        // 상품을 처음으로 장바구니에 담을 경우 해당 회원의 장바구니 엔티티를 생성
        if(cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        // 현재 상품이 장바구니에 이미 들어가 있는지 조회
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        // 장바구니에 이미 있던 상품일 경우 기존 수량에 현재 장바구니에 담을 수량 만큼을 더해준다.
        if(savedCartItem != null) {
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else {
            // 장바구니 엔티티, 상품 엔티티, 장바구니에 담을 수량을 이용하여 CartItem 엔티티를 생성
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            // 장바구니에 들어갈 상품을 저장
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }

    // 현재 로그인한 회원의 정보를 이용하여 장바구니에 들어있는 상품을 조회하는 로직
    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email) {

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);
        // 현재 로그인한 회원의 장바구니 엔티티를 조회
        Cart cart = cartRepository.findByMemberId(member.getId());
        // 장바구니에 상품을 한 번도 안 담았을 경우 장바구니 엔티티가 없으므로 빈 리스트를 반환
        if(cart == null) {
            return cartDetailDtoList;
        }

        // 장바구니에 담겨있는 상품 정보를 조회
        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());

        return cartDetailDtoList;
    }

}
