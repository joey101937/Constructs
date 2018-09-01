/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constructs.Projectiles;

import Constructs.Block;
import Constructs.Projectile;
import Core.Coordinate;
import Core.Input;
import Core.SpriteManager;
import Core.Stickers.OnceThroughSticker;
import Core.Stickers.Sticker;

/**
 *
 * @author Joseph
 */
public class Cannonball extends Projectile{
    
    public Cannonball(Coordinate spawnLocation, Coordinate targetPoint) {
        super(spawnLocation, targetPoint);
        scaleSpeed(.5);
    }
    public Cannonball(Coordinate spawnLocation){
        super(spawnLocation);
        scaleSpeed(.5);
    }
    
    @Override
    public void collide(Block b){
        if(b.parent==this.parent.parent && parent!=null){
            return; //dont affect out own construct's blocks
        }else{
            for(Block b2 : Input.getNearbyBlocks(b.location, 30)){
                System.out.println("damage on block " + b);
                b2.takeDamage(damage);
            }
            OnceThroughSticker s = new OnceThroughSticker(SpriteManager.explosionSequence,b.location,5000);
            this.destroy();
        }
    }
    @Override
    public void tick(){
        super.tick();
       // realVelY+=.1; //gravity for firing arch if desired
    }
}
