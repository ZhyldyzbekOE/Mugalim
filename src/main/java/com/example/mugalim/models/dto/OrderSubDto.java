package com.example.mugalim.models.dto;

import lombok.Data;
import java.util.Date;

@Data
public class OrderSubDto {
    private String name;
    private String phone;
    private String schoolName;
    private String schoolAddress;
    private Date birthDate;
}
