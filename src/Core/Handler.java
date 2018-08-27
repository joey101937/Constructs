package Core;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import Constructs.Construct;
import java.awt.Graphics2D;
import java.util.LinkedList;

/**
 *
 * @author Joseph
 */
public class Handler {

    public LinkedList<GameObject> storage = new LinkedList<>();
    public LinkedList<Construct> constructs = new LinkedList<>();
    
    public void render(Graphics2D g) {
        for (GameObject go : storage) {
            go.render(g);
        }
        for(Construct c : constructs){
            c.render(g);
        }
    }

    public void tick() {
        for (GameObject go : storage) {
            go.tick();
        }
        for (Construct c : constructs) {
            c.tick();
        }
    }

}
