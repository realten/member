package kr.datasolution.member.controller;

import io.swagger.annotations.ApiOperation;
import kr.datasolution.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    /**
     * Method Get Test
     *  - Swagger API의 ApiParam을 사용한 방식
     *  - @ApiOperation : API 정보설정(제목, 설명 등)
     *  - @GetMapping : API 호출 URL주소, HttpMethod 설정
     *  - @ApiParam 이 @RequestParam 을 대체하며 Swagger-UI에도 자동으로 적용
     *
     * @return
     */
    @ApiOperation(value = "ApiParam 사용", notes = "API 설명")
    @GetMapping(value = "findAll")
    public Map<String, Object> findAll() {
        Map<String, Object> map = new HashMap<>();
        map.put("jpa", memberService.findAll());
        map.put("mybatis", memberService.getMemberList());
        return map;
    }

    /**
     * Method Get Test
     *  - Swagger API의 ApiParam을 사용한 방식
     *  - @ApiOperation : API 정보설정(제목, 설명 등)
     *  - @GetMapping : API 호출 URL주소, HttpMethod 설정
     *  - @ApiParam 이 @RequestParam 을 대체하며 Swagger-UI에도 자동으로 적용
     *
     * @return
     */
    @ApiOperation(value = "ApiParam 사용", notes = "API 설명")
    @GetMapping(value = "save")
    public Map<String, Object> save() {
        Map<String, Object> map = new HashMap<>();
        memberService.save();
        map.put("result", true);
        return map;
    }
}
