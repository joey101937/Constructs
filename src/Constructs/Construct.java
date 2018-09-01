/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constructs;

import Constructs.Blocks.ArmorBlock;
import Constructs.Blocks.CannonBlock;
import Core.Game;
import Core.Coordinate;
import Constructs.Blocks.OriginBlock;
import Core.Main;
import Core.SpriteManager;
import Core.Stickers.OnceThroughSticker;
import Core.Stickers.Sticker;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
    public Rectangle collisionBox = new Rectangle();
    public ConstructAI ai = null;
    private int speed =1;
    
    public void addBlock(Block b){
        if(!components.contains(b))components.add(b);
        b.parent=this;
        b.speed=getSpeed();
        b.onConnect();
        System.out.println("adding block at relative: " + b.relativeLocation);
        System.out.println("construct now has " + components.size() + " blocks");
        updateBounds();
    }
    
    public void setSpeed(int i){
        speed = i;
        for(Block b : components){
            b.speed=i;
        }
    }
    
    public int getSpeed(){
        return speed;
    }
    
   /**
    * randomly adds a x number of basic armor blocks to the construct
    * @param numBlocks 
    */
    public void generateFiller(int numBlocks){
        int goal = components.size() + numBlocks;
        while(components.size() < goal){
            components.get(Main.generateRandom(0, components.size()-1)).connect(Main.generateRandom(0, 4), new ArmorBlock());
        }
    }
    
    public Construct(int x, int y){
        orgin = new OriginBlock(x,y);
        this.addBlock(orgin);
    }
    
    public static Construct generateRandomConstruct(Coordinate location, int numBlocks){
        Construct output = new Construct(location.x,location.y);
        int numWeapons = 0;
        while(output.components.size() < numBlocks){
             output.components.get(Main.generateRandom(0, output.components.size()-1)).connect(Main.generateRandom(0, 4), new ArmorBlock());
             if(Main.generateRandom(0, 7)>6 && numWeapons < 3){
                 output.components.get(Main.generateRandom(0, output.components.size()-1)).connect(Main.generateRandom(0, 4), new CannonBlock());
                 numWeapons++;
             }
         }
        return output;
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
        g.draw(collisionBox);
    }

    public void tick() {boolean colliding = false;
        Construct collision = null;
        for(Construct c : Game.handler.constructs){
            if(c==this)continue;
            if(c.collisionBox.intersects(collisionBox)){
                colliding = true;
                collision = c;
                System.out.println("collision");
                break;
            }
        }
        if (colliding) {
            if (collision.getLocation().x > getLocation().x && velX > 0) {
                realign();
                return;
            }
            if (collision.getLocation().x < getLocation().x && velX < 0) {
                realign();
                return;
            }
            if (collision.getLocation().y > getLocation().y && velY > 0) {
                realign();
                return;
            }
            if (collision.getLocation().y < getLocation().y && velY < 0) {
                realign();
                return;
            }
        }
        adjustPositionForVelocity();
        updateBounds();
        if(ai!=null){
            ai.tick();
        }
    }
    
    public void adjustPositionForVelocity(){
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
        for (Block b : components) {
            if (Main.generateRandom(0, 2) > 1) {
                System.out.println("test");
                Sticker s = new OnceThroughSticker(SpriteManager.explosionSequence, b.location, 5000);
            }
            b.onDestruction();

        }

        while (Game.handler.constructs.contains(this)) {
            try {
                Game.handler.constructs.remove(this);
            }catch(ConcurrentModificationException cme){
                System.out.println("cme while removing construct");
            }
        }
    }
    
    public void shootAt(Coordinate c){
        for(Block b : components){
            b.shootAt(c);
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
            System.out.println("new relative y of " + b.name + " is " + b.relativeLocation.y);
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
        updateBounds();
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
        int[] bounds = this.getBounds();
        collisionBox.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
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
        output[1] = orgin.location.y + topY;
        output[0] = orgin.location.x + leftX;
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
