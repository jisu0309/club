package com.example.club.entity;


import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass   // 이 클래스는 테이블로 생성되지 않음. 얘를 상속한 엔티티의 클래스로 db테이블이 생성됨
@EntityListeners(value = {AuditingEntityListener.class })   //jpa 내부에서 엔티티객체가 생성/변경되는거 감지. 얘를 통해서 regDate, modDate에 적절한 값 지정됨
@Getter
abstract class BaseEntity {

    @CreatedDate
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(name = "moddate")
    private LocalDateTime modDate;

}
