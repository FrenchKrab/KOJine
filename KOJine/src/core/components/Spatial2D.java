package core.components;

import core.Component;
import dataTypes.Vector2;

import java.awt.geom.AffineTransform;



//Great ressource: https://www.alanzucconi.com/2016/02/10/tranfsormation-matrix/

/*
 * A component that holds 2D spatial information (position, size, rotation)
 */
public class Spatial2D extends Component {
	
	public Spatial2D()
	{
		this.transform = new AffineTransform();
	}
	
	public Spatial2D(double x, double y, double angle)
	{
		this.transform = new AffineTransform();
		this.transform.setToTranslation(x, y);
		this.setRotation(angle);
	}
	
	/**
	 * Holds all spatial data
	 */
	private AffineTransform transform;
	
	
	
	@Override
	public boolean isUnique() {
		//There should never be two spatial 2D on a GameObject
		return true;
	}

	
	/**
	 * Get a copy of this spatial's Transform
	 * @return this spatial's Transform2D
	 */
	public AffineTransform getTransform()
	{
		return new AffineTransform(this.transform);
	}
	
	/**
	 * Set the transform of this spatial
	 * @param transform The new Transform2D
	 */
	public void setTransform(AffineTransform transform)
	{
		this.transform = transform;
	}
	
	/**
	 * Get this spatial's global position
	 * @return the spatial's position
	 */
	public Vector2 getPosition()
	{
		return new Vector2(this.transform.getTranslateX(), this.transform.getTranslateY());
	}
	
	/**
	 * Set this spatial's local position
	 * @param position the new position
	 */
	public void setPosition(Vector2 position)
	{
		this.setPosition(position.x, position.y);
	}
	
	public void setPosition(double x, double y)
	{
		double[] m = new double[6]; 
		this.transform.getMatrix(m);
		// m00 m10 m01 m11 m02 m12 
		this.transform.setTransform(m[0], m[1], m[2], m[3], x, y);
	}
	
	/**
	 * Translates globally
	 * @param x
	 * @param y
	 */
	public void translateGlobal(double x, double y)
	{
		Vector2 currentPos = this.getPosition();
		this.setPosition(currentPos.x + x, currentPos.y + y);
	}
	
	public void translateGlobal(Vector2 move) {
		translateGlobal(move.x, move.y);
	}
	
	/**
	 * Translates locally (according to local vectors)
	 * @param x
	 * @param y
	 */
	public void translate(double x, double y)
	{
		Vector2 v = this.right().multiply(x).add(this.up().multiply(y));
		this.translateGlobal(v);
		//this.transform.translate(x , y);
	}
	
	public void translate(Vector2 move) {
		this.translate(move.x, move.y);
	}
	
	
	/**
	 * Sets the rotation angle (in radians) of this spatial
	 * @param angle the new angle
	 */
	public void setRotation(double angle)
	{
		this.rotate(-this.getRotationAngle() + angle);
	}
	
	
	/**
	 * Rotates this transform from a certain amount of radians
	 * @param angle the amount of radians
	 */
	public void rotate(double angle)
	{	
		/*//Manual way to do it (less optimized)
		double[] m = new double[6]; 
		this.transform.getMatrix(m);
		// m00 m10 m01 m11 m02 m12
		Vector2 v1 = new Vector2(m[0], m[1]);	//(m00,m10), the horizontal vector
		Vector2 v2 = new Vector2(m[2], m[3]);	//(m01,m11), the vertical vector
		v1.rotate(angle);
		v2.rotate(angle);
		this.transform.setTransform(v1.x, v1.y, v2.x, v2.y, m[4], m[5]);
	 */
		this.transform.rotate(angle);
	}
	
	/**
	 * Multiply transform scale
	 * @param x
	 * @param y
	 */
	public void scale(double x, double y)
	{
		this.transform.scale(x, y);
	}
	
	public void scale(Vector2 scale)
	{
		this.transform.scale(scale.x, scale.y);
	}

	/**
	 * Get the current rotation angle
	 * @return rotation angle in radiants
	 */
	public double getRotationAngle()
	{
		double[] matrix = new double[4]; 
		this.transform.getMatrix(matrix);
		return Math.atan2(matrix[1], matrix[0]);	//atan2(m10, m00)
	}

	
	/**
	 * Gives the local up vector of this spatial
	 * @return the up vector
	 */
	public Vector2 up()
	{
		double[] m = new double[4]; 
		this.transform.getMatrix(m);
		return new Vector2(m[2], m[3]).getNormalized();
	}

	/**
	 * Gives the local right vector of this spatial
	 * @return the right vector
	 */
	public Vector2 right()
	{
		double[] m = new double[4]; 
		this.transform.getMatrix(m);
		
		return new Vector2(m[0], m[1]).getNormalized();
	}

	
}
