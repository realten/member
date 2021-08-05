package kr.datasolution.member.service;

import kr.datasolution.member.jpa.domain.MemberEntity;
import kr.datasolution.member.jpa.repository.MemberRepository;
import kr.datasolution.member.mybatis.vo.MemberVo;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    /** 주 DATABASE - SQL SESSION TEMPLETE 객체 */
    @Autowired
    @Qualifier("sqlSessionTemplate")
    protected SqlSessionTemplate dao;

    @Transactional(readOnly = true)
    public List<MemberEntity> findAll() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<MemberVo> getMemberList() {
        return dao.selectList("kr.datasolution.member.selectMemberList");
    }

    @Transactional(value = "platformTransactionManager")
    public void save() {
        log.info("SpringPhysicalNamingStrategy - {}", SpringPhysicalNamingStrategy.class.getName());
        MemberEntity member = new MemberEntity();
        member.setMemberNo("0000001");
        member.setMemberId("realten");
        member.setMemberName("원영진");
        member.setRegDt(LocalDateTime.now());
        memberRepository.saveAndFlush(member);
        //memberRepository.flush();
        insertMember();
    }

    @Transactional(value = "mybatisDataSourceTransactionManager")
    public void insertMember() {
        MemberVo member = new MemberVo();
        member.setMemberNo("00000001");
        member.setMemberId("realten");
        member.setMemberName("원영진");
        member.setRegDt(LocalDateTime.now());
        dao.insert("kr.datasolution.member.insertMember", member);
        log.info("insertMember List - {}", dao.selectList("kr.datasolution.member.selectMemberList"));
    }
}
