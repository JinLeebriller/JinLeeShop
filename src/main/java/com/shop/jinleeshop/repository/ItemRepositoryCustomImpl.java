package com.shop.jinleeshop.repository;

import com.querydsl.core.QueryResults;
import com.shop.jinleeshop.dto.MainItemDto;
import com.shop.jinleeshop.dto.QMainItemDto;
import com.shop.jinleeshop.entity.QItemImg;
import org.springframework.data.domain.PageImpl;
import org.thymeleaf.util.StringUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.jinleeshop.constant.ItemSellStatus;
import com.shop.jinleeshop.dto.ItemSearchDto;
import com.shop.jinleeshop.entity.Item;
import com.shop.jinleeshop.entity.QItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

/*
    ItemRepositoryCustom 인터페이스를 구현하는 ItemRepositoryCustomImpl 클래스
    주의할 점으로는 클래스명 끝에 "Impl"을 붙여주어야 정상적으로 동작

    Querydsl에서는 BooleanExpression이라는 where절에서 사용할 수 있는 값을 지원
*/
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    // 동적으로 쿼리를 생성하기 위해서 JPAQueryFactory 클래스를 사용
    private JPAQueryFactory queryFactory;

    // JPAQueryFactory의 생성자로 EntityManager 객체를 넣어준다.
    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 상품 판매 상태 조건이 전체(null)일 경우는 null을 리턴. 리턴값이 null이면 where절에서 해당 조건은 무시
    // 상품 판매 상태 조건이 null이 아니라 판매중 or 품절 상태라면 해당 조건의 상품만 조회
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    /*
        searchDateType의 값에 따라서 dateTime의 값을 이전 시간으로 세팅 후 해당 시간 이후로 등록된 상품만 조회
        예를 들어, searchDateType 값이 "1m"인 경우 dateTime의 시간을 한 달 전으로 세팅 후
        최근 한 달 동안 등록된 상품만 조회하도록 조건값을 반환
    */
    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if(StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }

    // searchBy의 값에 따라서 상품명에 검색어를 포함하고 잇는 상품 또는 상품 생성자의 아이디에 검색어를 포함하고 있는 상품을 조회하도록 조건값을 반환
    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if(StringUtils.equals("itemNm", searchBy)) {
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy", searchBy)) {
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        // queryFactory를 이용해서 쿼리를 생성
        QueryResults<Item> results = queryFactory
                // 상품 데이터를 조회하기 위해서 QItem의 item을 지정
                .selectFrom(QItem.item)
                // where 조건절 : BooleanExpression 반환하는 조건문들을 넣어준다.
                // ',' 단위로 넣어줄 경우 and 조건으로 인식
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                       searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                       searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                // 데이터를 가지고 올 시작 인덱스를 지정
                .offset(pageable.getOffset())
                // 한번에 가지고 올 최대 개수를 지정
                .limit(pageable.getPageSize())
                // 조회한 리스트 및 전체 개수를 포함하는 QueryResults를 반환
                // 상품 데이터 리스트 조회 및 상품 데이터 전체 개수를 조회하는 2번의 쿼리문이 실행
                .fetchResults();

        List<Item> content = results.getResults();
        long total = results.getTotal();
        // 조회한 데이터를 Page 클래스의 구현체인 PageImpl 객체로 반환
        return new PageImpl<>(content, pageable, total);
    }

    // 검색어가 null이 아니면 상품명에 해당 검색어가 포함되는 상품을 조회하는 조건 반환
    private BooleanExpression itemNmLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
    }

    @Override
    public Page<MainItemDto> getMainITtemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<MainItemDto> results = queryFactory
                .select(
                        new QMainItemDto(  // QMainItemDto의 생성자에 반환할 값들을 넣어준다. @QueryProjection을 사용하면 DTO로 바로 조회가 가능하다.
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item)  // itemImg와 item을 내부 조인한다.
                .where(itemImg.repimgYn.eq("Y"))  // 상품 이미지의 경우 대표 상품 이미지만 불러온다.
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        // 조회한 데이터를 Page 클래스의 구현체인 PageImpl 객체로 반환
        return new PageImpl<>(content, pageable, total);
    }
}
