package application;

import java.util.ArrayList;
import java.util.Objects;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import org.joml.Vector2d;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Wall {

	public static ArrayList<Wall> list = new ArrayList<>();

	private Line line;
	public Line dLine;
	private double strokeWidth;
	private double weight = 2000.0;
	private Vector2d VLine = new Vector2d(0.0);
//
//	public Circle cStart;
//	public Circle cEnd;
	
	public Pane pane;

	public double rotation;

	private double startX, startY, endX, endY;	
	private boolean solid;

	public Vector2d getBallVelByCollision = new Vector2d();
    private Image im2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("Wood.jpg")));

	public Wall(double startX, double startY, double endX, double endY, double dicke, Pane p, boolean solid) {
		
		this.solid = solid;
	
		this.line = new Line();
		this.dLine = new Line();
		this.strokeWidth = dicke;
		
		this.pane = p;
		
		this.startX = startX;
		this.endX = endX;
		this.startY = startY;
		this.endY = endY;
		
		line.setStartX(MainWindow.rlX+startX);
		line.setStartY(MainWindow.rlY+startY);
		line.setEndX(endX+MainWindow.rlX);
		line.setEndY(endY+MainWindow.rlY);
		line.setStrokeWidth(dicke);

		line.setStroke(new ImagePattern(im2));
		line.setFill(new ImagePattern(im2));
		
//		cStart = new Circle();
//		cEnd = new Circle();
//
//		cStart.setLayoutX(MainWindow.rlX + startX);
//		cStart.setLayoutY(MainWindow.rlY + startY);
//		cStart.setRadius(5 + dicke/3);
//		cStart.setFill(Color.TRANSPARENT);
//
//		cEnd.setLayoutX(endX + MainWindow.rlX);
//		cEnd.setLayoutY(endY + MainWindow.rlY);
//		cEnd.setRadius(5 + dicke/3);
//		cEnd.setFill(Color.TRANSPARENT);
		
		dLine.setStroke(Color.TRANSPARENT);

		list.add(this);
		p.getChildren().add(line);
		p.getChildren().add(dLine);
//		p.getChildren().add(cStart);
//		p.getChildren().add(cEnd);
	}


	private double rollreibungskoeffizient = 0.04; // Stahl(Kugel) auf Holz(Ebene)
	private double ballX, ballY;
	private double alpha, phi;
	private double Fgh, Fgn, friction;
	private double edgeStart, edgeEnd, depth;

	private Vector2d LineVec = new Vector2d();
	private Vector2d LineVecNorm = new Vector2d();
	private Vector2d down = new Vector2d(0, -1);

	private Vector2d projection = new Vector2d();
	private Vector2d ClosestDisVec = new Vector2d();
	private Vector2d LineVector = new Vector2d();
	private Vector2d cPoint = new Vector2d();
	private Vector2d LineBallVecEnd = new Vector2d();
	private Vector2d LineBallVecStart = new Vector2d();
		
	private Vector2d LineVecNormal = new Vector2d();
	private Vector2d DirVec = new Vector2d();
	
	private Vector2d frictionVec = new Vector2d();
	private Vector2d downhillVec = new Vector2d();
	
	private Vector2d Vrel = new Vector2d();
	private Vector2d nb = new Vector2d();


	public Line getLine(){

		return line;
	}



	 Vector2d dv = new Vector2d();
	public Vector2d defaultStartVec(){

		dv.set(startX+20, startY+20);

//		cStart.setLayoutX(startX+20);
//		cStart.setLayoutY(startY+20);

		return dv;
	}


	 Vector2d dv2 = new Vector2d();
	public Vector2d defaultEndVec(){

		dv2.set(endX+20, endY+20);
//		cEnd.setLayoutX(endX+20);
//		cEnd.setLayoutY(endY+20);

		return dv2;
	}



