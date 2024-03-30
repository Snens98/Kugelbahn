package application;

import java.net.URL;
import java.util.ResourceBundle;

import org.joml.Vector2d;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MainWindow implements Initializable {

    @FXML
    private AnchorPane ap;

    @FXML
    private Rectangle rec;

    @FXML
    private Button bearbeiten;

    @FXML
    private Slider gravitation;

    @FXML
    private Slider windSlider;

    @FXML
    private Slider hebelSlider;

    @FXML
    private Label time;

    @FXML
    private Label dN;

    @FXML
    private TextField radius;

    @FXML
    private TextField gewicht;

    @FXML
    private TextField geschX;

    @FXML
    private TextField geschY;

    @FXML
    private CheckBox richtungsVek;
    static boolean rVec;

    @FXML
    private CheckBox WallVec;

    @FXML
    private Button back;

    public static double frameX;
    public static double frameY;

    public static double g;
    public static double wind;

    public static double zeit;
    public static double frameTime;
    public static double mousePosX;
    public static double mousePosY;
    public static double hebelKraft;
    public static boolean bea = false;
    public static String dNtext ="";

    private Wall rWall, rWall2, rWall3, rWall4;
    private Hebel2 hebel1, hebel2;

    public static double rlX, rlY;

    private double px, py;

    private Pendel pendel;
    private Kugel k;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        startLoop();
        drawWindow();

        rlX = rec.getLayoutX();
        rlY = rec.getLayoutY();

        frameX = rec.getWidth();
        frameY = rec.getHeight();


        new Wall(0, 0, 0, frameY, 5, ap, true);
        new Wall(0, 0, frameX, 0, 5, ap, true);
        new Wall(frameX, 0, frameX, frameY, 5, ap, true);
        new Wall(0, frameY, frameX, frameY, 5, ap, true);



//		int i = 30;
//		new Wall(50, 50 +i, 100, 60+i, 5, ap, true);
//		new Wall(100, 60+i, 150, 80+i, 5, ap,true);
//		new Wall(150, 80+i, 200, 120+i, 5, ap, true);
//		new Wall(200, 120+i, 250, 180+i, 5, ap,true);
//		new Wall(250, 180+i, 300, 220+i, 5, ap,true);
//		new Wall(300, 220+i, 350, 240+i, 5, ap, true);
//		new Wall(350, 240+i, 400, 240+i, 5, ap, true);
//		new Wall(400, 240+i, 450, 220+i, 5, ap, true);
//		new Wall(450, 220+i, 500, 180+i, 5, ap, true);





//        new Wall(280, 250, 880, 170, 6, ap, true);
//        new Wall(5, 250, 300, 350, 7, ap, true);
//        new Wall(350, 420, 945, 330, 8, ap, true);
//        new Wall(0, 390, 390, 490, 4, ap, true);
//        new Wall(0, 520, 950, 540, 5, ap, true);
//        new Wall(0, 60, 550, 120, 5, ap, true);
//        new Wall(310, 352, 400, 352, 3, ap, true);

       pendel = new Pendel(600, 250, 178, Math.PI/-3.7, ap);
//        new Pendel(150, 185, 200, Math.PI/-8.0, ap);


        new Kugel(12, 100, new Vector2d(80, 30), new Vector2d(0), ap);
        new Kugel(12, 100, new Vector2d(230, 150+6), new Vector2d(0), ap);
        new Kugel(12, 100, new Vector2d(420, 180+6), new Vector2d(0), ap);
        new Kugel(9, 100, new Vector2d(396, 35), new Vector2d(0), ap);
        new Kugel(12, 100, new Vector2d(793, 79+6), new Vector2d(0), ap);

        k = new Kugel(15, 30, new Vector2d(600, 544), new Vector2d(0), ap);
        k.setTexturedText("murmel.jpg");

        rWall4 = new Wall(860, 110, 860+25, 110+25, 4, ap, true);
        rWall = new Wall(830, 140, 830+25, 140+25, 4, ap, true);
        rWall2 = new Wall(830, 250, 830+30, 250+30, 4, ap, true);
        rWall3 = new Wall(900, 310, 900+30, 310+30, 4, ap, true);

//        new Rect(new Vector2d(480, 544-10), 3, 10, 0, ap);
        new Rect(new Vector2d(520, 544-10), 3, 10, 0, ap);
        new Rect(new Vector2d(560, 544-10), 3, 10, 0, ap);
        new Rect(new Vector2d(600, 544-10), 3, 10, 0, ap);
        new Rect(new Vector2d(640, 544-10), 3, 10, 0, ap);


        new Rect(new Vector2d(50, 50), 50, 100, 5, ap);
        new Rect(new Vector2d(150, 150), 40, 150, 2,  ap);
        new Rect(new Vector2d(380, 180), 100, 30, 2.0,  ap); ///

        new Rect(new Vector2d(250, 330), 20, 180, 6,  ap);
        new Rect(new Vector2d(0, 420),    10, 480, 4,  ap);

        new Rect(new Vector2d(500, 450),    26, 30, 0,  ap);
        new Rect(new Vector2d(568, 450),    26, 30, 0,  ap);
        new Rect(new Vector2d(500, 473),    20, 98, 0,  ap);

        new Rect(new Vector2d(602, 460),    20, 100, 10,  ap);
        new Rect(new Vector2d(705, 472),    20, 60, 0,  ap);

        new Rect(new Vector2d(766, 80), 5, 1.3, 2, ap);   ///

        new Rect(new Vector2d(585, 115), 20, 180, -10, ap); ///

        new Rect(new Vector2d(523, 150),    20, 60, -6,  ap);
        new Rect(new Vector2d(450, 165),    20, 60, -4,  ap);

        new Rect(new Vector2d(581, 210),    26, 30, 1,  ap);
        new Rect(new Vector2d(412, 233),    20, 199, 1,  ap);

        new Rect(new Vector2d(375, 26),    5, 5, 1,  ap);  //

        new Rect(new Vector2d(380, 38),    5, 430, 3,  ap); //

        new Rect(new Vector2d(792, 80),    5, 80, 10,  ap); ///
        new Rect(new Vector2d(865, 140),    5, 80, -40,  ap);
        new Rect(new Vector2d(810, 200),    5, 80, 40,  ap);
        new Rect(new Vector2d(865, 260),    5, 80, -40,  ap);
        new Rect(new Vector2d(810, 320),    5, 80, 40,  ap);
        new Rect(new Vector2d(865, 380),    5, 80, -40,  ap);
        new Rect(new Vector2d(810, 420),    5, 80, 40,  ap);
        new Rect(new Vector2d(730, 495),    5, 220, -20,  ap);

        new Rect(new Vector2d(400, 517),    3, 100, 25,  ap);
        new Rect(new Vector2d(270, 498),    30, 100, -3,  ap);

        new Rect(new Vector2d(110, 497),    26, 150, 2,  ap);

        new Rect(new Vector2d(0, 469),    50, 30, 0,  ap);
        new Rect(new Vector2d(68, 496),    26, 30, 0,  ap);
        new Rect(new Vector2d(0, 571-50),    20, 98, 0,  ap);

        new Rect(new Vector2d(340, 20),    10, 15, -10,  ap); ///


        hebel1 = new Hebel2(new Vector2d(770, 65), new Vector2d(0, 400), 5.0,0.4, ap);
        hebel1.getWall1().rotateLineStart(5.0);
        hebel1.getWall2().rotateLineStart(5.0);

        hebel2 = new Hebel2(new Vector2d(370, 20), new Vector2d(0, 150), -2.0,-8.0, ap);
        hebel2.getWall1().rotateLineStart(-8.0);
        hebel2.getWall2().rotateLineStart(-8.0);

    }


    // Renderschleife erstellen
    private void startLoop() {

        new AnimationTimer() {

            long startNanoTime = System.nanoTime();

            @Override
            public void handle(long currentNanoTime) {

                //Zeit pro Frame berechnen
                frameTime = (currentNanoTime - startNanoTime) / 1000000000.0;
                startNanoTime = currentNanoTime;
                update();
            }
        }.start();
    }


    @FXML
    public void resetButton() {

        //Kugeln zur�cksetzen
        for (Kugel k : Kugel.list) {

            k.posXY.set(k.reset);
            k.setVelocity(0.0, 0.0);
            k.setAcceleration(0.0,0.0);

            if(k.id > 7){

                ap.getChildren().remove(k.getKugel());
                k.posXY.set(-500);
            }
        }


        // Linien/Rechtecke zur�cksetzen
        for (Rect r : Rect.list) {

            for (int i = 0; i <= 3; i++) {

                r.wall[i].setStartX(r.startPoints[i].x);
                r.wall[i].setStartY(r.startPoints[i].y);

                r.wall[i].setEndX(r.endPoints[i].x);
                r.wall[i].setEndY(r.endPoints[i].y);
            }

            r.rotation = r.storeAngle;
        }

        hebel1.getWall1().rotateLineStart(5.0);
        hebel1.getWall2().rotateLineStart(5.0);


        hebel2.getWall1().rotateLineStart(-8.0);
        hebel2.getWall2().rotateLineStart(-8.0);

        pendel.angle = pendel.storeAngle;
        pendel.setVelocity(0.0);

        zeit = 0.0;
        time.setText("Sek.: 0.0");
    }

