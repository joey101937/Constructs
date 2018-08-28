package Core;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import Constructs.Block;
import Constructs.Blocks.ArmorBlock;
import Constructs.Construct;
import Constructs.Projectile;
import static Core.Game.handler;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author Joseph
 */
public class Input implements KeyListener, MouseListener{
    //FIELDS
    public Game hostGame;
    public Input(Game x){
        hostGame = x;
    }
    
    
    
     @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (getBlockAt(e.getX(), e.getY()) != null) {
            System.out.println("you clicked on a block");
            if(e.getButton() == 3){ //rightclick
                getBlockAt(e.getX(), e.getY()).destroy();
            }
        } else if (e.getButton() == 1) {
            if (!attemptToBuild(e.getX(), e.getY())) { 
                //if we didnt build a block with the click, continue with projectile testing
                System.out.println("projectile test");
                Projectile p = new Projectile(new Coordinate(300, 300), new Coordinate(e.getX(), e.getY()));
                handler.storage.add(p);
            }
        }
    }

    /**
     * attempts to place a block at a given location and connect to nearest
     * construct
     * @return weather or not a block was created and attached
     */
    private static boolean attemptToBuild(int x, int y) {
        Block closest = Input.nearestBlock(x, y);
        int requiredProximity = 60;
        if (Coordinate.distanceBetween(new Coordinate(x, y), closest.location) <= requiredProximity) {
            System.out.println("close");
            if(y < closest.location.y - Block.BLOCK_HEIGHT/2 && x > closest.location.x-Block.BLOCK_WIDTH/2 && x < closest.location.x+Block.BLOCK_WIDTH/2){
                System.out.println("build up");
                closest.connect(0, new ArmorBlock());
                return true;
            }
            if (y > closest.location.y - Block.BLOCK_HEIGHT / 2 && x > closest.location.x - Block.BLOCK_WIDTH / 2 && x < closest.location.x + Block.BLOCK_WIDTH / 2) {
                System.out.println("build down");
                closest.connect(2, new ArmorBlock());
                return true;
            }
            if (x > closest.location.x + Block.BLOCK_HEIGHT / 2 && y > closest.location.y - Block.BLOCK_WIDTH / 2 && y < closest.location.y + Block.BLOCK_WIDTH / 2) {
                System.out.println("build right");
                closest.connect(1, new ArmorBlock());
                return true;
            }
            if (x < closest.location.x + Block.BLOCK_HEIGHT / 2 && y > closest.location.y - Block.BLOCK_WIDTH / 2 && y < closest.location.y + Block.BLOCK_WIDTH / 2) {
                System.out.println("build left");
                closest.connect(3, new ArmorBlock());
                return true;
            }
        }
        return false;
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
      
    }
    
    
    @Override
    public void keyTyped(KeyEvent e) {
        
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        ArmorBlock next;
        switch(e.getKeyCode()){
            case 'W':
               Game.testConstruct.velY = -1;
                break;
            case 'S':
                Game.testConstruct.velY = 1;
                break;
            case 'A':
                Game.testConstruct.velX = -1;
                break;
            case 'D':
                Game.testConstruct.velX = 1;
                break;
            case 'Z':
                Game.testConstruct.orgin.connected[0].destroy();
                break;
            case 'T': //connect up of last one
                 next = new ArmorBlock();
                if(Game.testBlock.connect(0, next)){
                    Game.testBlock = next;
                }else{
                    next.destroy();
                }
                break;
             case 'H': //right of last
                next = new ArmorBlock();
                if (Game.testBlock.connect(1, next)){
                    Game.testBlock = next;
                }else{
                    next.destroy();
                }
                break;
            case 'F': //left of last
                next = new ArmorBlock();
                if(Game.testBlock.connect(3, next)){
                      Game.testBlock = next;
                } else{
                    next.destroy();
                }            
                break;
            case 'G': //below of last
                next = new ArmorBlock();
                if (Game.testBlock.connect(2, next)){
                   Game.testBlock = next; 
                }  else{
                    next.destroy();
                }              
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
         switch (e.getKeyCode()) {
            case 'W':
            case 'S':
               Game.testConstruct.velY = 0;
                break;
            case 'A':
            case 'D':
                Game.testConstruct.velX = 0;
                break;
        }
    }
    
    
    public static Block nearestBlock(int x, int y){
        Coordinate target = new Coordinate(x,y);
        Block closest = null;
        double nearestDistance = 9999999;
        for(Construct c : Game.handler.constructs){
            for(Block b : c.components){
                if(Coordinate.distanceBetween(target, b.location) < nearestDistance){
                    nearestDistance = Coordinate.distanceBetween(target, b.location);
                    closest = b;
                }
            }
        }
        System.out.println("nearest block is " + closest);
        return closest;
    }

   public static Block getBlockAt(Coordinate c){
       return getBlockAt(c.x,c.y);
   }
   
   public static Block getBlockAt(int x, int y){
       Coordinate target = new Coordinate(x,y);
        Block closest = null;
        double nearestDistance = 9999999;
        for(Construct c : Game.handler.constructs){
            for(Block b : c.components){
                if(Coordinate.distanceBetween(target, b.location) < nearestDistance){
                    nearestDistance = Coordinate.distanceBetween(target, b.location);
                    closest = b;
                }
            }
        }
        if(nearestDistance > (Block.BLOCK_HEIGHT/2) * 1.25){
            //return null if the click was farther from the center of the block than the block is wide
            return null;
        }
        return closest;
   }

}
