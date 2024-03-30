package application;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.Objects;


public class Hebel2 {

    public static ArrayList<Hebel2> list = new ArrayList<>(); // Liste mit allen erstellten Kugeln


    private Vector2d startPos, size;
    private double solidity;
    private double angle;
    private Circle pivot;
    private Vector2d middle;
    private Wall hebel, hebel2;
    private double radius;
    private double endAngle;

    public double storeAngle;

    public Hebel2(Vector2d startPos, Vector2d size, double startAngle, double endAngle, Pane p) {

        this.storeAngle = startAngle;
        this.middle = new Vector2d();
        this.startPos = new Vector2d();
        this.size = new Vector2d();
        this.endAngle = endAngle;
        this.startPos = startPos;
        this.size = size;
        this.angle = angle;

        middle.x = startPos.x + (size.x / 2.0) + 20;
        middle.y = startPos.y + (size.y / 2.0) + 20;
        radius = 10.0 + Math.min(size.y / 2.0, size.x / 2.0);

        Image im2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("oak.jpg")));
        ImagePattern imagePattern = new ImagePattern(im2);

        hebel = new Wall(startPos.x, startPos.y, startPos.x + size.x / 2.0, startPos.y + size.y / 2.0, 10.0, p, true);
        hebel2 = new Wall(startPos.x + size.x, startPos.y + size.y, middle.x - 20, middle.y - 20, 10.0, p, true);

        pivot = new Circle(middle.x, middle.y, radius);
        pivot.setFill(imagePattern);
        p.getChildren().add(pivot);

        list.add(this);
    }


    public void update() {

        rotationDir1();
        rotationDir2();
    }

    // Länge des hebels bis zum Mittelpunkt
    private double length() {

        return hebel.getVec().length();
    }


    // Winkelgeschwindigkeit berechnen
    private double angularVelocity(Wall hebel) {

        return (hebel.getBallVelByCollision.length() / length()) * 0.95;
    }


    // Geschwindigkeit berechen (Bahngeschwindigkeit)
    private double velocity(Wall hebel) {

        return (length() * angularVelocity(hebel)) * 0.95;
    }


    // Geschwindigkeit der Kugel bei Kollision für den oberen hebel
    private double BallVel(Wall hebel) {

        return hebel.getBallVelByCollision.length();
    }


    // Hebelrotation für alle Richtungen (Hebel oben)
    public void rotationDir1() {

        // Die beiden bewegungsrichtungen für den Hebel oben
        if (BallVel(hebel) > MainWindow.hebelKraft * 100.0) {

            // hat die Kugel genug Geschwindigkeit, um den Hebel zu bewegen?
            if (hebel.getBallVelByCollision.x < 0) {

                getWall1().rotateLineStart(this.endAngle);
                getWall2().rotateLineStart(this.endAngle);

            } else {

                if (this.storeAngle > 0) {

                    getWall1().rotateLineStart(this.storeAngle);
                    getWall2().rotateLineStart(this.storeAngle);
                } else {
                    getWall1().rotateLineStart(-this.storeAngle);
                    getWall2().rotateLineStart(-this.storeAngle);
                }
            }
        }
    }

    public void rotationDir2() {

        // Hebelrotation für alle Richtungen (Hebel unten)
        if (BallVel(hebel2) > MainWindow.hebelKraft * 100.0) {

            // hat die Kugel genug Geschwindigkeit, um den Hebel zu bewegen?
            if (hebel2.getBallVelByCollision.x < 0) {

                if (this.storeAngle > 0) {

                    getWall2().rotateLineStart(this.storeAngle);
                    getWall1().rotateLineStart(this.storeAngle);
                } else {
                    getWall2().rotateLineStart(-this.storeAngle);
                    getWall1().rotateLineStart(-this.storeAngle);
                }
            } else {
                getWall2().rotateLineStart(this.endAngle);
                getWall1().rotateLineStart(this.endAngle);
            }
        }
    }





//    Hebel eine Geschwindigkeit geben, damit er Kugeln in bewegung setzen kann
//    hebel.setVelocity(velocity(), 0);
//    hebel.rotateAt(1);
//    hebel2.rotateAt(1);


    public Wall getWall1(){

        return hebel;
    }

    public Wall getWall2(){

        return hebel2;
    }
}
