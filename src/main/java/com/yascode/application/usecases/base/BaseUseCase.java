package com.yascode.application.usecases.base;

import com.yascode.application.services.LoggerConsoleService;

public abstract class BaseUseCase<RQ, RS> {
    private static final LoggerConsoleService console = new LoggerConsoleService();

    public RS run(RQ request) {
        long startTime = System.currentTimeMillis();

        RS response = null;
        try {
            response = execute(request);
        } catch (Exception e) {
            console.logWithBox("Error during execution of UseCase: " + this.getClass().getSimpleName() + "\n" +
                    "Error Details: " + e.getMessage());
            throw e;
        }

        long endTime = System.currentTimeMillis();
        console.logWithBox(
                "UseCase started: " + this.getClass().getSimpleName() + "\n" +
                "Input: " + request + "\n" +
                "Output: " + response + "\n" +
                "Duration: " + (endTime - startTime)  + " ms"
        );

        return response;
    }

    protected abstract RS execute(RQ request);

}