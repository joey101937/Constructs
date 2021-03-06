/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constructs;

import Core.GameObject;
import Core.Coordinate;
import Core.Main;
import Core.SpriteManager;
import Core.Stickers.OnceThroughSticker;
import Core.Stickers.Sticker;
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
    public double fireInterval = 0.3; //how long a combat block will wait between firing IN SECONDS
    public boolean readyToFire = true;
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
    
    public void takeDamage(int amount){
        health-=amount;
        if(health<=0){
            destroy();
        }
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
        if(parent!=null)parent.components.remove(this);
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
    
 
    
    /**
     * Connects a block to this one
     * @param b the block to connect
     * @param i specifies the place to connect. 
     * 0 = topside
     * 1 = rightside
     * 2 = botside
     * 3 = leftside
     */
    public boolean connect(int i, Block b){
        if(b == this){
            System.out.println("warning: trying to connect block to itself. " + name);
            return false;
        }
        b.orientation = this.orientation; //orient them the same direction  
        b.location = new Coordinate(this.location);
        b.relativeLocation = new Coordinate(this.relativeLocation);
        System.out.println("positioning block to connect at " + b.name +" at " + location);
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
        if(!parent.relativeLocationClear(b.relativeLocation)){
            System.out.println("block in the way at r"+ b.relativeLocation); 
            return false;
        }
        connected[i] = b;
        parent.addBlock(b);
        System.out.println("new location for connected block " + b.location);
        b.connectSurrounding();
        return true;
    }

    /**
     * attempts to attach block to surrounding blocks of its construct, if able
     * based on position
     */
    public void connectSurrounding(){
        for(Block b : parent.components){
            if(b==null)continue;
            if(b.relativeLocation.x == relativeLocation.x+Block.BLOCK_WIDTH){
                //right column to us
                if(b.relativeLocation.y == relativeLocation.y){
                    //our right neighbor
                    b.connected[3] = this;
                    connected[1] = b;
                }
            }else if (b.relativeLocation.x == relativeLocation.x-Block.BLOCK_WIDTH){
                //left column to us
                if(b.relativeLocation.y == relativeLocation.y){
                    //our left neighbor
                    b.connected[1] = this;
                    connected[3] = b;
                }
            }else if(b.relativeLocation.x == relativeLocation.x){
                //same column as us
                if(b.relativeLocation.y == relativeLocation.y + Block.BLOCK_HEIGHT){
                    //lower neightbor
                    b.connected[0] = this;
                    connected[2] = b;
                }else if (b.relativeLocation.y == relativeLocation.y - Block.BLOCK_HEIGHT){
                    b.connected[2] = this;
                    connected[0] = b;
                }
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
    
    /**
     * draws line between center of this block and center of all connected blocks
     * @param g graphics object to use
     */
    public void renderLinks(Graphics2D g){
        Color original = g.getColor();
        g.setColor(Color.green);
        for(Block b : this.connected){
            if(b==null)continue;
            g.drawLine(location.x, location.y, b.location.x, b.location.y);
        }
        g.setColor(original);
    }
    
    /////////////////////////////////////gameplay methods
    public Projectile getProjectile(){
        return null;
    }
    
    public void shootAt(Coordinate point){
    }
    
    public void beginReload(){
        readyToFire=false;
        new reloadHelper(this);
    }
    
    public void onConnect(){
        //run after parent accepts the new block
    }

    public void onDestruction() {
    }
    
    public class reloadHelper implements Runnable{
        public Block host;
        public reloadHelper(Block host){
            this.host=host;
            Thread t = new Thread(this);
            t.start();
        }
        @Override
        public void run() {
            Main.wait((int)(host.fireInterval*1000));
            host.readyToFire=true;
        }
        
    }
   
}
