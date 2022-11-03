package com.github.dearmann.matchservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "match_")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn
    private Team teamOne;

    @ManyToOne
    @JoinColumn
    private Team teamTwo;

    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime startDate;

    private Boolean teamOneWon;

    private Boolean teamTwoWon;

    @ManyToOne
    @JoinColumn
    private Event event;
}
