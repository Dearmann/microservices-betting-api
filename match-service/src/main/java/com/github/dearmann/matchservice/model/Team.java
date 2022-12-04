package com.github.dearmann.matchservice.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "game"})})
public class Team {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    private String logoUrl;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(nullable = false)
    private Game game;

    @OneToMany(mappedBy = "team1", cascade = CascadeType.ALL)
    private List<Match> matchesAsTeam1;

    @OneToMany(mappedBy = "team2", cascade = CascadeType.ALL)
    private List<Match> matchesAsTeam2;
}