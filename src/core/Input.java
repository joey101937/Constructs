package core;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


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
        switch(e.getKeyCode()){
            case 'W':
               Game.testConstruct.velX = 1;
                break;
            case 'Z':
                Game.testBlock.destroy();
                System.out.println(Game.testConstruct.components);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
         switch (e.getKeyCode()) {
            case 'W':
               Game.testConstruct.velX = 0;
                break;
        }
    }

}
