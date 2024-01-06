package com.chelkatrao.sparkdata.criminal;


import com.chelkatrao.starter.ForeignKeyName;
import com.chelkatrao.starter.Source;
import com.chelkatrao.sparkdata.order.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Source("data/criminals.csv")
public class Criminal {
    private long id;
    private String name;
    private int number;
    @ForeignKeyName(value = "criminalId")
    private List<Order> orders;
}
