package kr.datasolution.member.mybatis.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString(callSuper = true)
public class MemberVo {
    private String memberNo;
    private String memberId;
    private String memberName;
    private LocalDateTime regDt;
    private LocalDateTime updDt;
}
