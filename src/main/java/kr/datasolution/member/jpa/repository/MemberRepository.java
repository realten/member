package kr.datasolution.member.jpa.repository;

import kr.datasolution.member.jpa.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    void
}
