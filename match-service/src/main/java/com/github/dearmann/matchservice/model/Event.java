package com.github.dearmann.matchservice.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Event {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String region;

    private Integer season;

    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime start;

    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime end;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(nullable = false)
    private Game game;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Match> matches;
}