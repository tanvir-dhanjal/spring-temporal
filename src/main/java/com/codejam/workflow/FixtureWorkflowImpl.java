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
            .setMaximumAttempts(30)
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
            while (!activities.confirmFixture(fixture.getFixtureId())) {
                LOGGER.info("fixture was not confirmed yet , will retry in 60 sec");
                // Sleep for 60 secs
                Workflow.sleep(Duration.ofSeconds(60));
            }

            // Check if fixture was played
            while (!activities.confirmFixturePlayed(fixture.getFixtureId())) {
                // Sleep for 60 secs
                Workflow.sleep(Duration.ofSeconds(60));
            }

            // Check if Feature score published
            boolean scoreReminder = false;
            while (!activities.checkFixtureScorePublished(fixture.getFixtureId())) {
                if (!scoreReminder) {
                    // Send notification about pending score
                    activities.publishFixtureScoreReminder(fixture.getFixtureId(), fixture.getPlayerOneId());
                }
                // Sleep for 60 secs
                Workflow.sleep(Duration.ofSeconds(60));
                scoreReminder = true;
            }

            // Check if Feature score was validated by other player
            while (!activities.confirmFixtureScore(fixture.getFixtureId())) {
                // Sleep for 60 secs
                Workflow.sleep(Duration.ofSeconds(60));
            }
        } catch (ActivityFailure | ServiceException e) {
            LOGGER.error("Failed to execute workflow with " + e);
        }
    }
}
