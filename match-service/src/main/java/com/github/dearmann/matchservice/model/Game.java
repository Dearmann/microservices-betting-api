package com.github.dearmann.matchservice.model;

import com.github.dearmann.matchservice.repository.GameRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Event> events;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Team> teams;
}