package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.QMainItemDto;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import com.shop.entity.QItemImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return  searchSellStatus == null ? null
                : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    //하루, 일주일, 한달, 6개월 따라 조회 설정
    private BooleanExpression regDtsAfter(String searchDateType) {

        LocalDateTime dateTime = LocalDateTime.now();

        //조건 all 이거나  조건없다면  -> 전체 데이타 검색
        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        }else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        }else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        }else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        }else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {

        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        }else if(StringUtils.equals("createBy", searchBy)){
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    private BooleanExpression itemNmLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like(searchQuery);
    }

    /*
       1. itemSearchDto : 검색조건
       2. pageable : 페이지정보(페이지 번호, 페이지크기,정렬정보)
       3. regDtsAfter(itemSearchDto.getSearchDateType()) : 등록 날짜 필터링 조건
       4. searchSellStatusEq(itemSearchDto.getSearchSellStatus()) : 판매 상태 필터링 조건
       5. searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery())
            : 검색어에 따른 필터링 조건
       6. orderBy(QItem.item.id.desc()): 아이템 ID를 기준으로 내림차순 정렬
       7. offset(pageable.getOffset()): 조회할 데이터의 시작 위치를 설정
       8. limit(pageable.getPageSize()): 한 번에 조회할 데이터의 최대 개수를 설정
       9. fetch(): 최종적으로 쿼리를 실행하여 결과 리스트
       10. queryFactory.select(Wildcard.count).from(QItem.item): 전체 아이템 수를 세기 위해 count
       11. fetchOne(): 단일 결과를 반환, 여기서는 총 아이템 수
       12. new PageImpl<>(content, pageable, total): PageImpl 객체를 생성
          -> content: 조회된 아이템 리스트
          -> pageable: 페이징 정보
          -> total: 전체 아이템 수
     */
    @Override
    public Page<Item> getAdiminItemPage(ItemSearchDto itemsearchDto, Pageable pageable) {

        QueryResults<Item> results = queryFactory.selectFrom(QItem.item)
                .where(
                        regDtsAfter(itemsearchDto.getSearchDateType()),
                        searchSellStatusEq(itemsearchDto.getSearchSellStatus()),
                        searchByLike(itemsearchDto.getSearchBy(), itemsearchDto.getSearchQuery())
                )
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Item> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    /*
        1. queryFactory.select(...): QueryDSL을 사용하여 QMainItemDto 객체를 생성
        2. join(itemImg.item, item): itemImg와 item을 조인
        3. where(itemImg.repimgYn.eq("Y")): 대표 이미지 조건을 추가
        4. where(itemNmLike(itemSearchDto.getSearchQuery())): 아이템 이름 검색 조건을 추가
        5. orderBy(item.id.desc()): 아이템 ID를 기준으로 내림차순 정렬
        6. offset(pageable.getOffset()): 조회할 데이터의 시작 위치를 설정
        7. limit(pageable.getPageSize()): 한 번에 조회할 데이터의 최대 개수를 설정
        8. fetch(): 최종적으로 쿼리를 실행하여 결과 리스트
        9. List<MainItemDto> content
            -> new QMainItemDto()생성된 객체를 MainItemDto로 변환해서
            -> content가 리스트로 모든 정보를 가지고있음.

     */
    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemsearchDto, Pageable pageable) {

        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<MainItemDto> results  =
                queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.oriUrl,
                                item.price
                        )
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemsearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<MainItemDto>(content, pageable, total);
    }
}
