/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroids;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

/**
 *
 * @author gyukim
 */
public abstract class Character {
    private Polygon character;
    private Point2D movement;
    
    public Character(Polygon polygon, int x, int y) {
        this.character = polygon;
        this.character.setTranslateX(x);
        this.character.setTranslateY(y);
        this.movement = new Point2D(0,0);
    }
    
    public Polygon character() {
        return this.character;
    }
    
    public void left() {
        this.character.setRotate(this.character.getRotate() - 5);
    }
    
    public void right() {
        this.character.setRotate(this.character.getRotate() + 5);
    }
    
    public void move() {
        this.character.setTranslateX(this.character.getTranslateX() + this.movement.getX());
        this.character.setTranslateY(this.character.getTranslateY() + this.movement.getY());
        if(this.character.getTranslateX() < 0) {
            this.character.setTranslateX(this.character.getTranslateX() + AsteroidsApplication.width);
        }
        if(this.character.getTranslateX() > AsteroidsApplication.width) {
            this.character.setTranslateX(this.character.getTranslateX() % AsteroidsApplication.width);
        }
        if(this.character.getTranslateY() < 0) {
            this.character.setTranslateY(this.character.getTranslateY() + AsteroidsApplication.height);
        }
        if(this.character.getTranslateY() > AsteroidsApplication.height) {
            this.character.setTranslateY(this.character.getTranslateY() + AsteroidsApplication.height);
        }
    }
    
    public void accelerate() {
        double X = Math.cos(Math.toRadians(this.character.getRotate())) * 0.05;
        double Y = Math.sin(Math.toRadians(this.character.getRotate())) * 0.05;
        this.movement = this.movement.add(X, Y);
    }
    
    public boolean collide(Character other) {
        Shape collision = Shape.intersect(this.character, other.character());
        return collision.getBoundsInLocal().getWidth() != -1;
    }
    
    public Point2D getMovement() {
        return this.movement;
    }
    
    public void setMovement(Point2D pd) {
        this.movement = pd;
    }
    
    public boolean setAlive(boolean value) {
        if(value == false) {
            this.character.setDisable(true);
            this.character.setVisible(false);
        }
        return true;
    }
    
    public boolean isAlive() {
        if(this.character.isDisabled()) {
            return false;
        }
        return true;
    }
}
