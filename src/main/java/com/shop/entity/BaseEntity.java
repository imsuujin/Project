package com.shop.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

//Entity의 생명주기 이벤트를 관리하는 'AuditingEntityListener'를 설정, 주로 생성일시와 수정일시를 자동 관리하기 위해 사용됨.
@EntityListeners(value = {AuditingEntityListener.class})

//JPA엔티티가 공통으로 사용하는 매핑 정보를 포함한 부모 클래스임을 나타냄, 이 클래스 자체로는 DB테이블과 매핑되지 않음
@MappedSuperclass
@Getter
public abstract class BaseEntity  extends  BaseTimeEntity{ //작성자, 수정자, 생성시간, 수정시간을 가지고 있음..

    @CreatedBy//엔티티가 생성될 때 자동으로 값이 설정됨.
    @Column(updatable = false)
    private String createdBy;  //작성자
    
    @LastModifiedBy//엔티티가 수정될 때 자동으로 값이 설정됨.
    private String modifiedBy; //수정자

}
