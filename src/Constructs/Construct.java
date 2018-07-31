/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constructs;

import Constructs.Blocks.OriginBlock;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author Joseph
 */
public class Construct {
    public ArrayList<Block> components = new ArrayList<Block>();
    public OriginBlock orgin;
    public Orientation orienation = Orientation.Up;
    public void addBlock(Block b){
        components.add(b);
    }
    
    public Construct(int x, int y){
        orgin = new OriginBlock(x,y);
        components.add(orgin);
    }
    
    public void render(Graphics2D g){
        for(Block b : components){
            b.render(g);
        }
    }
    
    public void tick(){
        for(Block b : components){
            b.tick();
        }
    }
    
}
