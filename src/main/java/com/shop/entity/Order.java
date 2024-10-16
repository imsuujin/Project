package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@ToString
@Setter@Getter
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne //다대일 관계
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member; //멤버는 주문을 여러개 가질 수 있다.

    @OneToMany(mappedBy = "order" ,cascade = CascadeType.ALL
            ,orphanRemoval = true, fetch = FetchType.LAZY)  //외래키 설정 하지않는다.
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate;   //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem>orderItems){
        Order order = new Order();

        order.setMember(member);//주문자

        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }

        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.ORDER);

        return order;
    }

    //총 주문 금액
    public int getTotalPrice(){
        int totalPrice = 0;

        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCEL;

        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }
}
