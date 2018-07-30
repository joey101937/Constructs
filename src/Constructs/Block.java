/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constructs;

import core.Coordinate;
import java.util.ArrayList;

/**
 * Basic building block for all constructs
 * @author Joseph
 */
public abstract class Block {
    public static final int BLOCK_HEIGHT = 10, BLOCK_WIDTH = 10;
    public Coordinate localCoord;    //X,Y about orgin block
    public Coordinate AbsoluteCoord;    //X,Y relative to world
    public boolean isOrgin;
    public ArrayList<Block> connected = new ArrayList<>(); //blocks connected to this one
    public Orientation orientation = Orientation.Up;
    
    
    public void destroy(){
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
        
        switch(i){
            case 0:
                
                break;
        }
    }
}
