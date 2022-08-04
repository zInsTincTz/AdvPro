package CurrencyStudy;

import CurrencyStudy.controller.FetchData;
import CurrencyStudy.controller.Initialize;
import CurrencyStudy.controller.RefreshTask;
import CurrencyStudy.model.Currency;
import CurrencyStudy.view.CurrencyParentPane;
import CurrencyStudy.view.TopPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Launcher extends Application {

    private static Stage primaryStage;
    private static Scene mainScene;
    private static FlowPane mainPane;
    private static TopPane topPane;
    private static CurrencyParentPane currencyParentPane;
    private static ArrayList<Currency> currencyList;
    @Override
    public void start(Stage primaryStage) throws ExecutionException, InterruptedException {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Currency");
        this.primaryStage.setResizable(false);
        Initialize.initialize_app();
        initMainPane();
        mainScene = new Scene(mainPane);
        this.primaryStage.setScene(mainScene);
        this.primaryStage.show();
        RefreshTask r = new RefreshTask();
        Thread th = new Thread(r);
        th.setDaemon(true);
        th.start();
    }
    public void initMainPane() throws ExecutionException, InterruptedException {
        mainPane = new FlowPane();
        topPane = new TopPane();
        currencyParentPane = new CurrencyParentPane(this.currencyList);
        mainPane.getChildren().add(topPane);
        mainPane.getChildren().add(currencyParentPane);
    }

    public static ArrayList<Currency> getCurrencyList() {
        return currencyList;
    }

    public static void refreshPane() {
        try {
            topPane.refreshPane();
            currencyParentPane.refreshPane(currencyList);
            primaryStage.sizeToScene();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static void setCurrencyList(ArrayList<Currency> currencyList) {
        Launcher.currencyList = currencyList;
    }
}