/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constructs.Blocks;

import Constructs.Projectile;
import Core.Coordinate;

/**
 * Interface for offensive blocks that shoot projectiles
 * @author Joseph
 */
public interface ShootingBlock {
    public Projectile getProjectile();
    public void shootAt(Coordinate x);
}
