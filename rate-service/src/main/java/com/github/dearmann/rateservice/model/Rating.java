package com.github.dearmann.rateservice.model;

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
public class Rating {
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private Long matchId;
    private Integer rating;
}