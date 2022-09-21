package chapter4debugging.model;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import chapter4debugging.Launcher;
import chapter4debugging.view.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Character extends Pane {
    Logger logger = LoggerFactory.getLogger(Character.class);
    public int CHARACTER_WIDTH = 32;
    public int CHARACTER_HEIGHT = 64;
    private Image characterImg;
    private AnimatedSprite imageView;
    private int x;
    private int y;
    private KeyCode leftKey;
    private KeyCode rightKey;
    private KeyCode upKey;
    int xVelocity = 0;
    int yVelocity = 0;
    int xAcceleration;
    int yAcceleration;
    int xMaxVelocity;
    int yMaxVelocity;
    boolean isMoveLeft = false;
    boolean isMoveRight = false;
    boolean isFalling = true;
    boolean canJump = false;
    boolean isJumping = false;

    public Character(int x, int y, int offsetX, int offsetY, int xAccel, int XMaxVelo, int yAccel, int YMaxVelo, KeyCode leftKey, KeyCode rightKey, KeyCode upKey, String asset) {
        this.xAcceleration = xAccel;
        this.xMaxVelocity = XMaxVelo;
        this.yAcceleration = yAccel;
        this.yMaxVelocity = YMaxVelo;
        this.x = x;
        this.y = y;
        this.setTranslateX(x);
        this.setTranslateY(y);
        if (asset == "MarioSheet.png"){
            this.characterImg = new Image(Launcher.class.getResourceAsStream("assets/MarioSheet.png"));
            this.imageView = new AnimatedSprite(characterImg, 4, 4, 1, offsetX, offsetY, 16, 32);
            this.imageView.setFitWidth(CHARACTER_WIDTH);
            this.imageView.setFitHeight(CHARACTER_HEIGHT);
        }else{
            this.characterImg = new Image(Launcher.class.getResourceAsStream("assets/MegaManSprite.png"));
            this.imageView = new AnimatedSprite(characterImg, 5, 5, 1, offsetX, offsetY, 550, 450);
            this.imageView.setFitWidth(CHARACTER_WIDTH + 32);
            this.imageView.setFitHeight(CHARACTER_HEIGHT);
        }
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.upKey = upKey;
        this.getChildren().addAll(this.imageView);
    }

    public void checkReachGameWall() {
        if (x <= 0) {
            x = 0;
        } else if (x + getWidth() >= Platform.WIDTH) {
            x = Platform.WIDTH - (int) getWidth();
        }
    }

    public void jump() {
        if (canJump) {
            yVelocity = yMaxVelocity;
            canJump = false;
            isJumping = true;
            isFalling = false;
        }
    }

    public void checkReachHighest() {
        if (isJumping && yVelocity <= 0) {
            isJumping = false;
            isFalling = true;
            yVelocity = 0;
        }
    }

    public void checkReachFloor() {
        if (isFalling && y >= Platform.GROUND - CHARACTER_HEIGHT) {
            isFalling = false;
            canJump = true;
            yVelocity = 0;
        }
    }

    public void moveLeft() {
        isMoveLeft = true;
        isMoveRight = false;
    }

    public void moveRight() {
        isMoveLeft = false;
        isMoveRight = true;
    }

    public void stop() {
        isMoveLeft = false;
        isMoveRight = false;
    }

    public void moveX() {
        setTranslateX(x);
        if (isMoveLeft) {
            xVelocity = xVelocity >= xMaxVelocity ? xMaxVelocity : xVelocity + xAcceleration;
            x = x - xVelocity;
        }
        if (isMoveRight) {
            xVelocity = xVelocity >= xMaxVelocity ? xMaxVelocity : xVelocity + xAcceleration;
            x = x + xVelocity;
        }
    }

    public void moveY() {
        setTranslateY(y);
        if (isFalling) {
            yVelocity = yVelocity >= yMaxVelocity ? yMaxVelocity : yVelocity + yAcceleration;
            y = y + yVelocity;
        } else if (isJumping) {
            yVelocity = yVelocity <= 0 ? 0 : yVelocity - yAcceleration;
            y = y - yVelocity;
        }
    }

    public void repaint() {
        moveX();
        moveY();
    }

    public void trace() {
        logger.info("x:{} y:{} VeloX:{} VeloY:{}", x, y, xVelocity, yVelocity);
    }

    public KeyCode getLeftKey() {
        return leftKey;
    }

    public KeyCode getRightKey() {
        return rightKey;
    }

    public AnimatedSprite getImageView() {
        return imageView;
    }

    public KeyCode getUpKey() {
        return upKey;
    }
}