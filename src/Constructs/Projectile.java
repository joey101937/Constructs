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
import Core.Input;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Joseph
 */
public class Projectile extends GameObject{
    public double slope; //projectile travels in a line with this slope 
    public int lifeTime = 500; //how many ticks this projectile has before it is destroyed automatically
    public double realVelX = 0.0;
    public double realVelY = 0.0;
    public DCoordinate realLocation;
    public Block parent = null; //block that created this projectile, if exists
    public int damage = 2;
    private Coordinate target = new Coordinate(0,0);
    /**
     * Creates a projectile at a given coordinate, traveling at a specified angle
     */
    public Projectile(Coordinate spawnLocation, Coordinate targetPoint) {
        super(spawnLocation.x, spawnLocation.y);
        realLocation = new DCoordinate(location);
        launch(targetPoint);
    }
    
    public Projectile(Coordinate spawnLocation){
        super(spawnLocation.x,spawnLocation.y);
        realLocation = new DCoordinate(location);
    }
    

    public void launch(Coordinate targetPoint) {
        speed = 3;
        target = targetPoint;
        double rise, run;
        run = Math.abs(targetPoint.x - location.x);
        rise = Math.abs(targetPoint.y - location.y);
        System.out.println(rise + "/" + run);
        double slope = rise / run;
        System.out.println("slope: " + slope);
        double travelVel = slope + 1; //how many units it would move per tick
        double desiredSpeed = speed;  //how fast we want it to go
        double delta = desiredSpeed / travelVel; //multiply by this to keep ratio of rise/run but constrain to desired speed
        realVelX = speed * delta;
        realVelY = speed * slope * delta;
        if (targetPoint.x < location.x) {
            realVelX *= -1;
        }
        if (targetPoint.y < location.y) {
            realVelY *= -1;
        }
    }
    /**
     * Scales projectile speed be this much
     * @param i multiplier
     */
    public void scaleSpeed(double i){
        realVelX *= i;
        realVelY *= i;
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.red);
        g.fillOval(location.x - Block.BLOCK_WIDTH/4, location.y-Block.BLOCK_HEIGHT/4, Block.BLOCK_WIDTH/2, Block.BLOCK_WIDTH/2);
        Rectangle r = new Rectangle();
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
    
    @Override
    public void tick(){
        adjustPositionForVelocity();
        Block b = Input.getBlockAt(location.x, location.y);
        if(b !=null){
           collide(b);
        }
    }
    
    public void collide(Block b) {
        b.takeDamage(damage);
        this.destroy();
    }
}
