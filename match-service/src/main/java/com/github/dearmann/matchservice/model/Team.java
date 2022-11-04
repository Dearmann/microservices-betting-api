package com.github.dearmann.matchservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Game game;

    @OneToMany(mappedBy = "teamOne")
    private List<Match> matchesAsTeamOne;

    @OneToMany(mappedBy = "teamTwo")
    private List<Match> matchesAsTeamTwo;
}