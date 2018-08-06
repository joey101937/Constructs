package core;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import Constructs.Blocks.ArmorBlock;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Joseph
 */
public class Input implements KeyListener{
    //FIELDS
    public Game hostGame;
    public Input(Game x){
        hostGame = x;
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
                Game.testBlock.connect(0, next);
                Game.testBlock = next;
                break;
             case 'Y': //right of last
                next = new ArmorBlock();
                Game.testBlock.connect(1, next);
                Game.testBlock = next;
                break;
            case 'H': //left of last
                next = new ArmorBlock();
                Game.testBlock.connect(3, next);
                Game.testBlock = next;
                break;
            case 'G': //below of last
                next = new ArmorBlock();
                Game.testBlock.connect(2, next);
                Game.testBlock = next;
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

}
