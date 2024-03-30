package application;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.joml.Vector2d;
import java.util.ArrayList;
import java.util.Objects;


public class Rect {

    public static ArrayList<Rect> list = new ArrayList<>();

    public Wall[] wall;
    private Vector2d pos = new Vector2d();
    private Vector2d middleRect = new Vector2d();
    private double width, height;
    private Rectangle rect;
    public double rotation;
    public Circle cStart, rStart;
    public double storeAngle;

    public Rect(Vector2d pos, double height, double width, double rotation, Pane p){

        storeAngle = rotation;
        this.rotation = rotation;
        this.pos.x = pos.x;
        this.pos.y = pos.y;
        this.width = width;
        this.height = height;

        Image im2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("Wood.jpg")));
        ImagePattern imagePattern = new ImagePattern(im2);

        middleRect.x = pos.x + (width / 2.0);
        middleRect.y = pos.y + (height /2.0);

        cStart = new Circle();
        cStart.setLayoutX(MainWindow.rlX + middleRect.x+0.0);
        cStart.setLayoutY(MainWindow.rlY + middleRect.y+0.0);
        cStart.setRadius(5 + Math.min(height, width)/10.0);

        rStart = new Circle();
        rStart.setLayoutX(MainWindow.rlX + middleRect.x-0.0);
        rStart.setLayoutY(MainWindow.rlY + middleRect.y-0.0);
        rStart.setRadius(5 + Math.min(height, width)/10.0);

        wall  = new Wall[4];
        wall[0] = new Wall(pos.x, pos.y, pos.x + width, pos.y, 2, p, true);
        wall[1] = new Wall(pos.x+width, pos.y+20, pos.x+width, pos.y+height, 1.8, p, true);
        wall[2] = new Wall(pos.x+width, pos.y+height, pos.x, pos.y+height, 2, p, true);
        wall[3] = new Wall(pos.x, pos.y+height, pos.x, pos.y, 1.8, p, true);

        rect = new Rectangle();
        rect.setLayoutX(pos.x+20-wall[0].getStrokeWidth()/2.0);
        rect.setLayoutY(pos.y+20-wall[0].getStrokeWidth()/2.0);
        rect.setHeight(height+wall[0].getStrokeWidth()/2.0);
        rect.setWidth(width+wall[0].getStrokeWidth()/2.0);
        rect.setFill(imagePattern);

        rotate(rotation);
        p.getChildren().add(rect);

        storeStartPoints();
        storeEndPoints();

        list.add(this);


        cStart.setFill(Color.TRANSPARENT);
        p.getChildren().add(cStart);

