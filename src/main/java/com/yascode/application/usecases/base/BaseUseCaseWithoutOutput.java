package com.yascode.application.usecases.base;

public abstract class BaseUseCaseWithoutOutput<RQ> {
    private static final LoggerConsole console = new LoggerConsole();

    public void run(RQ request) {
        long startTime = System.currentTimeMillis();

        try {
            execute(request);
        } catch (Exception e) {
            console.logWithBox("Error during execution of UseCase: " + this.getClass().getSimpleName() + "\n" +
                    "Error Details: " + e.getMessage());
            throw e;
        }

        long endTime = System.currentTimeMillis();
        console.logWithBox(
                "UseCase started: " + this.getClass().getSimpleName() + "\n" +
                "Input: " + request + "\n" +
                "Duration: " + (endTime - startTime)  + " ms"
        );
    }

    public abstract void execute(RQ request);
}