package core;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * Coordinate holds values for position
 * @author Joseph
 */
public class Coordinate {
    public int x, y;
    
    public static double distanceBetween(Coordinate a, Coordinate b){
        return Math.sqrt(Math.pow((b.x-a.x), 2)+Math.pow(b.y-a.y, 2));
    }
}
