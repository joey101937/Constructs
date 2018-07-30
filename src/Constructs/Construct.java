/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constructs;

import Constructs.Blocks.OriginBlock;
import java.util.ArrayList;

/**
 *
 * @author Joseph
 */
public class Construct {
    ArrayList<Block> components = new ArrayList<Block>();
    OriginBlock orgin;
    Orientation orienation = Orientation.Up;
    public void addBlock(Block b){
        components.add(b);
    }
}
