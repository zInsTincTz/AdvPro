package chapter4debugging.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import chapter4debugging.Launcher;
import chapter4debugging.model.Character;
import chapter4debugging.model.Keys;

import java.util.ArrayList;

public class Platform extends Pane {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 400;
    public final static int GROUND = 300;
    private Image platformImg;
    //private Character character1;
    //private Character character2;
    private Keys keys;

    private ArrayList<Character> characters ;
    private ArrayList<Score> scoreList ;
    public Platform() {

        this.characters = new ArrayList<>() ;
        this.scoreList = new ArrayList<>() ;

        this.keys = new Keys();
        platformImg = new Image(Launcher.class.getResourceAsStream("assets/Background.png"));
        ImageView backgroundImg = new ImageView(platformImg);
        backgroundImg.setFitHeight(HEIGHT);
        backgroundImg.setFitWidth(WIDTH);

        Character character1 = new Character(30, 30, 0, 0, 1, 7, 1, 17, KeyCode.A, KeyCode.D, KeyCode.W, "MarioSheet.png");
        Character character2 = new Character(30/*720*/, 30, 0, 0, 1, 10, 1, 20, KeyCode.LEFT, KeyCode.RIGHT, KeyCode.UP, "MegaManSheet.png");

        characters.add(character1);
        characters.add(character2);

        this.getChildren().addAll(backgroundImg);

        for(Character ch : characters){
            this.getChildren().add(ch);
        }
        scoreList.add(new Score(30 , GROUND + 30));
        scoreList.add(new Score(Platform.WIDTH - 60 , GROUND + 30));
        scoreList.forEach( list -> { this.getChildren().add(list) ; });

    }

    public ArrayList<Character> getCharacters() {return  this.characters ; }

    /*
    public Character getCharacter() {
        return character1;
    }
    public Character getCharacter2() {
        return character2;
    }
    */

    public ArrayList<Score> getScoreList() {
        return scoreList;
    }


    public Keys getKeys() {
        return keys; }
}