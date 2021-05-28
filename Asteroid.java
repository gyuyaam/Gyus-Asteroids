/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroids;

import java.util.Random;

/**
 *
 * @author gyukim
 */
public class Asteroid extends Character {
    private double rotation;
    
    public Asteroid(int x, int y) {
        super(new PolygonFactory().newPolygon(), x, y);
        
        Random random = new Random();
        
        super.character().setRotate(random.nextInt(360));
        
        int acceleration = 1 + random.nextInt(10);
        for(int i = 0; i < acceleration; i++) {
            accelerate();
        }
        
        this.rotation = 0.5 - random.nextDouble();
    }
    
    @Override
    public void move() {
        super.move();
        super.character().setRotate(super.character().getRotate() + this.rotation);
    }
    
}
