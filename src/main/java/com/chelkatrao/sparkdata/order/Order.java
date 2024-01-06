package com.chelkatrao.sparkdata.order;

import com.chelkatrao.starter.Source;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Source("data/order.csv")
public class Order {
    private String name;
    private String desc;
    private int price;
    private long criminalId;
}
