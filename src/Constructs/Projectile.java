/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constructs;

import Core.Coordinate;
import Core.Game;
import Core.GameObject;
import Core.Main;
import java.awt.Graphics2D;

/**
 *
 * @author Joseph
 */
public class Projectile extends GameObject{
    public double slope; //projectile travels in a line with this slope 
    public int lifeTime = 500; //how many ticks this projectile has before it is destroyed automatically
    
    /**
     * Creates a projectile at a given coordinate, traveling at a specified angle
     */
    public Projectile(Coordinate spawnLocation, Coordinate targetPoint) {
        //TODO: FIX THIS MESS
        super(spawnLocation.x, spawnLocation.y);
        targetPoint = new Coordinate(targetPoint);
        //targetPoint.x -= location.x;
        //targetPoint.y -= location.y;
        double s = location.y-targetPoint.y/location.x-targetPoint.x;
        System.out.println((location.y-targetPoint.y) + " " + (location.x-targetPoint.x));
        this.slope = Math.abs((double)(location.y-targetPoint.y)/(double)(location.x-targetPoint.x));
        System.out.println("slope: " + slope);
        speed = 1; //test value
        velX = speed;
        velY = (int)(slope * speed);
    }

    @Override
    public void render(Graphics2D g) {
      g.fillOval(location.x-Block.BLOCK_WIDTH/4, location.y-Block.BLOCK_HEIGHT/4, Block.BLOCK_WIDTH/2, Block.BLOCK_WIDTH/2);
    }
    
    @Override
    public void adjustPositionForVelocity(){
         location.x+=velX;
         location.y+=velY;
        if(location.x<0 || location.x>Game.width || location.y<0 || location.y>Game.height){
            destroy();
            //destroy projectile if it goes out of bounds
        }
    }
}
