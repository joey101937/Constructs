/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constructs;

import Core.Game;

/**
 * AI for enemy constructs
 * @author Joseph
 */
public class ConstructAI {
    public int jitteryness = 1;
    public Construct host;
    public Construct target = null;
    
    public ConstructAI(Construct c){
        host = c;
    }
    
    public void tick() {
        for (Construct c : Game.handler.constructs) {
            if (c == host) {
                continue;
            }
            target = c;
        }       
        System.out.println("target " + target);
        if (target != null) {
            host.shootAt(target.getLocation());
        }
    }
}
