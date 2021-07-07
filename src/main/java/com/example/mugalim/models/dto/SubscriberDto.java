package com.example.mugalim.models.dto;

import lombok.Data;
import java.util.Date;

@Data
public class SubscriberDto {
    private Long subsId;
    private String name;
    private Date birthDate;
    private String phone;
    private int age;
}
