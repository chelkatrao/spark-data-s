package com.chelkatrao.sparkdata;

import com.chelkatrao.sparkdata.criminal.Criminal;
import com.chelkatrao.sparkdata.criminal.CriminalRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class SparkDataApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SparkDataApplication.class, args);
        testCriminalCsv(run);
    }

    private static void testCriminalCsv(ConfigurableApplicationContext run) {
        CriminalRepository criminalRepository = run.getBean(CriminalRepository.class);
        List<Criminal> byNumberGreaterThan = criminalRepository.findByNumberGreaterThan(0);

        for (Criminal criminal : byNumberGreaterThan)
            System.out.println("criminal = " + criminal);
    }

}
