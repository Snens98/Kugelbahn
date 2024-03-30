package application;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.Objects;

public class Hebel {

    public static ArrayList<Hebel> list = new ArrayList<>(); // Liste mit allen erstellten Kugeln


    private Vector2d pos;
    private double height;
    private double width;
    private double angle;

    private Circle pivot;
    private Vector2d middle;
    private Rect hebel;
    private double radius;

    public Hebel(Vector2d pos, double height, double width, double angle, Pane p) {

        this.middle = new Vector2d();
        this.pos = new Vector2d();
        this.pos = pos;
        this.height = height;
        this.width = width;
        this.angle = angle;

        Image im2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("oak.jpg")));
        ImagePattern imagePattern = new ImagePattern(im2);

        middle.x = pos.x + (width / 2.0) + 20;
        middle.y = pos.y + (height / 2.0) + 20;

        radius = 5.0 + Math.min(height/2.0, width/2.0);

        hebel = new Rect(pos, height, width, angle, p);
        pivot = new Circle(middle.x, middle.y, radius);
        pivot.setFill(imagePattern);
        p.getChildren().add(pivot);

//        hebel.setRotation(0);
        list.add(this);


    }



    public void update(){

    }
}
