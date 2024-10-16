package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
//Entity의 생명주기 이벤트를 관리하는 'AuditingEntityListener'를 설정
@EntityListeners(value = {AuditingEntityListener.class})

//JPA엔티티가 공통으로 사용하는 매핑 정보를 포함한 부모 클래스임을 나타냄, 이 클래스 자체로는 DB테이블과 매핑되지 않음
@MappedSuperclass
@Getter@Setter
public abstract class BaseTimeEntity {

    @CreatedDate //엔티티가 생성될 때 자동으로 시간 값이 설정됨
    @Column(updatable = false)
    private LocalDateTime regTime; //생성 시간

    @LastModifiedDate //엔티티가 수정될 때 자동으로 시간 값이 설정됨
    private LocalDateTime updateTime; //수정 시간

}
