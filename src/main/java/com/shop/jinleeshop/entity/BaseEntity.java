package com.shop.jinleeshop.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

// Auditing을 적용하기 위한 어노테이션
@EntityListeners(value = {AuditingEntityListener.class})
// 공통 매핑 정보가 필요할 때 사용하는 어노테이션으로 부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공
@MappedSuperclass
@Getter
// 이 클래스를 상속받으면 등록일, 수정일, 등록자, 수정자를 자동으로 입력해준다.
public class BaseEntity extends BaseTimeEntity{

    @CreatedBy  // 엔티티가 생성되어 저장될 때 등록자를 자동으로 저장
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy  // 엔티티가 값을 변경할 때 수정자를 자동으로 저장
    private String modifiedBy;

}
