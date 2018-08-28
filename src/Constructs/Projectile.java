/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constructs;

import Core.Coordinate;
import Core.DCoordinate;
import Core.Game;
import Core.GameObject;
import java.awt.Graphics2D;

/**
 *
 * @author Joseph
 */
public class Projectile extends GameObject{
    public double slope; //projectile travels in a line with this slope 
    public int lifeTime = 500; //how many ticks this projectile has before it is destroyed automatically
    public double realVelX = 1.0;
    public double realVelY = 0.0;
    public DCoordinate realLocation;
    /**
     * Creates a projectile at a given coordinate, traveling at a specified angle
     */
    public Projectile(Coordinate spawnLocation, Coordinate targetPoint) {
        super(spawnLocation.x, spawnLocation.y);
        realLocation = new DCoordinate(location);
        speed = 2;
        double rise, run;
        run = Math.abs(targetPoint.x - location.x);
        rise = Math.abs(targetPoint.y - location.y);
        System.out.println(rise + "/" + run);
        double slope = rise / run;
        System.out.println("slope: " + slope);
        double travelVel = slope + 1;
        double desiredSpeed = speed;
        double delta = desiredSpeed / travelVel;
        realVelX = speed * delta;
        realVelY = speed * slope * delta;
        if (targetPoint.x < location.x) {
            realVelX *= -1;
        }
        if (targetPoint.y < location.y) {
            realVelY *= -1;
        }

    }

    @Override
    public void render(Graphics2D g) {
      g.fillOval(location.x-Block.BLOCK_WIDTH/4, location.y-Block.BLOCK_HEIGHT/4, Block.BLOCK_WIDTH/2, Block.BLOCK_WIDTH/2);
    }
    
    @Override
    public void adjustPositionForVelocity(){
         realLocation.x+=realVelX;
         realLocation.y+=realVelY;
         location = new Coordinate(realLocation);
        if(location.x<0 || location.x>Game.width || location.y<0 || location.y>Game.height){
            destroy();
            //destroy projectile if it goes out of bounds
        }
    }
}