package com.github.dearmann.betservice.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "matchId"})})
public class Bet {
    @Id
    @GeneratedValue
    private Long id;
    private String userId;
    private Long matchId;
    private Long predictedTeamId;
    private Boolean correctPrediction;
    private Boolean matchFinished;
}