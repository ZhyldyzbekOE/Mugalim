package com.example.mugalim.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "subscribers")
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subsId;
    private String name;
    private Date birthDate;
    private String phone;
    private int age;

}
