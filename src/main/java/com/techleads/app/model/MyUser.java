package com.techleads.app.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class MyUser {

    @Id
    private Integer id;
    private String name;
    private String location;
    private String email;
}
