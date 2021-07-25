package com.codejam.dao.entities;

import lombok.Builder;
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
@Builder
@Table(name = "player_stat")
public class PlayerStat extends AbstractEntity implements Serializable {
    @Id
    @Column(name = "player_id")
    private Long playerId;

    @Column(name = "won")
    private Integer won;

    @Column(name = "lose")
    private Integer lose;

    @Column(name = "total_played")
    private Integer totalPlayed;

    @Column(name = "total_abandoned")
    private Integer totalAbandoned;

    public PlayerStat(Long playerId, Integer won, Integer lose, Integer totalPlayed, Integer totalAbandoned) {
        this.playerId = playerId;
        this.won = won;
        this.lose = lose;
        this.totalPlayed = totalPlayed;
        this.totalAbandoned = totalAbandoned;
    }
}
