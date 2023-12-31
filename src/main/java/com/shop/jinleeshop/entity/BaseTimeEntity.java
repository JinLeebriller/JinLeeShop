package com.shop.jinleeshop.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

// Auditing을 적용하기 위한 어노테이션
@EntityListeners(value = {AuditingEntityListener.class})
// 공통 매핑 정보가 필요할 때 사용하는 어노테이션으로 부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공
@MappedSuperclass
@Getter @Setter
// 이 클래스를 상속받으면 등록일, 수정일을 자동으로 입력해준다.
public class BaseTimeEntity {

    @CreatedDate  // 엔티티가 생성되어 저장될 때 시간을 자동으로 저장
    @Column(updatable = false)
    private LocalDateTime regTime;

    @LastModifiedDate  // 엔티티가 값을 변경할 때 시간을 자동으로 저장
    private LocalDateTime updateTime;

}
