package application;

import java.util.ArrayList;
import java.util.Objects;
import org.joml.Vector2d;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Kugel {

	public static ArrayList<Kugel> list = new ArrayList<>(); // Liste mit allen erstellten Kugeln
	
    private Image im = new Image(Objects.requireNonNull(getClass().getResourceAsStream("Ironball.jpg")));

	public int id;
	public static int staticid;

	public boolean allowRot = true;

	public Pane ap;
	public double radius;
	public Vector2d posXY;
	public Vector2d geschwindigkeitXY;
	public Vector2d beschleunigungXY = new Vector2d();
	public Vector2d beschleunigungXYP = new Vector2d();

	private double gewicht;
	private Circle circle;
	public Line directionVec;

	Circle editC;
	
	public Kugel(double radius, double gewicht, Vector2d posXY, Vector2d geschwindigkeitXY, Pane ap) {

		staticid++;
		this.id = staticid;

		this.circle = new Circle();
		this.directionVec = new Line();
		
		if(MainWindow.rVec) {
			this.directionVec.setStroke(Color.RED);
		}else {
			this.directionVec.setStroke(Color.TRANSPARENT);
		}

		this.radius = radius;
		this.gewicht = gewicht;
		this.posXY = posXY;
		this.geschwindigkeitXY = geschwindigkeitXY.mul(Main.PixelPerMeter);
		
		this.circle.setCenterX(this.posXY.x);
		this.circle.setCenterY(this.posXY.y);
		this.circle.setRadius(this.radius);
		this.circle.setFill(new ImagePattern(im));

		this.ap = ap;
		
		editC = new Circle();
		editC.setCenterX(this.posXY.x);
		editC.setCenterY(this.posXY.y);
		editC.setRadius(1 + radius/2);
		editC.setFill(Color.TRANSPARENT);

		ap.getChildren().add(circle);
		ap.getChildren().add(directionVec);
		ap.getChildren().add(editC);

		list.add(this);
		reset.set(posXY);
	}



	public void updatePos(double px, double py){

		this.posXY.set(px, py);
	}



	//Wind
	public void setWind(double vecX, double vecY) {

		addAcceleration(vecX, vecY); //Zum Beschleunigungsvektor den Windvektor addieren
	}


	public void update() {
		
		//Wind	
		setWind(MainWindow.wind, 0.0);	 // Windvektor wird zum Beschleunigunsvektor addiert

		updateDirectionVec();
		rotateBall();
		ballCollisionHandling();
	}
	
	

	//
	Vector2d v2 = new Vector2d();
	public Vector2d getVelocity() {
		
		v2.set(geschwindigkeitXY.x, geschwindigkeitXY.y);
		return v2;
	}
	
	
	// Richtungsvektor der Kugel anzeigen, updaten
	public void updateDirectionVec() {

		directionVec.setStartX(getPos().x);
		directionVec.setStartY(getPos().y);

		directionVec.setEndX(getPos().x + getVelocity().x);
		directionVec.setEndY(getPos().y + getVelocity().y);
	}
	
	
	
	
	
	
	
	
	//Kugelposition im Bearbeitungsmodus ändern
	public void editBallPos() {
		
		editC.setOnMouseDragged(arg0 -> {

			// Wenn der Bearbeitungs-Modus an ist
			if(!MainWindow.bea) {
				return;
			}

			//Blauer Kreis mit der Maus bewegen
			editC.setCenterX(arg0.getX());
			editC.setCenterY(arg0.getY());

			//Position nach dem bearbeiten der blaue Kugel updaten
			posXY.x = editC.getCenterX();
			posXY.y = editC.getCenterY();

			// Die Kugel mit der Maus bewegen
			circle.setCenterX(arg0.getX());
			circle.setCenterY(arg0.getY());

			//Position nach dem bearbeiten der Kugel updaten
			posXY.x = circle.getCenterX();
			posXY.y = circle.getCenterY();
		});
	}
	
	
	

	

	
	
	//Beschleunigungsvektor
	Vector2d v3 = new Vector2d();
	public Vector2d getVelocityMeter() {
		
		v2.set(geschwindigkeitXY.x/Main.PixelPerMeter, geschwindigkeitXY.y/Main.PixelPerMeter);
		return v3;
	}
	






	//position und Geschwindigkeit der Kugel updaten am Ende des Frames
	private double frameTime = Main.frametime;
	public void updateValues() {

		beschleunigungXYP.x = beschleunigungXY.x * Main.PixelPerMeter;
		beschleunigungXYP.y = beschleunigungXY.y * Main.PixelPerMeter;

		//Strecke (Position)
		posXY.x = posXY.x + geschwindigkeitXY.x * frameTime + 0.5 * beschleunigungXYP.x * (frameTime * frameTime);
		posXY.y = posXY.y + geschwindigkeitXY.y * frameTime + 0.5 * beschleunigungXYP.y * (frameTime * frameTime);
		
		//Geschwindigkeit
		geschwindigkeitXY.x = geschwindigkeitXY.x + (beschleunigungXYP.x * frameTime);
		geschwindigkeitXY.y = geschwindigkeitXY.y + (beschleunigungXYP.y * frameTime);

		//Kugel Pos. setzen
		circle.setCenterX(posXY.x);
		circle.setCenterY(posXY.y);

		editC.setCenterX(this.posXY.x);
		editC.setCenterY(this.posXY.y);
	}


	// Hier mit wird das Element auf den angegebenen Winkel gedreht
	public void rotate(boolean rot){

		allowRot=rot;
	}



	// Hier mit wird die Drehbewegung der Kugel realisiert
	double ro = 0;
	public void rotateBall() {

		if(!allowRot){
			return;
		}
		//Winkelgeschwindigkeit
		//Um wie viel die Kugel pro Frame gedreht werden muss
		ro +=  (Math.toDegrees((geschwindigkeitXY.x / (radius/100.0)))) * (Main.frametime);

		//Kugel bzw. Textur rotieren
		circle.setRotate(ro*(1.0/(60.0 + Main.PixelPerMeter)));
	}




	private double distance, overlap;

	// Ob sich die Kugeln überlappen bzw berühren
	private Vector2d v = new Vector2d();
	private boolean doBallsOverlap(Vector2d vec1, double r1, Vector2d vec2, double r2) {
		
		v.set(vec1.x - vec2.x, vec1.y - vec2.y); // Die Distanz zweier Kugeln berechnen
		return (v.length()) <= (r1 + r2);
	}



	// Verhindert, dass die Kugeln sich überlappen
	public void preventOverlap(Kugel k) {
		
		// Abstand zwischen den 2 Bällen (von beiden Mittelpunkten)
		distance = v.length();

		// überlappung berechnen
		overlap = 0.5 * (distance - this.radius - k.radius);

		// Kugeln neu positionieren
		this.posXY.x -= overlap * (this.posXY.x - k.posXY.x) / distance;
		this.posXY.y -= overlap * (this.posXY.y - k.posXY.y) / distance;
		k.posXY.x += overlap * (this.posXY.x - k.posXY.x) / distance;
		k.posXY.y += overlap * (this.posXY.y - k.posXY.y) / distance;
	}



	private Vector2d tan = new Vector2d();
	private Vector2d nor = new Vector2d();
	private double Lpr1, Lpr2, Lpa1, Lpa2;
	private double Lpa1N, Lpa2N;
	private double U;

	public void rebounce(Kugel k) {
		
		// Normale
		nor.set(k.posXY.x - this.posXY.x, k.posXY.y - this.posXY.y).normalize();

		// Tangente
		tan.set(Vec.Normal(nor));

		// Länge der Projektion orthogonal zur Normalen
		Lpr1 = this.geschwindigkeitXY.x * tan.x + this.geschwindigkeitXY.y * tan.y;
		Lpr2 = k.geschwindigkeitXY.x * tan.x + k.geschwindigkeitXY.y * tan.y;

		// Länge vom Vektor Parallel zu Normalen
		Lpa1 = this.geschwindigkeitXY.x * nor.x + this.geschwindigkeitXY.y * nor.y;
		Lpa2 = k.geschwindigkeitXY.x * nor.x + k.geschwindigkeitXY.y * nor.y;
		
		// Lpa1 und Lpa2 mit Elastischer-Stoß-Formel neu berechnen
		U = ((this.gewicht * Lpa1 + k.gewicht * Lpa2) / (this.gewicht + k.gewicht));
		Lpa1N = Vec.ElasticCollison(U, Lpa1, 0.9);
		Lpa2N = Vec.ElasticCollison(U, Lpa2, 0.9);

		// Vektoren aus Länge und Richtung neu zusammensetzen für die Geschwindigkeit und Richtung nach der Kollision
		this.geschwindigkeitXY.x = tan.x * Lpr1 + nor.x * Lpa1N;
		this.geschwindigkeitXY.y = tan.y * Lpr1 + nor.y * Lpa1N;
		k.geschwindigkeitXY.x = tan.x * Lpr2 + nor.x * Lpa2N;
		k.geschwindigkeitXY.y = tan.y * Lpr2 + nor.y * Lpa2N;
	}

	
	private Vector2d Vrel = new Vector2d();
	private Vector2d nb = new Vector2d();
	
	private void ballCollisionHandling() {

		for (Kugel k : list) {
			if (this.id != k.id) { // Kugel soll sich nicht selber überprüfen
				if (doBallsOverlap(this.posXY, this.radius, k.posXY, k.radius)) {

					// überlappen der Kugeln verhindern
					preventOverlap(k);
					
					// Relative Bewegung der Kugeln zueinander
					Vrel.set(this.getVelocity().sub(k.getVelocity()));
					
					// Normale der Kugel
					nb.set(k.posXY.x - this.posXY.x, k.posXY.y - this.posXY.y);
							
					// Kontakt
					if(Vrel.dot(nb) == 0) {
						// System.out.println("Kontakt");
					}
					// Kollision
					if(Vrel.dot(nb) > 0) {

						// Neuen Geschwindikeitsvektor nach Kollision berechnen und neu setzen
						rebounce(k);
					}
				}
			}
		}
	}
	
	
	


	
	
	String info;
	public String getInfos(double decimalPlaces) {
		
		info = 
		"t: " + Math.round(MainWindow.zeit*Math.pow(10.0, decimalPlaces))/Math.pow(10.0, decimalPlaces) + " | " +
		"ID: " + id + " | " + 
		"v: " + Math.round(getVelocity().x/50.0*Math.pow(10.0, decimalPlaces))/Math.pow(10.0, decimalPlaces) + "/" + Math.round(-getVelocity().y/50.0*Math.pow(10.0, decimalPlaces))/Math.pow(10.0, decimalPlaces) + " | " + 
		"a: " + Math.round(getAcceleration().x*Math.pow(10.0, decimalPlaces))/Math.pow(10.0, decimalPlaces) + "/" + Math.round(-getAcceleration().y*Math.pow(10.0, decimalPlaces))/Math.pow(10.0, decimalPlaces) + " | " + 
		"s: " + Math.round(getPosMeter().x*Math.pow(10.0, decimalPlaces))/Math.pow(10.0, decimalPlaces) + "/" + Math.round(((MainWindow.frameY/Main.PixelPerMeter) - getPosMeter().y)*Math.pow(10.0, decimalPlaces))/Math.pow(10.0, decimalPlaces);
		return info;
	}



	public void setTexturedText(String text){

		Image im = new Image(Objects.requireNonNull(getClass().getResourceAsStream(text)));
		this.circle.setFill(new ImagePattern(im));
	}


	public void setVelocity(Vector2d v) {

		geschwindigkeitXY = v;
	}

	public void setVelocity(double vx, double vy) {

		geschwindigkeitXY.x = vx;
		geschwindigkeitXY.y = vy;

	}
	
	
	
	public Vector2d getAccleration() {
		
		return beschleunigungXY;
	}
	
	public void setAcceleration(double ax, double ay) {
		
		beschleunigungXY.x =  (ax);
		beschleunigungXY.y =  (ay);			
	}
	
	public void setAcceleration(Vector2d vec) {
		
		beschleunigungXY.x =  (vec.x);
		beschleunigungXY.y =  (vec.y);			
	}
	
	
	public void addAcceleration(double ax, double ay) {
		
		beschleunigungXY.x += ax;
		beschleunigungXY.y += ay;		
	}
	
	
	
	
	public int getBallID() {
		return id;
	}

	public double getBallRadius() {

		return this.radius;
	}

	public void setAccelerationX(double vX) {

		this.beschleunigungXY.x = vX;
	}

	public void setAccelerationY(double vY) {

		this.beschleunigungXY.y = vY;
	}

	public Vector2d getAcceleration() {

		return beschleunigungXY;
	}

	public void addPos(Vector2d addpos) {

		posXY = posXY.add(addpos);
	}
	
	public void addPosX(double addposX) {

		posXY.x = posXY.x + addposX;
	}

	public void addPosY(double addposY) {

		posXY.y = posXY.y + addposY;
	}

	public Vector2d getPos() {

		return posXY;
	}
	
	Vector2d posMeter = new Vector2d();
	public Vector2d getPosMeter() {

		posMeter.x = posXY.x / Main.PixelPerMeter;
		posMeter.y = posXY.y / Main.PixelPerMeter;

		return posMeter;
	}

	public double getGewicht() {

		return this.gewicht;
	}

	public void add(AnchorPane ap) {

		ap.getChildren().addAll(this.circle);
	}

	public void setColor(Paint color) {

		this.circle.setFill(color);
	}

	public final Vector2d reset = new Vector2d();

	public Circle getKugel(){
		return circle;
	}
}
