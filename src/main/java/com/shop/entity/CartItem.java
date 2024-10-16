package com.shop.entity;

import com.shop.constant.ItemSize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Setter
@Getter
@ToString
@Entity
@Table(name = "cart_item")
public class CartItem extends BaseEntity {

    @Id
    @Column(name = "cart_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 다대일 관계 즉 여러 카트아이템이 카트에 속할 수 있다..
    @ManyToOne(fetch = FetchType.LAZY)//fetch = FetchType.LAZY 지연 로딩을 사용, 지연 로딩은 관련 엔티티를 실제로 필요한 시점에 로드하는 방식. 장점 필요할 때만 데이터를 가져오므로 메모리 사용량 줄이고,
    @OnDelete(action = OnDeleteAction.CASCADE)
    //성능 향상 특히, 관련 엔티티의 데이터 양이 많거나 쿼리 성능이 중요한 경우 유용
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "item_id" )
    private Item item;

    private int count;

    @Enumerated(EnumType.STRING)
    private ItemSize itemSize;

    public static CartItem createCartItem(Cart cart, Item item, int count, ItemSize itemSize){
        CartItem cartItem = new CartItem();

        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        cartItem.setItemSize(itemSize);

        return cartItem;
    }

    public void addCount(int count){
        this.count += count;
    }

    public void updateCount(int count){
        this.count = count;
    }
}
