package com.codejam.config;

import com.codejam.dao.entities.Fixture;
import com.codejam.dao.respository.FixtureRepository;
import com.codejam.workflow.FixtureActivitiesImpl;
import com.codejam.workflow.FixtureWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class TemporalConfig {

    @Bean
    public WorkflowServiceStubs workflowServiceStubs() {
        return WorkflowServiceStubs.newInstance();
    }

    @Bean
    @DependsOn({"workflowServiceStubs"})
    public WorkflowClient workflowClient(@Autowired WorkflowServiceStubs workflowServiceStubs) {
        return WorkflowClient.newInstance(workflowServiceStubs);
    }

    @Bean
    @DependsOn({"workflowClient"})
    public WorkerFactory workerFactory(@Autowired WorkflowClient workflowClient) {
        return WorkerFactory.newInstance(workflowClient);
    }

    @Bean
    @DependsOn({"workerFactory","fixtureRepository"})
    public Worker worker(@Autowired WorkerFactory workerFactory,
                         @Autowired FixtureRepository fixtureRepository) {
        final Worker worker = workerFactory.newWorker("spring-temporal-example");
        worker.registerWorkflowImplementationTypes(FixtureWorkflowImpl.class);
        worker.registerActivitiesImplementations(new FixtureActivitiesImpl(fixtureRepository));
        workerFactory.start();
        return worker;
    }
}
