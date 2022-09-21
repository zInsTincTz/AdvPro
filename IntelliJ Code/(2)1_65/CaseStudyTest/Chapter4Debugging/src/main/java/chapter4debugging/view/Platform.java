package chapter4debugging.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import chapter4debugging.Launcher;
import chapter4debugging.model.Character;
import chapter4debugging.model.Keys;

public class Platform extends Pane {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 400;
    public final static int GROUND = 300;
    private Image platformImg;
    private Character character;
    private Character character2;
    private Keys keys;
    public Platform() {
        keys = new Keys();
        platformImg = new Image(Launcher.class.getResourceAsStream("assets/Background.png"));
        ImageView backgroundImg = new ImageView(platformImg);
        backgroundImg.setFitHeight(HEIGHT);
        backgroundImg.setFitWidth(WIDTH);
        character = new Character(30, 30, 0, 0, 1, 7, 1, 17, KeyCode.A, KeyCode.D, KeyCode.W, "MarioSheet.png");
        character2 = new Character(600, 30, 0, 0, 1, 10, 1, 20, KeyCode.LEFT, KeyCode.RIGHT, KeyCode.UP, "MegaManSheet.png");
        getChildren().addAll(backgroundImg,character,character2);
    }

    public Character getCharacter() {
        return character;
    }

    public Character getCharacter2() {
        return character2;
    }



    public Keys getKeys() {
        return keys; }
}