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
import java.util.HashMap;
import java.util.Map;

/**
 * Basic building block for all constructs
 * @author Joseph
 */
public abstract class Block extends GameObject{
    //Technical Fields
    public static final int BLOCK_HEIGHT = 30, BLOCK_WIDTH = 30;
    public Map<Orientation,BufferedImage> sprite = new HashMap<Orientation,BufferedImage>();  //visual representation
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
        initialConnection.connect(side, this);
    }
    
    
    
    @Override
    public synchronized void destroy(){
        onDestruction();
        super.destroy();
        parent.components.remove(this);
        for(Block b : connected){
            if(b==null)continue;
            for(int i = 0; i<4; i++){
                if(b.connected[i]==null)continue;
                if(b.connected[i] == this){
                    System.out.println("removing self from connection of " + b.name);
                    b.connected[i] = null;      //removes this from the connection of other blocks
                }
            }
            b=null;
        }
        if(parent!=null)parent.removeDetached();
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
    public void connect(int i, Block b){
        if(b == this){
            System.out.println("warning: trying to connect block to itself. " + name);
            return;
        }
        if(orientation == Orientation.Down && (i == 2 || i == 0)){
            if(i == 2) i = 0;
            else if (i==0) i = 2;
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
        b.connectSurrounding();
    }

    /**
     * attempts to attach block to surrounding blocks of its construct, if able
     * based on position
     */
    public void connectSurrounding(){
        
        for(Block b : parent.components){
            boolean alreadyConnected = false;
            for(Block c : connected){
                if(b==c){
                    alreadyConnected = true;
                    System.out.println("already connected");
                    break;
                }
            }
            if(alreadyConnected)continue;
            if(b.relativeLocation.x + Block.BLOCK_WIDTH == relativeLocation.x && b.relativeLocation.y == relativeLocation.y) {
                //left of us
                b.connected[1]=this;
                connected[3]=b;
            }
            if (b.relativeLocation.x - Block.BLOCK_WIDTH == relativeLocation.x && b.relativeLocation.y == relativeLocation.y) {
                //right of us
                b.connected[3] =this;
                connected[1]=b;
            }
            if (b.relativeLocation.y + Block.BLOCK_HEIGHT == relativeLocation.y && b.relativeLocation.x == relativeLocation.x) {
                //below us
                b.connected[0] = this;
                connected[2]=b;
            }
            if (b.relativeLocation.y - Block.BLOCK_HEIGHT == relativeLocation.y && b.relativeLocation.x == relativeLocation.x) {
                //above us
                b.connected[2] = this;
                connected[0]= b;
            }
        }
    }

    //tick
    public void tick() {
        adjustPositionForVelocity();
    }

    /**
     * renders the block on screen
     * @param g
     */
    @Override
    public void render(Graphics2D g){
        g.setColor(Color.red);
        g.fillRect(location.x - Block.BLOCK_WIDTH/2, location.y - Block.BLOCK_HEIGHT/2, Block.BLOCK_WIDTH, Block.BLOCK_HEIGHT);
        renderLinks(g);
    }
    public void renderLinks(Graphics2D g){
        Color original = g.getColor();
        g.setColor(Color.BLACK);
        for(Block b : this.connected){
            if(b==null)continue;
            g.drawLine(location.x, location.y, b.location.x, b.location.y);
        }
        g.setColor(original);
    }
}
