/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constructs;

import Constructs.Blocks.OriginBlock;
import core.Coordinate;
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
        b.parent=this;
    }
    
    public Construct(int x, int y){
        orgin = new OriginBlock(x,y);
        this.addBlock(orgin);
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
    
    public Coordinate getLocation(){
     return orgin.location;
    }
    
}
