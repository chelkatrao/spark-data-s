package com.chelkatrao.sparkdata.criminal;

import com.chelkatrao.starter.unsafe.SparkRepository;

import java.util.List;

public interface CriminalRepository extends SparkRepository<Criminal> {
    List<Criminal> findByNumberBetween(int min, int max);
    List<Criminal> findByNumberGreaterThan(int min);
    long findByNumberGreaterThanCount(int min);
}
