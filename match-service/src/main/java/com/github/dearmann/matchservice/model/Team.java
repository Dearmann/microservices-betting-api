package com.github.dearmann.matchservice.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Team {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(nullable = false)
    private Game game;

    @OneToMany(mappedBy = "team1", cascade = CascadeType.ALL)
    private List<Match> matchesAsTeam1;

    @OneToMany(mappedBy = "team2", cascade = CascadeType.ALL)
    private List<Match> matchesAsTeam2;
}