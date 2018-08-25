/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constructs;

import Constructs.Blocks.OriginBlock;
import Core.Coordinate;
import Core.Game;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

/**
 * Represents a structure made up of many blocks with a single important origin block
 * @author Joseph
 */
public class Construct {
    public ArrayList<Block> components = new ArrayList<Block>();
    public OriginBlock orgin;
    public Orientation orientation = Orientation.Up;
    public int velX, velY; //velocity of construct
    public int topY, botY, rightX, leftX; //topmost block's y coord, bottom most block's y, rightmost block's x and leftmost block's x. used for bounds
    public void addBlock(Block b){
        if(!components.contains(b))components.add(b);
        b.parent=this;
        System.out.println("adding block at relative: " + b.relativeLocation);
        System.out.println("construct now has " + components.size() + " blocks");
        updateBounds();
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
       renderBounds(g);  
    }
    
    private void renderBounds(Graphics2D g){
        g.setColor(Color.blue);
        g.drawRect(orgin.location.x + leftX, orgin.location.y + topY, Math.abs(rightX) + Math.abs(leftX), Math.abs(topY) + Math.abs(botY));
    }

    public void tick() {
        for(Block b : components){
            if(velX < 0){
                if(getCenter().x+leftX<=0){
                    realign();
                   return;
                }
            } else if (velX > 0){
                if (getCenter().x + rightX >= Game.width) {
                    realign();
                    return;
                }
            }
            if(velY < 0 ){
                if(getCenter().y + topY <= 0){
                    realign();
                    return;
                }
            }else if(velY > 0){
                if(getCenter().y + botY >= Game.height){
                    realign();
                    return;
                }
            }
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
        updateBounds();
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
        System.out.println("destroy construct");
        for(Block b : components){
            b.onDestruction();
        }
        while(Game.handler.constructs.contains(this)){
            try{
            Game.handler.constructs.remove(this);
            }catch(ConcurrentModificationException cme){
                System.out.println("cme while removing construct");
            }
        }
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
    
    /**
     * changes the topmost x and y coords and left and rightmost coords to reflect the bounds of the entire construct
     */
    private void updateBounds(){
        int top = 0, bot=0, left = 0, right = 0;
        for(Block b : components){
            if(b.relativeLocation.y < top){
                //block is above current topmost coord
                top=b.relativeLocation.y;
            }else if (b.relativeLocation.y > bot){
                //below Bottom
                bot=b.relativeLocation.y;
            }
            if(b.relativeLocation.x > right){
                right = b.relativeLocation.x;
            }else if (b.relativeLocation.x < left){
                left = b.relativeLocation.x;
            }
        }
        top-=Block.BLOCK_HEIGHT/2;
        bot+=Block.BLOCK_HEIGHT/2;
        left-=Block.BLOCK_WIDTH/2;
        right+=Block.BLOCK_WIDTH/2;
        topY = top;
        botY = bot ;
        leftX = left ;
        rightX = right ;
    }
    
    /**
     * returns an array of length 4 with the following int numbers
     * 0- topmost point Y coord
     * 1- leftmost point X coord
     * 2- total width
     * 3- total height
     * @return 
     */
    public int[] getBounds(){
        int[] output = new int[4];
        output[0] = orgin.location.y + topY;
        output[1] = orgin.location.x + leftX;
        output[2] = Math.abs(rightX) + Math.abs(leftX);
        output[3] = Math.abs(topY) + Math.abs(botY);
        return output;
    }
    
    /**
     * returns the coordinate for the orgin of this construct
     */
    public Coordinate getCenter(){
        return orgin.location;
    }
    
    /**
     * resets the position of all blocks relative to this construct
     * useful if blocks were hit against somethjing and are slightly off position
     */
    public void realign(){
        for(Block b : components){
            b.location=Coordinate.sum(getCenter(), b.relativeLocation);
        }
    }

}
