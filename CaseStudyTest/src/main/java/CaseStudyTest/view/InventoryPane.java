package CaseStudyTest.view;

import CaseStudyTest.Launcher;
import CaseStudyTest.controller.AllCustomHandler;
import CaseStudyTest.model.item.BasedEquipment;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class InventoryPane extends ScrollPane {
    private ArrayList<BasedEquipment> equipmentsArray ;
    public InventoryPane() {}

    private Pane getDetailPane() {
        Pane inventoryInfoPane = new HBox(10);
        inventoryInfoPane.setBorder(null);
        inventoryInfoPane.setPadding(new Insets(25,25,25,25));

        if(equipmentsArray != null ) {

            ImageView[] imageViewsList = new ImageView[equipmentsArray.size()] ;
            for(int i = 0 ; i < equipmentsArray.size() ; i++) {
                imageViewsList[i] = new ImageView() ;

                imageViewsList[i].setImage(new Image(Launcher.class.getResource(equipmentsArray.get(i).getImgpath()).toString()));

                int finalI = i ;
                imageViewsList[i].setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        AllCustomHandler.onDragDetected(event , equipmentsArray.get(finalI) , imageViewsList[finalI]);
                    }
                });
                imageViewsList[i].setOnDragDone(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        AllCustomHandler.onEquipDone(event);
                        //is drag done
                        if(!event.isAccepted()){
                            AllCustomHandler.onDragFailed(event);
                        }
                    }
                });
            }
            inventoryInfoPane.getChildren().addAll(imageViewsList) ;
        }
        return inventoryInfoPane ;
    }

    public void drawPane(ArrayList<BasedEquipment> equipmentArray) {
        this.equipmentsArray = equipmentArray ;
        Pane inventoryInfo = getDetailPane() ;
        this.setStyle("-fx-background-color:Green;");
        this.setContent(inventoryInfo);
    }
}