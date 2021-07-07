package com.example.mugalim.models.dto;

import com.example.mugalim.enums.OrderStatus;
import lombok.Data;
import java.util.Date;

@Data
public class OrderDto {
    private Long id;
    private String schoolName;
    private String schoolAddress;
    private Date addDate;
    private Date endDate;
    private Date naviDate;
    private String comment;
    private SubscriberDto subscriber;
    private OrderStatus orderStatus;
}
