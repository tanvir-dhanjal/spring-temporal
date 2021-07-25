package com.codejam;

import com.codejam.resource.PlayersResource;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.WorkerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class TemporalApplication extends ResourceConfig {

    public TemporalApplication() {
        register(PlayersResource.class);
    }

    /**
     * Main method that starts the Spring Boot application.
     *
     * @param args Arguments passed to the app.
     */
    public static void main(String[] args) {
        SpringApplication.run(TemporalApplication.class, args);
    }
}
