package com.codejam.workflow;

import com.codejam.dao.entities.Fixture;
import com.codejam.dao.entities.FixtureStatus;
import com.codejam.dao.respository.FixtureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixtureActivitiesImpl implements FixtureActivities {
    private static final Logger LOGGER = LoggerFactory.getLogger(FixtureActivitiesImpl.class);

    private final FixtureRepository fixtureRepository;

    public FixtureActivitiesImpl(FixtureRepository fixtureRepository) {
        this.fixtureRepository = fixtureRepository;
    }

    @Override
    public void sendFixtureNotifications(Long fixtureId, Long playerId) {
        LOGGER.info("Sending notifications to {} and waiting for the fixture confirmation", playerId);
    }

    @Override
    public boolean confirmFixture(Long fixtureId) {
        LOGGER.info("Checking if fixture was confirm");
        final Fixture fixture = fixtureRepository.findById(fixtureId).get();
        return (fixture.getStatus().equals(FixtureStatus.CONFIRMED));
    }

    @Override
    public boolean confirmFixturePlayed(Long fixtureId) {
        LOGGER.info("Checking if fixture was played");
        final Fixture fixture = fixtureRepository.findById(fixtureId).get();
        return (fixture.getStatus().equals(FixtureStatus.PLAYED));
    }

    @Override
    public void publishFixtureScoreReminder(Long fixtureId, Long playerOneId) {
        LOGGER.info("Sending notifications to {} and to publish fixture {} score", playerOneId, fixtureId);
    }

    @Override
    public boolean checkFixtureScorePublished(Long fixtureId) {
        LOGGER.info("Checking if fixture score was published");
        final Fixture fixture = fixtureRepository.findById(fixtureId).get();
        return (fixture.getStatus().equals(FixtureStatus.SCORE_PUBLISHED));
    }

    @Override
    public boolean confirmFixtureScore(Long fixtureId) {
        LOGGER.info("Checking if fixture score was validated");
        final Fixture fixture = fixtureRepository.findById(fixtureId).get();
        return (fixture.getStatus().equals(FixtureStatus.SCORE_VALIDATED));
    }
}
