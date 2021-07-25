package com.codejam.workflow;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface FixtureActivities {
    void sendFixtureNotifications(Long fixtureId, Long playerId);

    boolean confirmFixture(Long fixtureId);

    boolean confirmFixturePlayed(Long fixtureId);

    void publishFixtureScoreReminder(Long fixtureId, Long playerOneId);

    boolean checkFixtureScorePublished(Long fixtureId);

    boolean confirmFixtureScore(Long fixtureId);
}
