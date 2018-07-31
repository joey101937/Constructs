/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constructs;

import core.Coordinate;
import core.GameObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Basic building block for all constructs
 * @author Joseph
 */
public abstract class Block extends GameObject{
    //Technical Fields
    public static final int BLOCK_HEIGHT = 30, BLOCK_WIDTH = 30;
    public BufferedImage sprite;  //visual representation
    public Coordinate relativeLocation;    //X,Y about orgin block
    public boolean isOrgin;
    public Block[] connected = new Block[4];
    public Orientation orientation = Orientation.Up;
    public Construct parent; //what construct this block is a part of
    //Gameplay fields
    public int health = 10;
    public int initialHealth = 10;
    public int armor = 0;
    
    public Block(int x, int y) {
        super(x, y);
    }
    
    
    
    public void destroy(){
        super.destroy();
        parent.components.remove(this);
        //TODO
    }
    
    /**
     * Connects a block to this one
     * @param b the block to connect
     * @param i specifies the place to connect. 
     * 0 = topside
     * 1 = rightside
     * 2 = botside
     * 3 = leftside
     */
    public void Connect(int i, Block b){
        b.orientation = this.orientation; //orient them the same direction
        b.parent = this.parent;
        parent.addBlock(b);
        connected[i] = b;
        switch(i){
            case 0: //connecting block to our top/ their bot
                b.connected[2] = this;
                break;
            case 1:
                b.connected[3] = this;
                break;
            case 2:
                b.connected[0] = this;
                break;
            case 3: b.connected[1] = this;
        }
    }
    //tick
    public void tick(){
        
    }

    /**
     * renders the block on screen
     * @param g
     */
    @Override
    public void render(Graphics2D g){
        g.setColor(Color.red);
        g.fillRect(location.x - Block.BLOCK_WIDTH/2, location.y - Block.BLOCK_HEIGHT/2, Block.BLOCK_WIDTH, Block.BLOCK_HEIGHT);
    }
    
}
