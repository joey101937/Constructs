/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constructs.Projectiles;

import Constructs.Block;
import Constructs.Projectile;
import Core.Coordinate;

/**
 *
 * @author Joseph
 */
public class Cannonball extends Projectile{
    
    public Cannonball(Coordinate spawnLocation, Coordinate targetPoint) {
        super(spawnLocation, targetPoint);
    }
    public Cannonball(Coordinate spawnLocation){
        super(spawnLocation);
    }
    
    @Override
    public void collide(Block b){
        if(b.parent==this.parent.parent && parent!=null){
            return; //dont affect out own construct's blocks
        }else{
            super.collide(b);
        }
    }
    
}
