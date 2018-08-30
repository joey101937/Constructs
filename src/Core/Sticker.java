/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Joseph
 */
public class Sticker implements Runnable{
    public BufferedImage image;
    public Coordinate location = new Coordinate(0,0);
    public boolean disabled = false;
    public int timeToRender;

    
    public Sticker(BufferedImage i, Coordinate c, int duration){
        image = i;
        location = c;
        timeToRender = duration;
        Game.visHandler.stickers.add(this);
        Thread t = new Thread(this);
        t.start();
    }

    protected void render(Graphics2D g) {
        if (location.x < 0 || location.y < 0) {
            disable();     //if the coordinates are bad, dont render
        }
        if (!disabled) {
            if (image != null) {
                g.drawImage(image, location.x, location.y, null);
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
