package org.alikwon.sampleboardstudy.repository;

import org.alikwon.sampleboardstudy.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void insertMembers() {
        IntStream.rangeClosed(1,100).forEach(i -> {
            Member member = Member.builder()
                    .email("user" + i + "@test.com")
                    .password("1111")
                    .name("유저" + i)
                    .build();
            memberRepository.save(member);
        });
    }
}
