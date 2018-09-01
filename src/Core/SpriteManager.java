/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Joseph
 */
public abstract class SpriteManager {

    public static boolean initialized = false;

    public static BufferedImage test;
    public static BufferedImage[] birdSequence;
    public static BufferedImage[] explosionSequence;

    public static void initialize(){
        try{
            test = load("checkmarkSmall.png");
            birdSequence = new BufferedImage[9];
            for(int i = 0; i < 9; i++){
                birdSequence[i] = load("birdies"+i+".png");
            }
            explosionSequence = new BufferedImage[16];
            for(int i = 0; i < 16; i++){
                explosionSequence[i] = load("explosionSequence/tile0"+i+".png");
            }
            initialized=true;
        }catch(Exception e){
            e.printStackTrace();
            Main.display("Error loading all assets. Please Verify Assets folder.");
        }
    }

    /**
     * returns a bufferedImage loaded from the given filename, located in assets
     * folder.
     *
     * @param filename name of file including extension
     * @return buffered image render
     * @throws IOException if file cannot be found or loaded
     */
    private static BufferedImage load(String filename) throws IOException {
        return ImageIO.read(new File(Main.assets + filename));
    }
}
