package application;

import javafx.scene.layout.Pane;
import org.joml.Vector2d;

import java.util.ArrayList;

public class Pendel {

    public static ArrayList<Pendel> list = new ArrayList<>();

    public int id;
    public static int staticid;

    private Wall pline;
    private Kugel pCircle;

    double angle = Math.PI/2.0;
    double velocity, v;
    double acceleration;
    double Fp;
    double armLength = 500;
    Vector2d pivot = new Vector2d(500, 0);
    Vector2d ball = new Vector2d();

    public double storeAngle;

    public Pendel(double pivotX, double pivotY, double armLength, double startAngle, Pane ap) {

        storeAngle = startAngle;

        staticid++;
        this.id = staticid;

        this.pivot.x = pivotX;
        this.pivot.y = pivotY;
        this.armLength = armLength;
        this.angle = startAngle;

        pline = new Wall(this.pivot.x, this.pivot.y, this.pivot.x, this.pivot.y+armLength, 2, ap, false);
        pCircle = new Kugel(12, 500, new Vector2d(this.pivot.x+20, this.pivot.y+armLength+20), new Vector2d(0, 0), ap);
        pCircle.rotate(false);

        list.add(this);
    }


    public void update(){

        pline.setStart(pivot.x + MainWindow.rlX, pivot.y + MainWindow.rlY);

        Fp = -MainWindow.g * Math.sin(angle); // Berechnung der Pendelkraft

        acceleration = ((Fp / armLength) + MainWindow.wind / Main.PixelPerMeter)  * Main.frametime;

        velocity += acceleration;
        velocity *= 0.999; // Windwiderstand
        angle += velocity;

        //Positon vom Pendel berechnen
        ball.x = armLength * Math.sin(angle) + pivot.x + MainWindow.rlX;
        ball.y = armLength * Math.cos(angle) + pivot.y + MainWindow.rlY;

        pCircle.updatePos(ball.x, ball.y);
        pline.setEnd(ball.x, ball.y);

        // Geschwindigkeitsvektor berechnen, um abprallen zu ermöglichen
        v = (velocity * armLength) * Main.PixelPerMeter;
        pCircle.setVelocity(v, MainWindow.g);
    }



    public void bounce(){}


    public void setVelocity(double v){

        this.velocity = v;
    }

    public void addVelocity(double v){

        this.velocity += v;
    }
}
