package com.codejam.service.impl;

import com.codejam.dao.entities.Fixture;
import com.codejam.dao.entities.FixtureSetScore;
import com.codejam.dao.entities.FixtureStatus;
import com.codejam.dao.entities.Player;
import com.codejam.dao.respository.FixtureRepository;
import com.codejam.dao.respository.FixtureSetScoreRepository;
import com.codejam.dao.respository.PlayerRepository;
import com.codejam.dao.respository.PlayerStatRepository;
import com.codejam.error.ServiceErrorsEnum;
import com.codejam.error.ServiceException;
import com.codejam.service.ApiService;
import com.codejam.workflow.FixtureWorkflow;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowException;
import io.temporal.client.WorkflowOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Service
public class ApiServiceImpl implements ApiService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiServiceImpl.class);
    private static final String TASK_QUEUE = "spring-temporal-example";

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PlayerStatRepository playerStatRepository;

    @Autowired
    FixtureRepository fixtureRepository;

    @Autowired
    FixtureSetScoreRepository fixtureSetScoreRepository;

    @Autowired
    WorkflowClient workflowClient;

    @Override
    public CompletionStage<Player> createPlayer(String firstName, String lastName, float level) {
        final Player player = playerRepository.save(new Player(firstName, lastName, level));
        return CompletableFuture.completedFuture(player);
    }

    @Override
    public CompletionStage<Player> getPlayer(Long playerId) {
        final Optional<Player> player = playerRepository.findById(playerId);
        if (player.isPresent()) {
            return CompletableFuture.completedFuture(player.get());
        } else {
            LOGGER.error("Player with id {} not found", playerId);
            throw new ServiceException(ServiceErrorsEnum.PLAYER_NOT_FOUND);
        }
    }

    @Override
    public CompletionStage<Fixture> createFixture(Long playerOneId, Long playerTwoId, String scheduledFor) {
        try {
            final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH);
            final Fixture fixture = fixtureRepository.save(new Fixture(playerOneId, playerTwoId, FixtureStatus.SCHEDULED, formatter.parse(scheduledFor), playerOneId));

            //Trigger work flow
            final WorkflowOptions options = WorkflowOptions.newBuilder().setTaskQueue(TASK_QUEUE).build();
            final FixtureWorkflow fixtureWorkflow = workflowClient.newWorkflowStub(FixtureWorkflow.class, options);

            // Returns as soon as the workflow starts and save workflow id in DB.
            final WorkflowExecution workflowExecution = WorkflowClient.start(fixtureWorkflow::runFixtureWorkflow, fixture);
            LOGGER.info("Started process file workflow with workflowId=\"" + workflowExecution.getWorkflowId()
                    + "\" and runId=\"" + workflowExecution.getRunId() + "\"");
            fixture.setWorkflowId(workflowExecution.getWorkflowId());
            fixtureRepository.save(fixture);

            return CompletableFuture.completedFuture(fixture);
        } catch (ParseException | WorkflowException e) {
            LOGGER.error("Failed with exception {e}", e);
            throw new ServiceException(ServiceErrorsEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CompletionStage<Fixture> getFixture(Long playerId, Long fixtureId) {
        return getPlayer(playerId)
                .thenApply(player -> Optional.ofNullable(fixtureRepository.getPlayerFixture(playerId, fixtureId))
                        .orElseThrow(() -> new ServiceException(ServiceErrorsEnum.FIXTURE_NOT_FOUND)));
    }

    @Override
    public CompletionStage<Fixture> updateFixture(Long playerId, Long fixtureId, String status) {
        return getFixture(playerId, fixtureId)
                .thenApply(fixture -> {
                    final FixtureStatus fixtureStatus = FixtureStatus.getEnum(status.toLowerCase());
                    if (fixtureStatus == FixtureStatus.CANCELED) {
                        LOGGER.warn("Cancelling Fixture workflow and fixture");
                        FixtureWorkflow workflowById = workflowClient.newWorkflowStub(FixtureWorkflow.class, fixture.getWorkflowId());

                        // Send the signal to our complete the workflow and cancel fixture.
                        workflowById.cancelFixture();
                    }

                    if (fixtureStatus == FixtureStatus.INVALID) {
                        throw new ServiceException(ServiceErrorsEnum.INVALID_FIXTURE_STATUS);
                    }

                    fixture.setStatus(fixtureStatus);
                    return fixtureRepository.save(fixture);
                });
    }

    @Override
    public CompletionStage<FixtureSetScore> createFixtureSetScore(Long fixtureId, Integer setNumber, Integer playerOneSetScore, Integer playerTwoSetScore) {
        final FixtureSetScore fixtureSetScore = fixtureSetScoreRepository.save(new FixtureSetScore(fixtureId, setNumber, playerOneSetScore, playerTwoSetScore));
        return CompletableFuture.completedFuture(fixtureSetScore);
    }
}
