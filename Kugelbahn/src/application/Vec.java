package application;

import javafx.scene.shape.Line;
import org.joml.Vector2d;

public class Vec {
	
	private static Vector2d pr = new Vector2d();
	public static Vector2d Project(Vector2d v1, Vector2d v2)
	{
		pr.x = ((v2.dot(v1) * v2.x)/ v2.lengthSquared());
		pr.y = ((v2.dot(v1) * v2.y)/ v2.lengthSquared());

	    return pr;
	}
	
	
	public static double Angle(Vector2d v1, Vector2d v2) {
		return  Math.toDegrees(Math.acos((v1.dot(v2)) / (v1.length()*v2.length())));
	}

	public static double AngleTan(double s1, double s2) {
		
		return Math.toDegrees(Math.atan(Math.abs(s1) / Math.abs(s2))); 	// Winkel Phi
	}



	private static Vector2d lo = new Vector2d();
	public static Vector2d Normal(Vector2d line) {

		return lo.set(-(line.y), line.x);

	}


	private static Vector2d ec = new Vector2d();

	public static Vector2d ElasticCollison(double m1, Vector2d v1, double m2, Vector2d v2, double K) {

		ec.x = (((m1*v1.x + m2 * (2 * v2.x -v1.x)) / (m1 + m2)) * K);
		ec.y = (((m1*v1.y + m2 * (2 * v2.y -v1.y)) / (m1 + m2)) * K);

		return ec;
	}

	
	public static double ElasticCollison(double U, double Lpa, double K) {

		return (2.0 * U - Lpa) * K;
	}



	private static Vector2d vec = new Vector2d();
	public static Vector2d CreateVecOf(Wall line){

		return 	vec.set(line.getStartX() - line.getEndX(), line.getStartY() - line.getEndY());

	}
}
