package org.alikwon.sampleboardstudy.repository;

import org.alikwon.sampleboardstudy.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}
