package kr.datasolution.member.jpa.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @Entity : 데이터베이스의 테이블명과 매핑한다.
 * @Table : class명이 데이터베이스의 테이블명과 다를 경우 name=""을 사용하여 매핑이 가능하다.
 */
@Entity
@Table(name = "members")
@Getter
@Setter
@ToString(callSuper = true)
public class MemberEntity {
    /**
     * @Id : primary key를 가지는 변수를 선언한다.
     * @Column : 변수의 컬럼명 매핑이나 속성을 설정할 수 있다.
     */
    @Id
    @Column(length = 7, nullable = false)
    private String memberNo;
    @Column(length = 20, nullable = false)
    private String memberId;
    @Column(length = 20, nullable = false)
    private String memberName;
    @Column(nullable = false)
    private LocalDateTime regDt;
    private LocalDateTime updDt;
}
