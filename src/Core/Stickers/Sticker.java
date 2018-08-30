/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core.Stickers;

import Core.Coordinate;
import Core.Game;
import Core.Main;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Renders an image at a location for a given length of time
 * @author Joseph
 */
public class Sticker implements Runnable{
    public BufferedImage image;
    public Coordinate spawnLocation = new Coordinate(0,0);
    protected Coordinate renderLocation = new Coordinate(0,0);
    public boolean disabled = false;
    public int timeToRender;

    /**
     * @param i Image to display
     * @param c location to display
     * @param duration how long to display
     */
    public Sticker(BufferedImage i, Coordinate c, int duration){
        image = i;
        spawnLocation = c; //where we want the sticker
        timeToRender = duration; //topleft location of sticker used to put center on spawnLocation
        Game.visHandler.stickers.add(this);
        Thread t = new Thread(this);
        t.start();
    }
    /**
     * calibrates the render location to center the image on spawn location
     * @param toRender 
     */
    protected void centerCoordinate(BufferedImage toRender) {
        try{
        renderLocation.x = spawnLocation.x - toRender.getWidth() / 2;
        renderLocation.y = spawnLocation.y - toRender.getHeight() / 2;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public synchronized void render(Graphics2D g) {
        centerCoordinate(image);
        if (spawnLocation.x < 0 || spawnLocation.y < 0) {
            disable();     //if the coordinates are bad, dont render
        }
        if (!disabled) {
            if (image != null) {
                g.drawImage(image, renderLocation.x, renderLocation.y, null);
            }
        }
    }

    public void disable() {
        disabled = true;
    }

    @Override
    public void run() {
        Main.wait(timeToRender);
        disable();
    }

}
