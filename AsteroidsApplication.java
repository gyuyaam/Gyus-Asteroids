package asteroids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AsteroidsApplication extends Application {
    
    public static int width = 300;
    public static int height = 200;
    
    public static void main(String[] args) {
        launch(AsteroidsApplication.class);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();
        pane.setPrefSize(width, height);
        Text text = new Text(10, 20, "Points: 0");
        pane.getChildren().add(text);
        AtomicInteger points = new AtomicInteger();
        //
        Ship ship = new Ship(width/2,height/2);
        List<Asteroid> asteroids = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            Random random = new Random();
            Asteroid asteroid = new Asteroid(random.nextInt(width/3), random.nextInt(height/3));
            asteroids.add(asteroid);
        }
        
        pane.getChildren().add(ship.character());
        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.character()));
        
        List<Projectile> projectiles = new ArrayList<>();
        
        Scene scene = new Scene(pane);
        Map<KeyCode, Boolean> keys = new HashMap<>();
        scene.setOnKeyPressed((event) -> {
            keys.put(event.getCode(), Boolean.TRUE);      
        });
        scene.setOnKeyReleased((event) -> {
            keys.put(event.getCode(), Boolean.FALSE);
        });
        
        //
        
        new AnimationTimer() {
            @Override
            public void handle(long l) {
                if(keys.getOrDefault(KeyCode.LEFT, Boolean.FALSE)) {
                    ship.left();
                }
                if(keys.getOrDefault(KeyCode.RIGHT, Boolean.FALSE)) {
                    ship.right();
                }
                if(keys.getOrDefault(KeyCode.UP, Boolean.FALSE)) {
                    ship.accelerate();
                }
                if(keys.getOrDefault(KeyCode.SPACE, Boolean.FALSE) && projectiles.size() < 3) {
                    Projectile projectile = new Projectile((int) ship.character().getTranslateX(), (int) ship.character().getTranslateY());
                    projectile.character().setRotate(ship.character().getRotate());
                    projectiles.add(projectile);
                    projectile.accelerate();
                    projectile.setMovement(projectile.getMovement().normalize().multiply(3));
                    
                    pane.getChildren().add(projectile.character());
                }
                //
                ship.move();
                projectiles.forEach(projectile -> { 
                    projectile.move();
                    
                    asteroids.forEach(asteroid -> {
                        if(projectile.collide(asteroid)) {
                            projectile.setAlive(false);
                            asteroid.setAlive(false);
                        }
                    });
                    
                    if(!projectile.isAlive()) {
                        text.setText("Points: " + points.addAndGet(1000));
                    }
                });
                
                projectiles.stream()
                        .filter(projectile -> !projectile.isAlive())
                        .forEach(projectile -> pane.getChildren().remove(projectile.character()));
                
                projectiles.removeAll(projectiles.stream()
                        .filter(projectile -> !projectile.isAlive())
                        .collect(Collectors.toList()));
                        
                
                asteroids.forEach(asteroid -> {
                    asteroid.move();
                    
                    if(ship.collide(asteroid)) {
                        text.setText("Game Over! Points: " + points.get());
                        stop();
                    }
                });
                
                asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .forEach(asteroid -> pane.getChildren().remove(asteroid.character()));
                asteroids.removeAll(asteroids.stream()
                .filter(asteroid -> !asteroid.isAlive())
                .collect(Collectors.toList()));
                
                if(asteroids.size() < 5) {
                    Asteroid asteroid = new Asteroid(width, height);
                    if(!asteroid.collide(ship)) {
                        asteroids.add(asteroid);
                        pane.getChildren().add(asteroid.character());
                    }
                }
            }          
        }.start();
        
        //
        
        stage.setTitle("Asteroids!");
        stage.setScene(scene);
        stage.show();
    }
}
