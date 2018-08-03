/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constructs;

import Constructs.Blocks.OriginBlock;
import core.Coordinate;
import core.Game;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author Joseph
 */
public class Construct {
    public ArrayList<Block> components = new ArrayList<Block>();
    public OriginBlock orgin;
    public Orientation orientation = Orientation.Up;
    public int velX, velY; //velocity of construct
    
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
            b.velX = velX;
            b.velY = velY;
            b.tick();
        }
    }
    
    public Coordinate getLocation(){
     return orgin.location;
    }
    
    
    public void removeDetached() {
        System.out.println("removing detached");
        ArrayList<Block> approved = new ArrayList<>();
        approved.add(orgin);
        removalHelper(orgin, approved);
        System.out.println("approved: " + approved.size());
        components = approved;
    }

    private void removalHelper(Block start, ArrayList<Block> approved){
        for(Block b : start.connected){
            if(b==null)continue;
            if(!approved.contains(b)){
                approved.add(b);
                removalHelper(b, approved);
            }
        }
    }
    
    
    //removes construct from game
    public void destroy(){
        System.out.println("destroy construct run");
        
        //TODO
    }
    
    /**
     * flips the ship virtically
     */
    public void flipVirtically(){
        int axis = orgin.location.y;
        for(Block b : components){
            int formerY = b.location.y;
            int newOffset = (formerY - axis) * -1;
            b.location.y = axis + newOffset;
            b.relativeLocation.y*=-1;
            Block temp = b.connected[0];
            b.connected[0] = b.connected[2];
            b.connected[2]=temp; //topside attach and botside attach swap
            if(b.orientation == Orientation.Up){
                b.orientation = Orientation.Down;
            }else{
                b.orientation = Orientation.Up;
            }
        }
        if (this.orientation == Orientation.Up) {
            this.orientation = Orientation.Down;
        } else {
            this.orientation = Orientation.Up;
        }
    }

}
