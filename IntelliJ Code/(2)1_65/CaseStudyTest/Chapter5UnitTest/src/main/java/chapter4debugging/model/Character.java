package chapter4debugging.model;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import chapter4debugging.Launcher;
import chapter4debugging.view.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;

public class Character extends Pane {

    public int CHARACTER_WIDTH;
    public static int CHARACTER_HEIGHT = 64;
    private Image characterImg;
    private AnimatedSprite imageView;
    private int x;
    private int y;

    private int startX , startY;
    private int offSetX , offSetY ;

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
    int collisionVal = 6;

    private int score = 0 ;
    private Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass()) ;
    //Logger logger = LoggerFactory.getLogger(Character.class);
    public Character(int x, int y, int offsetX, int offsetY, int xAccel, int XMaxVelo, int yAccel, int YMaxVelo, KeyCode leftKey, KeyCode rightKey, KeyCode upKey, String asset) {
        this.xAcceleration = xAccel;
        this.xMaxVelocity = XMaxVelo;
        this.yAcceleration = yAccel;
        this.yMaxVelocity = YMaxVelo;
        this.startX = x ;
        this.startY = y ;
        this.offSetX = offsetX ;
        this.offSetY = offsetY ;
        this.x = x;
        this.y = y;
        this.setTranslateX(x);
        this.setTranslateY(y);
        if (asset == "MarioSheet.png"){
            this.CHARACTER_WIDTH = 32;
            this.characterImg = new Image(Launcher.class.getResourceAsStream("assets/MarioSheet.png"));
            this.imageView = new AnimatedSprite(characterImg, 4, 4, 1, offsetX, offsetY, 16, 32);
            this.imageView.setFitWidth(CHARACTER_WIDTH);
            this.imageView.setFitHeight(CHARACTER_HEIGHT);
        }else{
            this.CHARACTER_WIDTH = 64;
            this.characterImg = new Image(Launcher.class.getResourceAsStream("assets/MegaManSprite.png"));
            this.imageView = new AnimatedSprite(characterImg, 5, 5, 1, offsetX, offsetY, 550, 450);
            this.imageView.setFitWidth(CHARACTER_WIDTH);
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

    public void collided(Character c) {
        if(isMoveLeft){
            x = c.getX() + CHARACTER_WIDTH + 1 ;
            stop();
        }else if (isMoveRight){
            x = c.getX() - CHARACTER_WIDTH - 1 ;
            stop();
        }

        if(y < Platform.GROUND - CHARACTER_HEIGHT){
            if(isFalling && y < c.getY() && (Math.abs(y-c.getY()) <= this.getCharacterHeight()+1) ){
                score++ ;
                y = Platform.GROUND - this.getCharacterHeight() - 5 ;
                c.collapsed() ;
                c.respawn() ;
            }
        }
    }

    public void respawn() {
        x = startX ;
        y = startY ;
        if(getCharacterHeight() == 32){
            imageView.setFitWidth(this.getCharacterWidth() + 32);
        }else{
            imageView.setFitWidth(this.getCharacterWidth());
        }

        imageView.setFitHeight(this.getCharacterHeight());
        isMoveRight = false ;
        isMoveLeft = false ;
        isFalling = true ;
        canJump = false ;
        isJumping = false ;
    }

    public void collapsed() {
        imageView.setFitHeight(5);
        y = Platform.GROUND - 5 ;
        repaint();

        try{
            TimeUnit.MILLISECONDS.sleep(500);
        }catch (InterruptedException e){
            throw  new RuntimeException(e);
        }

    }

    public  int getCharacterWidth() {
        return CHARACTER_WIDTH;
    }

    public  int getCharacterHeight() {
        return CHARACTER_HEIGHT;
    }

    public int getScore() {
        return score;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getOffSetX() {
        return offSetX;
    }

    public int getOffSetY() {
        return offSetY;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getyVelocity() {
        return yVelocity;
    }

    public int getxVelocity() {
        return xVelocity;
    }

    public int getxAcceleration() {
        return xAcceleration;
    }

    public int getyAcceleration() {
        return yAcceleration;
    }

    public int getxMaxVelocity() {
        return xMaxVelocity;
    }

    public int getyMaxVelocity() {
        return yMaxVelocity;
    }

    public boolean isFalling() {
        return isFalling;
    }

    public boolean isCanJump() {
        return canJump;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public boolean isMoveRight() {
        return isMoveRight;
    }

    public boolean isMoveLeft() {
        return isMoveLeft;
    }

    public void setMovingRight(boolean movingRight) {
        isMoveRight = movingRight;
    }

    public void setMovingLeft(boolean movingLeft) {
        isMoveLeft = movingLeft;
    }

    public void setFalling(boolean falling) {
        isFalling = falling;
    }

    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }
    public void setX(int x){
        this.x = x ;
    }
    public void setY(int y){
        this.y = y ;
    }

    public void onFloor(){
        this.y = Platform.GROUND;
        checkReachFloor();
    }
    public boolean checkReachGameWallBool() {
        return x != 0 || x != Platform.WIDTH - (int) getWidth();
    }

    public boolean canPassed() {
        return !checkReachGameWallBool();
    }

    public int getCollisionVal() {
        return collisionVal;
    }

    public void collidedTest(Character c) {
        x = c.getX() + collisionVal;
    }

    public boolean isStomped(Character c) {
        boolean check;
        // c2 stomp c1
        check = y < c.getY() && c.getY() - y <= CHARACTER_HEIGHT;
//        Math.abs(y - c.getY())
//        System.out.println(check);
//        System.out.println("y: " + y + ", c.getY: " + c.getY() + ", c.getY() - y: " + (c.getY() - y) + ", CHARACTER_HEIGHT " + CHARACTER_HEIGHT);
        c.repaint();
        c.collapsed();
        c.respawn();
//        System.out.println("y: " + y + ", c.getY: " + c.getY() + ", c.getY() - y: " + (c.getY() - y) + ", CHARACTER_HEIGHT " + CHARACTER_HEIGHT);
        return check;
    }

}