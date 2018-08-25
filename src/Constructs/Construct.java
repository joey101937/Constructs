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
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a structure made up of many blocks with a single important origin block
 * @author Joseph
 */
public class Construct {
    public ArrayList<Block> components = new ArrayList<Block>();
    public OriginBlock orgin;
    public Orientation orientation = Orientation.Up;
    public int velX, velY; //velocity of construct
    
    
    public void addBlock(Block b){
        if(!components.contains(b))components.add(b);
        b.parent=this;
        System.out.println("adding block at relative: " + b.relativeLocation);
        System.out.println("construct now has " + components.size() + " blocks");
    }
    
    public Construct(int x, int y){
        orgin = new OriginBlock(x,y);
        this.addBlock(orgin);
    }
    
    /**
     * Returns false if there is a block on the construct with a realative coord
     * equal to the given coordinate
     * @param c relative coordinate to check
     * @return if the position is free
     */
    public boolean relativeLocationClear(Coordinate c){
        for(Block b : components){
            if(c.equals(b.relativeLocation)){
                return false;
            }
        }
        return true;
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
    
    /**
     * removes blocks which have no possible block path to the orgin block
     */
    public void removeDetached() {
        System.out.println("removing detached");
        ArrayList<Block> approved = new ArrayList<>();
        approved.add(orgin);
        removalHelper(orgin, approved);
        System.out.println("approved: " + approved.size());
        components = approved;   //components now only has the approved blocks
    }
    /**
     * helper recursive method for removedDetached method
     * @param start block we are starting from
     * @param approved list of approved blocks
     */
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
