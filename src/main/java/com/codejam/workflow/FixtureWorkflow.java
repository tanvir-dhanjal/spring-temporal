package com.codejam.workflow;

import com.codejam.dao.entities.Fixture;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface FixtureWorkflow {

    @WorkflowMethod
    void runFixtureWorkflow(Fixture fixture);

    @SignalMethod
    void cancelFixture();
}
