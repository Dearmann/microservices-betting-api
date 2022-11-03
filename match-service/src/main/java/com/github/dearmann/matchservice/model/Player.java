package com.github.dearmann.matchservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private int age;

    private String country;

    @ManyToOne
    @JoinColumn
    private Team team;
}