//    private boolean mouseInWindow = false;

    private boolean CheckMouseIsInWindow() {

        return (ypos < frameY + rlY - 3 - Double.parseDouble(radius.getText()))
                && (xpos < frameX + rlX - 3 - Double.parseDouble(radius.getText()))
                && (ypos > rlY + 3 + Double.parseDouble(radius.getText()))
                && (xpos > rlX + 3 + Double.parseDouble(radius.getText()));
    }


    // Bild 60 mal pro Sek. updaten
    public void update() {

        addBallWhenClicked();
        updateMousePos();
        CheckMouseIsInWindow();

        rVec = richtungsVek.isSelected();
        calcdN();


        if (run) {    // Abbruchbedingung --> Wenn man auf den Start/Stopp-Button dr�ckt

            g = gravitation.getValue();    // Gravitation vom Slider auslesen
            wind = windSlider.getValue();    // Wind vom Slider auslesen

            ResetAcceleration(); // Beschleunigungsvektor wird f�r den n�chsten Frame zur�ckgesetzt.

            updateObject();

            UpdateBall();   // Der gesamte Beschleunigungsvektor wird f�r den aktuellen
                            // Frame f�r die Berechnung der Kugel genutzt.

            //Zeit in Sek. berechnen und anzeigen
            zeit += frameTime;
            time.setText("Sek.: " + Math.round(zeit * 1000) / 1000.0);
        }
    }

    private void calcdN(){

        hebelKraft = hebelSlider.getValue();
        double v = Math.round(hebelSlider.getValue() * 100.0) / 100.0;
        dN.setText("Hebel: " + v + " dN");
    }

    private void updateObject(){

        if (rWall != null)
            rWall.rotateAt(180*Main.frametime);

        if (rWall2 != null)
            rWall2.rotateAt(230*Main.frametime);

        if (rWall3 != null)
            rWall3.rotateAt(130*Main.frametime);

        if (rWall4 != null)
            rWall4.rotateAt(300*Main.frametime);


        for (Wall l : Wall.list) {
            l.update();
            LineVecCheck();
        }

        for (Rect r : Rect.list) { //Kollison der Kugel am Rand & Wind

            r.update();
        }

        for (Pendel k : Pendel.list) { //Kollison der Kugel am Rand & Wind

            k.update();
        }

        for (Hebel2 h : Hebel2.list) { //Kollison der Kugel am Rand & Wind

            h.update();
        }


        for (Kugel k : Kugel.list) { //Kollison der Kugel am Rand & Wind
            k.update();
            BallVecCheck();

//				System.out.println(k.getInfos(1)); // Infos zur Kugel mit 1 Nachkommastelle ausgeben.
        }
    }


    private void BallVecCheck() {

        richtungsVek.setOnMouseClicked(arg0 -> {

            for (Kugel k : Kugel.list) {

                if (richtungsVek.isSelected()) {

                    k.directionVec.setStroke(Color.RED);

                } else if (!richtungsVek.isSelected()) {

                    k.directionVec.setStroke(Color.TRANSPARENT);
                }
            }
        });
    }

    private void LineVecCheck() {

        WallVec.setOnMouseClicked(arg0 -> {

            for (Wall l : Wall.list) {

                if (WallVec.isSelected()) {
                    l.dLine.setStroke(Color.GREEN);

                } else if (!WallVec.isSelected()) {
                    l.dLine.setStroke(Color.TRANSPARENT);
                }
            }
        });
    }


    private boolean click = true;
    private boolean run = false;

    // Programm starten und stoppen können
    public void gui() {

        click = !click;

        if (bea) {
            bea = false;
            changeEditMode();
        }

        run = !click;
        changeButtonText();
    }


    @FXML
    public void bearbeiten() {

        if (run) {
            run = false;
            click = !click;
        }
        bea = !bea;

        changeButtonText();
        changeEditMode();
    }


    private void changeButtonText() {

        if (bea) {
            bearbeiten.setTextFill(Color.rgb(100, 150, 240, 1));
        } else {
            bearbeiten.setTextFill(Color.BLACK);
        }
    }


    private void changeEditMode() {

        for (Rect l : Rect.list) {

            l.editLinePos();

            if (!bea) {

                l.cStart.setFill(Color.TRANSPARENT);
//                l.cEnd.setFill(Color.TRANSPARENT);
            } else {
                l.cStart.setFill(Color.rgb(100, 150, 240, 1));
//                l.cEnd.setFill(Color.rgb(100, 150, 240, 1));
            }
        }

        for (Kugel k : Kugel.list) {

            k.editBallPos();

            if (!bea) {

                k.editC.setFill(Color.TRANSPARENT);
            } else {
                k.editC.setFill(Color.rgb(100, 150, 240, 1));
            }
        }
    }


    // Anzeigefenster zeichnen
    private void drawWindow() {

        Rectangle rect = new Rectangle();
        rect.setWidth(frameX);
        rect.setHeight(frameY);
        rect.setFill(Color.LIGHTGRAY);
        ap.getChildren().add(rect);
    }


    private double xpos;
    private double ypos;

    //Position der Maus ermitteln und beim klick die Kugel an der Stelle platzieren
    private void addBallWhenClicked() {


//        (s.matches("\\d+"))
//        (s.matches("-?\\d+([.]{1}\\d+){1}"))

            ap.setOnMouseClicked(arg0 -> {

            xpos = arg0.getX();
            ypos = arg0.getY();

            if (!bea && CheckMouseIsInWindow()) {

                // Neue Kugel erstellen mit den Werten aus den Textfeldern in der Gui
                new Kugel(Double.parseDouble(radius.getText()), Double.parseDouble(gewicht.getText()),
                        new Vector2d(xpos, ypos),
                        new Vector2d(Double.parseDouble(geschX.getText()), -Double.parseDouble(geschY.getText())), ap);
            }
        });
    }

    private boolean validate(String text)
    {
        return text.matches("[0-9]*");
    }

    //Position der Maus ermitteln und die Kugel an der Stelle platzieren

    private void updateMousePos() {

        ap.setOnMouseMoved(arg0 -> {

            xpos = arg0.getX();
            ypos = arg0.getY();

            mousePosX = xpos;
            mousePosY = ypos;

//				for (Kugel k : Kugel.list) {
//
//					k.posXY.x = xpos;
//					k.posXY.y = ypos;
//				}
        });
    }


    // Position und Geschwindigkeit der Kugeln updaten
    private void UpdateBall() {

        for (Kugel k : Kugel.list) {

            k.updateValues();
        }
    }


    // Beschleunigungsvektor zur�cksetzen auf standardwert
    private void ResetAcceleration() {

        for (Kugel k : Kugel.list) {

            k.beschleunigungXY.set(0.0, MainWindow.g);
        }
    }


    // Programm zur�cksetzen (Anfangszustand)
    public void zuruecksetzen(ActionEvent e) {
    }


}
