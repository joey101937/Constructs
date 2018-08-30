/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constructs.Blocks;

import Constructs.Block;
import Constructs.Projectile;
import Constructs.Projectiles.Cannonball;
import Core.Coordinate;
import Core.Game;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Joseph
 */
public class CannonBlock extends Block implements ShootingBlock{
     public CannonBlock(int x, int y) {
        super(x, y);
         name = "Cannon block";
    }

    public CannonBlock() {
       super();
       name = "Cannon block";
    }

    
    @Override
    public void render(Graphics2D g){
        g.setColor(Color.darkGray);
        g.fillRect(location.x - Block.BLOCK_WIDTH/2, location.y - Block.BLOCK_HEIGHT/2, Block.BLOCK_WIDTH, Block.BLOCK_HEIGHT);
        g.setColor(Color.black);
         g.drawRect(location.x - Block.BLOCK_WIDTH/2, location.y - Block.BLOCK_HEIGHT/2, Block.BLOCK_WIDTH, Block.BLOCK_HEIGHT);
        renderLinks(g);
    }

    @Override
    public Projectile getProjectile() {
        Projectile output = new Cannonball(location);
        output.parent = this;
        return output;
    }

    @Override
    public void shootAt(Coordinate targetPoint) {
        Projectile p = getProjectile();
        p.launch(targetPoint);
        Game.handler.storage.add(p);
    }
}
