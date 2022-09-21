package chapter3downloader.controller;

import chapter3downloader.Launcher;
import chapter3downloader.model.FileFreq;
import chapter3downloader.model.PDFdocument;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainViewController {
    LinkedHashMap<String, ArrayList<FileFreq>> uniqueSets;
    @FXML
    private ListView<String> inputListView;
    private List<String> inputListViewAbsolutePath = new ArrayList<>();
    @FXML
    private Button startButton;
    @FXML
    private MenuBar menuBar;
    @FXML
    private ListView listView;
    @FXML
    public void initialize() {

        // Exercise 4 Let the user use either small or capital letters for the currency short code.
        menuBar.getMenus().get(0).getItems().get(0).setOnAction(actionEvent -> {
            Platform.exit();
        });

        inputListView.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            final boolean isAccepted = db.getFiles().get(0).getName().endsWith(".pdf");
            if (db.hasFiles() && isAccepted) {
                event.acceptTransferModes(TransferMode.COPY);
            } else {
                event.consume();
            }
        });

        inputListView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;
                int total_files = db.getFiles().size();
                for (int i=0; i<total_files; i++) {
                    File file = db.getFiles().get(i);
                    inputListView.getItems().add(file.getName());
                    inputListViewAbsolutePath.add(file.getAbsolutePath());
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        startButton.setOnAction(actionEvent -> {
            Parent bgRoot = Launcher.stage.getScene().getRoot();
            Task<Void> processTask = new Task<Void>() {
                @Override
                public Void call() throws Exception {
                    ProgressIndicator pi = new ProgressIndicator();
                    VBox box = new VBox(pi);
                    box.setAlignment(Pos.CENTER);
                    Launcher.stage.getScene().setRoot(box);
                    ExecutorService executorService = Executors.newFixedThreadPool(4);
                    final ExecutorCompletionService<Map<String, FileFreq>> completionService = new ExecutorCompletionService<>(executorService);

                    // Exercise 3 Add an Unwatch button. This button should be located next to the watch button, i.e., to the
                    //right of the application window. After this Unwatch button is clicked, the watch rate should
                    //be disappeared and all the programming logic behind it should also be unset.
                    int total_files = inputListViewAbsolutePath.size();
                    Map<String, FileFreq>[] wordMap = new Map[total_files];
                    for (int i = 0; i < total_files; i++) {
                        try {
                            String filePath = inputListViewAbsolutePath.get(i);
                            PDFdocument p = new PDFdocument(filePath);
                            completionService.submit(new WordMapPageTask(p));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    for (int i = 0; i < total_files; i++) {
                        try {
                            Future<Map<String, FileFreq>> future = completionService.take();
                            wordMap[i] = future.get();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        WordMapMergeTask merger = new WordMapMergeTask(wordMap);
                        Future<LinkedHashMap<String, ArrayList<FileFreq>>> future = executorService.submit(merger);
                        uniqueSets = future.get();
                        listView.getItems().addAll(uniqueSets.keySet());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        executorService.shutdown();
                    }
                    return null;
                }
            };
            processTask.setOnSucceeded(e -> {
                Launcher.stage.getScene().setRoot(bgRoot);
            });
            Thread thread = new Thread(processTask);
            thread.setDaemon(true);
            thread.start();
        });

        listView.setOnMouseClicked(event -> {
            ArrayList<FileFreq> listOfLinks = uniqueSets.get(listView.getSelectionModel().getSelectedItem());
            ListView<FileFreq> popupListView = new ListView<>();
            LinkedHashMap<FileFreq, String> lookupTable = new LinkedHashMap<>();
            for (int i=0; i<listOfLinks.size(); i++) {
                lookupTable.put(listOfLinks.get(i),listOfLinks.get(i).getPath());
                popupListView.getItems().add(listOfLinks.get(i));
            }
            popupListView.setPrefHeight(popupListView.getItems().size() * 28);
            popupListView.setOnMouseClicked(innerEvent -> {
                Launcher.hostServices.showDocument("file:///" + lookupTable.get(popupListView.getSelectionModel().getSelectedItem()));
                popupListView.getScene().getWindow().hide();
            });
            Popup popup = new Popup();
            popup.getContent().add(popupListView);
            popup.setAutoHide(true);

            // Exercise 5  The application should be able to notify the user if the input currency short code is invalid
            //and let the user re-enter the new one.
            //Hint: a try-catch-finally block should be useful if you check what is returned from the api.
            popup.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ESCAPE)
                    popup.hide();
            });
            popup.show(Launcher.stage);
        });
    }
}