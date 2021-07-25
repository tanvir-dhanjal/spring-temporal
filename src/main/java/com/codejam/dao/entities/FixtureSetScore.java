package com.codejam.dao.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Getter
@ToString
@NoArgsConstructor
@Table(name = "fixture_set_score")
public class FixtureSetScore extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fixture_set_score_id")
    private Long fixtureSetScoreId;

    @Column(name = "fixture_id")
    private Long fixtureId;

    @Column(name = "set_number")
    private Integer setNumber;

    @Column(name = "player_one_set_score")
    private Integer playerOneSetScore;

    @Column(name = "player_one_two_score")
    private Integer playerTwoSetScore;

    public FixtureSetScore(Long fixtureId,
                           Integer setNumber,
                           Integer playerOneSetScore,
                           Integer playerTwoSetScore) {
        this.fixtureId = fixtureId;
        this.setNumber = setNumber;
        this.playerOneSetScore = playerOneSetScore;
        this.playerTwoSetScore = playerTwoSetScore;
    }
}
