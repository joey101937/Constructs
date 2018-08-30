package Core;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import Constructs.Block;
import Constructs.Blocks.ArmorBlock;
import Constructs.Blocks.CannonBlock;
import Constructs.Construct;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ConcurrentModificationException;

/**
 * this is the part of the screen that you look at while playing and that
 * contains all gameObjects
 * @author Joseph
 */
public class Game extends Canvas implements Runnable {
    /*  FIELDS   */

    private Thread thread = null;
    private boolean running = false;
    public BufferedImage backgroundImage;
    public static Handler handler = new Handler();
    public static VisualEffectHandler visHandler = new VisualEffectHandler();
    public static int width, height;
    public Window window;
    public Input input;
    public static Game mainGame; //main game instance
    public static Construct testConstruct;
    public static Block testBlock;
    
    public Game() {
        mainGame = this;
        this.width = 700;
        this.height = 700;
        window = new Window(this);
        Setup();
        input = new Input(this);
        this.addKeyListener(input);      
        this.addMouseListener(input);
    }

    /**
     * use this method to set starting objects etc
     */
    public void Setup() {
         testConstruct = new Construct(100,100);
         handler.constructs.add(testConstruct);
         testBlock = new CannonBlock();
         testConstruct.orgin.connect(0, testBlock);
         
         //Construct testConstruct2 = new Construct(500,300);
        // while(testConstruct2.components.size() < 30){
        //   testConstruct2.components.get(Main.generateRandom(0, testConstruct2.components.size()-1)).connect(Main.generateRandom(0, 4), new ArmorBlock());
        // }
         handler.constructs.add(Construct.generateRandomConstruct(new Coordinate(500,300), 30));

        
      
    }

    //core tick, tells all game Objects to tick
    private void tick() {
        try{
            handler.tick();
        }catch(ConcurrentModificationException cme){
            cme.printStackTrace();
        }
        
    }

    //core render method, tells all game Objects to render
    private void render() {

        BufferStrategy bs = this.getBufferStrategy();

        if (bs == null) { ///run once at the start
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g = (Graphics2D)g;
        Graphics2D g2d = (Graphics2D)g;
        g.setColor(Color.GREEN);

        this.renderBackGround(g);
        handler.render(g2d);
        visHandler.render(g2d);
        g.dispose();
        
        bs.show();
    }

    /**
     * renders the background onto the game
     * @param g 
     */
    public void renderBackGround(Graphics g) {
        try {
            if (backgroundImage == null) {
                backgroundImage = ImageIO.read(new File(Main.getDir() + Main.assets + "Platformbg.png"));
            }
            g.drawImage(backgroundImage, 0, 0, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Core game loop 
    @Override
    public void run() {
        this.requestFocus(); ///automatically selects window so you dont have to click on it
        long lastTime = System.nanoTime();
        double amountOfTicks = 60;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
            }
            if (running) {
                try{
                this.render();
                }catch(ConcurrentModificationException cme){
                    System.out.println("cme render");
                }
            }
            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
                ///this triggers once a second
            }
        }
        stop();
    }

    //starts the main game
    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    ///stops the main game
    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * adds object to the world, the object will be located at whatever x/y coordinates it has
     * @param o object to add
     */
    public void addObject(GameObject o){
        handler.storage.add(o);
    }
    
    public void removeObject(GameObject o){
        o.destroy();
    }

}