//        rStart.setFill(Color.YELLOW);
//        p.getChildren().add(rStart);
    }


    public void update(){

        rotate(this.rotation);
//        editLinePos();
    }


    double o = 0.5;
    public void rotate(double angle) {

        wall[0].getLine().setFill(Color.TRANSPARENT);

        rotateLineStart(pos.x+o, pos.y, angle, 0);
        rotateLineEnd(pos.x + width-o, pos.y, angle, 0);

        rotateLineStart(pos.x + width, pos.y+o, angle, 1);
        rotateLineEnd(pos.x + width, pos.y + height-o, angle, 1);

        rotateLineStart(pos.x + width-o, pos.y + height, angle, 2);
        rotateLineEnd(pos.x+o, pos.y + height, angle, 2);

        rotateLineStart(pos.x, pos.y + height-o, angle, 3);
        rotateLineEnd(pos.x, pos.y+o, angle, 3);
    }

    public double rotation(double angle) {

        rect.setRotate(angle);

        double calc = Math.toRadians(angle);
        return calc;
    }



    public void rotateLineStart(double startX, double startY, double angle, int i){

        double ox = startX-middleRect.x;
        double oy = startY-middleRect.y;

        double rotatedX = ox*Math.cos(rotation(angle)) - oy*Math.sin(rotation(angle));
        double rotatedY = ox*Math.sin(rotation(angle)) + oy*Math.cos(rotation(angle));

        wall[i].setStartX(rotatedX + 20 + middleRect.x);
        wall[i].setStartY(rotatedY + 20 + middleRect.y);
    }




    public void rotateLineEnd(double endX, double endY, double angle, int i){

        double ox2 = endX-middleRect.x;
        double oy2 = endY-middleRect.y;

        double rotatedX2 = ox2*Math.cos(rotation(angle)) - oy2*Math.sin(rotation(angle));
        double rotatedY2 = ox2*Math.sin(rotation(angle)) + oy2*Math.cos(rotation(angle));

        wall[i].setEndX(rotatedX2 + 20 + middleRect.x);
        wall[i].setEndY(rotatedY2 + 20 + middleRect.y);
    }






    public Vector2d[] startPoints  = new Vector2d[4];

    public void storeStartPoints() {

        startPoints[0] = new Vector2d();
        startPoints[1] = new Vector2d();
        startPoints[2] = new Vector2d();
        startPoints[3] = new Vector2d();

        for (int i = 0; i <= 3; i++) {

            startPoints[i].x = wall[i].getStartX();
            startPoints[i].y = wall[i].getStartY();
        }
    }


    public Vector2d[] endPoints  = new Vector2d[4];
    public void storeEndPoints() {

        endPoints[0] = new Vector2d();
        endPoints[1] = new Vector2d();
        endPoints[2] = new Vector2d();
        endPoints[3] = new Vector2d();

        for (int i = 0; i <= 3; i++) {

            endPoints[i].x = wall[i].getEndX();
            endPoints[i].y = wall[i].getEndY();
        }
    }



    public void setRotation(double angle){

        this.rotation = angle;
        this.rotate(angle);
    }

    public void rotateAt(double angle){

        this.rotation += angle;
        this.rotate(angle+=rotation);
    }



    //Zum ändern der Linien
    public void editLinePos() {

//        cStart.setOnMouseDragged(arg0 -> {
//
//            // Nur wenn der Bearbeiten-Modus an ist
//            if(!MainWindow.bea) {
//                return;
//            }
//
//            cStart.setLayoutX(cStart.getLayoutX() + arg0.getX());
//            cStart.setLayoutY(cStart.getLayoutY() + arg0.getY());
//
//            rect.setLayoutX(cStart.getLayoutX()-width/2.0 + arg0.getX());
//            rect.setLayoutY(cStart.getLayoutY()-height/2.0  + arg0.getY());
//            rect.setRotate(0.0);
//
//            wall[0].setStartX(cStart.getLayoutX() + pos.x + arg0.getX() - width);
//            wall[0].setStartY(cStart.getLayoutY() +  pos.y + arg0.getY()- height*1.5);
//            wall[0].setEndX(cStart.getLayoutX() + pos.x + width + arg0.getX() - width);
//            wall[0].setEndY(cStart.getLayoutY() + pos.y + arg0.getY() - height*1.5);
//
//            wall[1].setStartX(cStart.getLayoutX() + pos.x+width+ arg0.getX() - width);
//            wall[1].setStartY(cStart.getLayoutY() + pos.y + arg0.getY() - height*1.5);
//            wall[1].setEndX(cStart.getLayoutX() + pos.x+width + arg0.getX() - width);
//            wall[1].setEndY(cStart.getLayoutY() + pos.y+height + arg0.getY() - height*1.5);
//
//            wall[2].setStartX(cStart.getLayoutX() + pos.x+width + arg0.getX() - width);
//            wall[2].setStartY(cStart.getLayoutY() + pos.y+height + arg0.getY() - height*1.5);
//            wall[2].setEndX(cStart.getLayoutX() + pos.x + arg0.getX()-width);
//            wall[2].setEndY(cStart.getLayoutY() + pos.y+height + arg0.getY()-height*1.5);
//
//            wall[3].setStartX(cStart.getLayoutX() +pos.x + arg0.getX() - width);
//            wall[3].setStartY(cStart.getLayoutY() + pos.y+height + arg0.getY() - height*1.5);
//            wall[3].setEndX(cStart.getLayoutX() + pos.x + arg0.getX() - width);
//            wall[3].setEndY(cStart.getLayoutY() +pos.y + arg0.getY() - height*1.5);
//        });



        cStart.setOnMouseDragged(arg0 -> {

            // Nur wenn der Bearbeiten-Modus an ist
            if(!MainWindow.bea) {
                return;
            }

            rotation = -arg0.getY();
            rotation(rotation);

        });
    }
}
