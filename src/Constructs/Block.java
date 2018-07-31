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
    public Coordinate relativeLocation = new Coordinate(0,0);    //X,Y about orgin block
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
    
    public Block(){
        super(0,0);
    }
    
    /**
     * connection to use when adding a block straight to another block
     * @param initialConnection block we attatch to
     * @param side side of that other block we attach to
     */
    public Block(Block initialConnection, int side){
        super(0,0);
        initialConnection.Connect(side, this);
    }
    
    
    
    public void destroy(){
        onDestruction();
        super.destroy();
        parent.components.remove(this);
        for(Block b : connected){
            for(Block b2 : b.connected){
                if(b2 == this){
                    b2 = null;      //removes this from the connection of other blocks
                }
            }
        }
    }
    
    public void onDestruction(){
        
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
        if(b == this){
            System.out.println("warning: trying to connect block to itself. " + name);
            return;
        }
        b.orientation = this.orientation; //orient them the same direction
        parent.addBlock(b);
        connected[i] = b;
        b.location = new Coordinate(this.location);
        b.relativeLocation = new Coordinate(this.relativeLocation);
        System.out.println("connecting " + b.name +" at " + location);
        switch(i){
            case 0: //connecting block to our top
                b.connected[2] = this;
                b.location.y -= Block.BLOCK_HEIGHT;
                b.relativeLocation.y -= Block.BLOCK_HEIGHT;
                break;
            case 1: //to our right side
                 b.location.x += Block.BLOCK_WIDTH;
                b.relativeLocation.x += Block.BLOCK_WIDTH;
                b.connected[3] = this;
                break;
            case 2: //to our bot side
                b.connected[0] = this;
                b.location.y += Block.BLOCK_HEIGHT;
                b.relativeLocation.y += Block.BLOCK_HEIGHT;
                break;
            case 3: //to our left side
                b.connected[1] = this;
                b.location.x -= Block.BLOCK_WIDTH;
                b.relativeLocation.x -= Block.BLOCK_WIDTH;
                break;
        }
        System.out.println("new location for connected block " + b.location);
    }

    //tick
    public void tick() {
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
