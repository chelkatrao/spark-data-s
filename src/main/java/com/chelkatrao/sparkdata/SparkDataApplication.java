package com.chelkatrao.sparkdata;

import com.chelkatrao.sparkdata.criminal.CriminalRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SparkDataApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SparkDataApplication.class, args);


        testCriminalCsv(run);
//        testProductJson(run);
    }

    private static void testCriminalCsv(ConfigurableApplicationContext run) {
        CriminalRepository criminalRepository = run.getBean(CriminalRepository.class);
        long count = criminalRepository.findByNumberGreaterThanCount(500);

//        for (Criminal criminal : list)
            System.out.println("count = " + count);
    }

}
