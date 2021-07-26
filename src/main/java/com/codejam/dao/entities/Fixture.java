package com.codejam.dao.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@ToString
@NoArgsConstructor
@Setter
@Table(name = "fixture")
public class Fixture extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fixture_id")
    private Long fixtureId;

    @Column(name = "player_one_id")
    private Long playerOneId;

    @Column(name = "player_two_id")
    private Long playerTwoId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private FixtureStatus status;

    @Column(name = "scheduled_for")
    private Date scheduledFor;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "workflow_id")
    private String workflowId;

    public Fixture(Long playerOneId, Long playerTwoId, FixtureStatus status, Date scheduledFor,  Long createdBy) {
        this.playerOneId = playerOneId;
        this.playerTwoId = playerTwoId;
        this.status = status;
        this.scheduledFor = scheduledFor;
        this.createdBy = createdBy;
    }
}
