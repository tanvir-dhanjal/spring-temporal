package com.codejam.workflow;

import com.codejam.dao.entities.Fixture;
import com.codejam.error.ServiceException;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;


public class FixtureWorkflowImpl implements FixtureWorkflow {
    private static final Logger LOGGER = LoggerFactory.getLogger(FixtureActivitiesImpl.class);

    private final RetryOptions retryOptions = RetryOptions.newBuilder()
            .setInitialInterval(Duration.ofSeconds(10))
            .setBackoffCoefficient(4)
            .setMaximumInterval(Duration.ofSeconds(60))
            .build();

    private final ActivityOptions options =
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofHours(1))
                    .setRetryOptions(retryOptions)
                    .build();

    private final FixtureActivities activities =
            Workflow.newActivityStub(FixtureActivities.class, options);

    @Override
    public void runFixtureWorkflow(Fixture fixture) {
        try {
            LOGGER.info("Running workflow on fixture {} created by {}", fixture.getFixtureId(), fixture.getPlayerOneId());

            // Send notification about fixture is created
            activities.sendFixtureNotifications(fixture.getFixtureId(), fixture.getPlayerTwoId());

            // Check if fixture was confirmed by other player
            Workflow.await(() -> activities.confirmFixture(fixture.getFixtureId()));

            // Check if fixture was played
            Workflow.await(() -> activities.confirmFixturePlayed(fixture.getFixtureId()));

            // Send notification about pending score
            activities.publishFixtureScoreReminder(fixture.getFixtureId(), fixture.getPlayerOneId());

            // Check if Feature score published
            Workflow.await(() -> activities.checkFixtureScorePublished(fixture.getFixtureId()));

            // Check if Feature score was validated by other player
            Workflow.await(() -> activities.confirmFixtureScore(fixture.getFixtureId()));

        } catch (ActivityFailure | ServiceException e) {
            LOGGER.error("Failed at fetch Fixture {}", e);
        }
    }
}
