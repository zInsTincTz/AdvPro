package chapter4debugging.controller;

import chapter4debugging.view.Platform;
import chapter4debugging.model.Character;

import java.util.ArrayList;

public class DrawingLoop implements Runnable {
    private Platform platform;
    private int frameRate;
    private float interval;
    private boolean running;

    public DrawingLoop(Platform platform) {
        this.platform = platform;
        frameRate = 30;
        interval = 1000.0f / frameRate; // 1000 ms = 1 second
        running = true;
    }

    private void checkDrawCollisions(ArrayList<Character> characters){
        for(Character character : characters){
            character.checkReachGameWall();
            character.checkReachHighest();
            character.checkReachFloor();
        }

        for (Character cA : characters){
            for(Character cB: characters){
                if(cA != cB){
                    if(cA.getBoundsInParent().intersects(cB.getBoundsInParent())){
                        cA.collided(cB);
                        cB.collided(cA);
                        return;
                    }
                }
            }
        }
    }

    private void paint(ArrayList<Character> characters){
        for(Character character : characters){
            character.repaint();
        }
    }

    @Override
    public void run() {
        while (running) {
            float time = System.currentTimeMillis();
            checkDrawCollisions(platform.getCharacters());
            paint(platform.getCharacters());
            /*
            checkDrawCollisions(platform.getCharacter());
            paint(platform.getCharacter());
            checkDrawCollisions(platform.getCharacter2());
            paint(platform.getCharacter2());
            */
            time = System.currentTimeMillis() - time;
            if (time < interval) {
                try {
                    Thread.sleep((long) (interval - time));
                } catch (InterruptedException e) {
                }
            } else {
                try {
                    Thread.sleep((long) (interval - (interval % time)));
                } catch (InterruptedException e) {
                }
            }
        }
    }
}