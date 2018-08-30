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
public class AnimatedSticker extends Sticker{
    public BufferedImage[] sprites;
    public int frameCount = 0;
    public int currentFrame = 0;
    public int frameDuration = 40;
    
    public AnimatedSticker(BufferedImage[] i, Coordinate c, int duration) {
        super(null, c, duration);
        sprites = i;
        frameCount = i.length;
        new AnimationHelper(this);
    }

    @Override
    protected void render(Graphics2D g) {
        image = sprites[currentFrame];
        if (location.x < 0 || location.y < 0) {
            disable();     //if the coordinates are bad, dont render
        }
        if (!disabled) {
            if (image != null) {
                g.drawImage(image, location.x, location.y, null);
            }
        }
    }

    public class AnimationHelper implements Runnable {

        public AnimatedSticker host;

        public AnimationHelper(AnimatedSticker as){
            host = as;
            Thread t = new Thread(this);
            t.start();
        }

        @Override
        public void run() {
           while(!host.disabled){
               Main.wait(host.frameDuration);
               currentFrame++;
               if(currentFrame>=frameCount){
                   currentFrame=0;
               }
           }
        }
    }
    
}
