/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import java.awt.Graphics2D;
import java.util.LinkedList;

/**
 *
 * @author Joseph
 */
public class VisualEffectHandler {
    public LinkedList<Sticker> stickers = new LinkedList<>();
    
    public void render(Graphics2D g){
        for(Sticker s : stickers){
            if(s.disabled)continue;
            s.render(g);
        }
    }
}
