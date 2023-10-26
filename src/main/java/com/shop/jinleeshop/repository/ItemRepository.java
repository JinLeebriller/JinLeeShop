package com.shop.jinleeshop.repository;

import com.shop.jinleeshop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

/*
    QuerydslPredicateExcutor : Repository에 Predicate를 파라미터로 전달하기 위해 이 인터페이스를 상속
                               Predicate란 '이 조건이 맞다'고 판단한 근거를 함수로 제공
                               Predicate를 구현하고 있는 BooleanBuilder로 쿼리에 들어갈 조건을 만들어 준다.
    - long count(Predicate) : 조건에 맞는 데이터의 총 개수 반환
    - boolean exists(Predicate) : 조건에 맞는 데이터 존재 여부 반환
    - Iterable findAll(Predicate) : 조건에 맞는 모든 데이터 반환
    - Page<T> findAll(Predicate, Pageable) : 조건에 맞는 페이지 데이터 반환
    - Iterable findAll(Predicate, Sort) : 조건에 맞는 정렬된 데이터 반환
    - T findOne(Predicate) : 조건에 맞는 데이터 1개 반환

     사용자 정의로 만든 ItemRepositoryCustom 인터페이스를 상속해서
     ItemRepository에서 Querydsl로 구현한 상품 관리 페이지 목록을 불러오는 getAdminItemPage() 메서드를 사용한다.
*/
public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {

    List<Item> findByItemNm(String itemNm);

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    // Spring Data JPA에서 제공하는 @Query 어노테이션을 이용하면 SQL과 유사한 JPQL이라는 개체지향 쿼리 언어를 통해
    // 복잡한 쿼리도 처리가 가능하다.
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    @Query(value="select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

}
