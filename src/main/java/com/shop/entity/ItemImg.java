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
@Entity
@Table(name = "item_img")
public class ItemImg extends BaseEntity {

    @Id
    @Column(name = "item_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgName; //이미지명
    private String oriImgName; //원본이미지명
    private String oriUrl; //이미지 경로

    private String repimgYn; //대표이미지(이미지가 여러장일 때 , 메인페이지에서 보이는 이미지)

    @ManyToOne(fetch = FetchType.LAZY)   //외래키 설정
    @JoinColumn(name="item_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Item item;
    
    public void updateItemImg(String oriImgName, String imgName, String oriUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.oriUrl = oriUrl;;
    }
}
