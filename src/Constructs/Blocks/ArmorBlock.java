/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constructs.Blocks;

import Constructs.Block;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Joseph
 */
public class ArmorBlock extends Block{
    
    public ArmorBlock(int x, int y) {
        super(x, y);
         name = "armor block";
    }

    public ArmorBlock() {
       super();
       name = "armor block";
    }

    
    @Override
    public void render(Graphics2D g){
         g.setColor(Color.gray);
        g.fillRect(location.x - Block.BLOCK_WIDTH/2, location.y - Block.BLOCK_HEIGHT/2, Block.BLOCK_WIDTH, Block.BLOCK_HEIGHT);
    }
    
}
