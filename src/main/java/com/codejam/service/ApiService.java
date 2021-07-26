package com.codejam.service;

import com.codejam.dao.entities.Fixture;
import com.codejam.dao.entities.FixtureSetScore;
import com.codejam.dao.entities.Player;

import java.util.concurrent.CompletionStage;

public interface ApiService {
    CompletionStage<Player> createPlayer(String firstName, String lastName, float level);

    CompletionStage<Player> getPlayer(Long playerId);

    CompletionStage<Fixture> createFixture(Long playerOneId, Long playerTwoId, String scheduledFor);

    CompletionStage<Fixture> getFixture(Long playerId, Long fixtureId);

    CompletionStage<Fixture> updateFixture(Long playerId, Long fixtureId, String fixtureStatus);

    CompletionStage<FixtureSetScore> createFixtureSetScore(Long fixtureId, Integer setNumber, Integer playerOneSetScore, Integer playerTwoSetScore);
}
