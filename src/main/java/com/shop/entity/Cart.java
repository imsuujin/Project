package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Setter
@Getter
@ToString
@Entity //개체 테이블을 의미함
@Table(name = "cart")
public class Cart extends BaseEntity {

    @Id//기본키 설정
    @Column(name = "cart_id")//컬럼명 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY)//AUTO INCREMENT BY...
    private Long id;

    //하나의 멤버는 하나의 카트를 가질 수 있다.
    @OneToOne(fetch = FetchType.LAZY) //일대일 관계 매핑
    @JoinColumn(name = "member_id")//연관된 컬럼
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member; //연관된 entity..즉 테이블


    //장바구니를 만드는 정적 메서드
    public static Cart createCart(Member member) {
        Cart cart = new Cart();

        cart.setMember(member);
        return cart;
    }
}
