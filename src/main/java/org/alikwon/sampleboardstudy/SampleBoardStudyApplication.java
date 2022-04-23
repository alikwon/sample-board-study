package org.alikwon.sampleboardstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SampleBoardStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleBoardStudyApplication.class, args);
    }

}