/**
	//Zum ändern der Linien
	public void editLinePos() {
					
		cStart.setOnMouseDragged(arg0 -> {

			// Nur wenn der Bearbeiten-Modus an ist
			if(!MainWindow.bea) {
				return;
			}

			// Position des blauen Kreises wird geupdated
			cStart.setLayoutX(cStart.getLayoutX() + arg0.getX());
			cStart.setLayoutY(cStart.getLayoutY() + arg0.getY());

			// Position des Startpunkts der Linie geupdated
			line.setStartX(cStart.getLayoutX());
			line.setStartY(cStart.getLayoutY());

		});
				
		cEnd.setOnMouseDragged(arg0 -> {

			// Nur wenn der Bearbeiten-Modus an ist
			if(!MainWindow.bea) {
				return;
			}

			// Position des blauen Kreises wird geupdated
			cEnd.setLayoutX(cEnd.getLayoutX() + arg0.getX());
			cEnd.setLayoutY(cEnd.getLayoutY() + arg0.getY());

			// Position des Endpunkts der Linie geupdated
			line.setEndX(cEnd.getLayoutX());
			line.setEndY(cEnd.getLayoutY());
		});
	}
	**/


	public void rotateAtStart(double angle){

		rotation += angle;
		rotateLineStart(rotation);
	}

	public void rotateAt(double angle){

		rotation += angle;
		rotateLine(rotation);
	}
	

	public void update() {

		if(!solid) {
			return;
		}
		
		//LinienVektor
		LineVec.set(line.getStartX() - line.getEndX(), line.getStartY() - line.getEndY());	
		
		//Normierter LinienVektor
		LineVecNorm.set(LineVec.normalize());
		
		//Normierte Linien Normale			
		LineVecNormal.set(Vec.Normal(LineVecNorm));	
		
		alpha = Vec.Angle(LineVecNormal, down);
		nb.set(LineVecNormal);

		getBallVelByCollision.x = 0;
		getBallVelByCollision.y = 0;

		for (Kugel k : Kugel.list) {

			// X und Y Position der Kugel
			ballX = k.getPos().x;
			ballY = k.getPos().y;

			//Richtungsvektor der Kugel
			DirVec.set(k.getVelocity());

			//Relative Bewegung der Objekte zueinander
			Vrel.set(DirVec.sub(LineVec));

			drawDistanceline();
						
			// N�he Pr�fen
			if (checkProximity(k)) {

				// Kontakt
				if(Vrel.dot(nb.negate()) == 0) {
//					System.out.println("Kontakt");
				}
					
				// Kollision
				if(Vrel.dot(nb) > 0 || Vrel.dot(nb) < 0) {

					// Abprallen
					rebounce(k);

					// Position der Kugel korrigieren
					correctBallPosition(k);

					// Kugel auf Linie rollen lassen
					rollMovement(k);
				}
			}
		}
	}



	private double t = 0.75;
	private boolean checkProximity(Kugel k) {

		return getClosestVec().length()-t <= (k.getBallRadius() + line.getStrokeWidth() / 2.0);
	}



	
	private Vector2d getClosestVec() {

		LineVector.set(line.getStartX() - line.getEndX(), line.getStartY() - line.getEndY());	// Vektor von Anfangspunkt zum Endpunkt der Linie
		LineBallVecStart.set(ballX - line.getStartX(), ballY - line.getStartY()); 				// Vektor von Anfangspunkt der Linie zum Ballmittelpunkt
		LineBallVecEnd.set(ballX - line.getEndX(), ballY - line.getEndY());						// Vektor von Endpunkt der Linie zum Ballmittelpunkt

		edgeEnd = LineVector.dot(LineBallVecEnd);												// Kugel rechts der Linie --> < 0	
		edgeStart = LineVector.negate().dot(LineBallVecStart);									// Kugel links der Linie  --> < 0

		// Projektionvektor: LineBallVecStart auf LineVector projezieren
		projection.set(Vec.Project(LineBallVecStart, LineVector));
		
		// K�rzeste distanz von Kugel zur Linie
		ClosestDisVec.set(LineBallVecStart.sub(projection));
			

		if (edgeStart < 0) {																	// Wenn die Kugel vor dem Anfangspunkt der Linie ist...

			return cPoint.set(line.getStartX() - ballX, line.getStartY() - ballY);				// ist der n�chste Punkt der Anfangspunkt der Linie

		} else if (edgeEnd < 0) {																// Wenn die Kugel hinter dem Endpunkt der Linie ist...

			return cPoint.set(line.getEndX() - ballX, line.getEndY() - ballY);					// ist der n�chste Punkt der Endpunkt der Linie

		} else {																				// Wenn beides nicht...
			return cPoint.set(ClosestDisVec.mul(-1));											// ist der n�chste Punkt der Projektionspunkt 
		}		
	}




	
	
	
	// Kugel an der Linie neu positionieren
	private void correctBallPosition(Kugel k) {
		
		depth = cPoint.length()-t - k.getBallRadius() - line.getStrokeWidth()/2; 	// Wie tief die Kugel in der Linie ist
		k.posXY.set(k.posXY.add(cPoint.normalize().mul(depth))); 					// Position der Kugel in Richtung der Liniennormalen bewegen
	}
	
	

	
	
	
	private Vector2d Vpr = new Vector2d();
	private Vector2d Vpa = new Vector2d();
	private Vector2d Vn = new Vector2d();


	private void rebounce(Kugel k) {

		// Projektion orthogonal zur Normalen
		Vpr = Vec.Project(k.getVelocity(), LineVec.normalize());

		// Parallel zu Normalen
		Vpa = k.getVelocity().sub(Vpr);


		//Abbruchbedingung
		if(Vpa.length() < 0.5 * Main.PixelPerMeter) {
			return;
		}

		// Elastischer Sto�
		Vpa = Vec.ElasticCollison(k.getGewicht(), Vpa, weight, VLine, 0.2);

//		bounceVec.x = Vpa.x*-1.8;
//		bounceVec.y = Vpa.y*-1.8;

		getBallVelByCollision.x = k.geschwindigkeitXY.x;
		getBallVelByCollision.y = k.geschwindigkeitXY.y;


		// Neuer Beschleunigungsvektor zusammengesetzt
		Vn = Vpr.add(Vpa);
			
		//Neuen Beschleunigungsvektor setzen
		k.setVelocity(Vn.x, Vn.y);
	}







	private void rollMovement(Kugel k) {
		
		
		if(LineVecNormal.dot(DirVec) < 0) {

			k.setAcceleration((Fgh(alpha).add(friction())));	// Rollen auf schiefer Ebene mit Reibung
		}
	}
	
	
	
	// Hangabtriebskraft
	private Vector2d Fgh(double alpha) {
		
		Fgh = -MainWindow.g * Math.sin(Math.toRadians(alpha)); 					// Hangabtriebskraft berechnen
		Fgh*= (line.getStartY() <= line.getEndY()) ? 1 : -1;  					// Richtung der Hangabtriebskraft

		downhillVec.set(LineVecNorm.x * Fgh, LineVecNorm.y * Fgh);				// Beschleunigungsvektor in Richtung der Hangabtriebskraft

		return downhillVec;
	}
		
	
	
	
	// Reibung
	private Vector2d friction() {
		
		friction = Fgn(alpha) * rollreibungskoeffizient;						// Reibung
		friction*= DirVec.x < 0 ? 1 : -1;										// Reibung entgegen dem Richtungsvektor 
		
		phi = Vec.AngleTan(friction, Fgn);										// Winkel Phi

		frictionVec.set(LineVecNorm.x * friction, LineVecNorm.y * friction);	// Beschleunigungsvektor der Reibung
		return frictionVec;
	}
	
	
	
	// Normalkraft
	private double Fgn(double alpha) {

		return Fgn = -MainWindow.g * Math.cos(Math.toRadians(alpha));
	}
	
	
	
	// Linien am Mittelpunkt rotieren
	private Vector2d vec = new Vector2d();
	public void rotateLine(double rotation) {
		
		vec.set(startX - endX, startY - endY); // Vektor der Linie berechnen

		// Mittelpunkt der Linie berechnen
		double middleX = (this.startX+this.endX)/2;
		double middleY = (this.startY+this.endY)/2;

		// Linie am Ursprungspunkt verschieben, sp�ter f�r die rotation
		double ox = startX-middleX;
		double oy = startY-middleY;

		// Linie rotieren
		double rotatedX = ox*Math.cos(Math.toRadians(rotation)) - oy*Math.sin(Math.toRadians(rotation));
		double rotatedY = ox*Math.sin(Math.toRadians(rotation)) + oy*Math.cos(Math.toRadians(rotation));

		// Linie mit rotation wieder auf die ursprungsposition verschieben
		line.setStartX(rotatedX + 20 + middleX);
		line.setStartY(rotatedY + 20 + middleY);
		line.setEndX(-rotatedX + 20 + middleX);
		line.setEndY(-rotatedY + 20 + middleY);
	}




	// Linien um den Startpunkt rotieren
	private Vector2d vec2 = new Vector2d();
	public void rotateLineStart(double rotation) {

		vec2.set(startX - endX, startY - endY); // Vektor der Linie berechnen

		// Mittelpunkt der Linie berechnen
		double middleX = (this.endX);
		double middleY = (this.endY);

		// Linie am Ursprungspunkt verschieben, sp�ter f�r die rotation
		double ox = startX-middleX;
		double oy = startY-middleY;

		// Linie rotieren
		double rotatedX = ox*Math.cos(Math.toRadians(rotation)) - oy*Math.sin(Math.toRadians(rotation));
		double rotatedY = ox*Math.sin(Math.toRadians(rotation)) + oy*Math.cos(Math.toRadians(rotation));

		// Linie mit rotation wieder auf die ursprungsposition verschieben
		line.setStartX(rotatedX + 20 + this.endX);
		line.setStartY(rotatedY + 20 + this.endY);
	}
	


	private Vector2d v = new Vector2d();
	public Vector2d getVec(){

		v.set(line.getStartX() - line.getEndX(), line.getStartY() - line.getEndY());
		return v;
	}
	
	
	// Abstandslinie von Kugel und Linie erstellen und anzeigen
	private void drawDistanceline() {

		if(dLine.getFill() == Color.TRANSPARENT) {
			return;
		}
		
		if (edgeStart < 0) {
			dLine.setStartX(ballX);
			dLine.setStartY(ballY);
			dLine.setEndX(line.getStartX());
			dLine.setEndY(line.getStartY());
			
		} else if (edgeEnd < 0) {

			dLine.setStartX(ballX);
			dLine.setStartY(ballY);
			dLine.setEndX(line.getEndX());
			dLine.setEndY(line.getEndY());
			
		} else {
			
			dLine.setStartX(ballX);
			dLine.setStartY(ballY);
			dLine.setEndX(ballX + ClosestDisVec.x);
			dLine.setEndY(ballY + ClosestDisVec.y);
		}	
	}

	
	
	public double getStrokeWidth() {
		return strokeWidth;
	}


	public double getStartX(){

		return line.getStartX();
	}

	public double getStartY(){

		return line.getStartY();
	}

	public double getEndX(){

		return line.getEndX();
	}

	public double getEndY(){

		return line.getEndY();
	}


	public void setEndX(double ex){

		line.setEndX(ex);
	}


	public void setEndY(double ey){

		line.setEndY(ey);
	}


	public void setStartX(double sx){

		line.setStartX(sx);
	}


	public void setStartY(double sy){

		line.setStartY(sy);
	}

	public void setStart(double sx, double sy){

		line.setStartX(sx);
		line.setStartY(sy);
	}

	public void setEnd(double ex, double ey){

		line.setEndX(ex);
		line.setEndY(ey);
	}


	public void setVelocity(double vx, double vy){

		VLine.x = vx;
		VLine.y = vy;
	}

	public Vector2d getVelocity(){

		return VLine;
	}

}
