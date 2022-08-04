package CurrencyStudy.controller;

import CurrencyStudy.Launcher;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class RefreshTask extends Task<Void> {
    @Override
    protected Void call() throws Exception {
        for (;;) {
            try {
                Thread.sleep((long)(60 * 1e3));
            } catch (InterruptedException e) {
                System.out.println("Encountered an interrupted exception");
            }
            Platform.runLater(() -> {
                try {
                    Launcher.refreshPane();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            FutureTask futureTask = new FutureTask(new WatchTask());
            Platform.runLater(futureTask);
            try {
                futureTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}