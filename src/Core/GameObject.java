package Core;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Graphics2D;
import java.util.Iterator;

/**
 * A game object is everything that will go in the game. it ticks (does somthing every frame)
 * it renders (displays itself) and has coordinates for its X and Y position.
 * also has an x velocity and a Y velocity variable as well has two arrays for sprites.
 * those arnt yet implemented however.
 * @author Joseph
 */
public abstract class GameObject {
    /*  FIELDS  */
    public int velX, velY, speed;       //X/Y coordinates and X/Y velocity
    public Coordinate location = new Coordinate(0,0);    //X,Y relative to world
    public int width, height;                  //length and width
    public boolean isAlive = true;      
    public String name;                     //used to identify what kind of gameobject this is
    /**
     * constructor that takes two ints for x and y coordinates
     * @param x
     * @param y 
     */
    public GameObject(int x, int y){
        location.x = x;
        location.y = y;
    }
    
    //every game tick, we update the coordinates based on velocity. at the end, clamp the coordinates to within the bounds of the world
    public void tick(){
        adjustPositionForVelocity();
    }
    
    public void adjustPositionForVelocity() {
        for (int i = 0; i < Math.abs(velX); i++) {
            if (velX < 0) {
                location.x-=speed;
            } else if (velX > 0) {
                location.x+=speed;
            }
        }
        for (int i = 0; i < Math.abs(velY); i++) {
            if (velY < 0) {
                location.y-=speed;
            } else if (velY > 0) {
                location.y+=speed;
            }
        }
        location.x = Main.clamp(location.x, Game.width - width / 2, 0);
        location.y = Main.clamp(location.y, Game.height - height / 2, 0);
    }

    /**
     * this is run once a frame and contains code that puts the object on
     * screen.
     *
     * @param g
     */
    public abstract void render(Graphics2D g);
    

    /**
     * removes object from the game handler
     */
    public void destroy() {
        isAlive = false;
        Iterator<GameObject> it = Game.handler.storage.iterator();
        while (Game.handler.storage.contains(this)) {
            try {
                Game.handler.storage.remove(this);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("CME from removing");
            }
        }

    }
}
