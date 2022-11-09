package com.github.dearmann.betservice.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
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
    private Long userId;
    private Long matchId;
    private Long predictedTeamId;
    private Boolean correctPrediction;
    private Boolean matchFinished;
}