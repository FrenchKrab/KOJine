package dataTypes;

import java.awt.geom.AffineTransform;

import core.Component;


//Sources:
//https://github.com/godotengine/godot/blob/master/core/math/transform_2d.cpp
//https://www.alanzucconi.com/2016/02/10/tranfsormation-matrix/


/**
 * DEPRECATED: A try to represent 2D transforms, abandonned after the discovery of
 * the {@link AffineTransform} class
 * Most of this code is a translation from Godot's transform_2d.cpp class to Java 
 */
public class Transform2D {
	
	public Transform2D()
	{
		matrix[0]=Vector2.zero();
		matrix[1]=Vector2.zero();
		matrix[2]=Vector2.zero();
	}
	
	public Transform2D(Vector2 x, Vector2 y, Vector2 p)
	{
		matrix[0]=x;
		matrix[1]=y;
		matrix[2]=p;
	}
	
	public Transform2D(double angle, Vector2 position)
	{
		this.setRotation(angle);
		matrix[2]=position.clone();
	}
	
	
	
	private Vector2[] matrix = new Vector2[3];
	

	public Transform2D clone()
	{
		return new Transform2D(matrix[0].clone(), matrix[1].clone(), matrix[2].clone());
	}
	
	
	/**
	 * Transposed dot product with the x axis of the matrix
	 * @param v The other vector
	 * @return Transposed dot product with the x axis of the matrix
	 */
	public double tDotX(Vector2 v)
	{
		return this.matrix[0].x * v.x + matrix[1].x * v.y;
	}
	
	
	/**
	 * Transposed dot product with the y axis of the matrix
	 * @param v The other vector
	 * @return Transposed dot product with the y axis of the matrix
	 */
	public double tDotY(Vector2 v)
	{
		return this.matrix[0].y * v.x + matrix[1].y * v.y;
	}
	
	/**
	 * Returns a vector transformed by the matrix
	 * @param v A vector
	 * @return The vector transformed by the matrix
	 */
	public Vector2 xform(Vector2 v) 
	{
		return new Vector2(this.tDotX(v), tDotY(v)).add(this.matrix[2]);
	}
	
	
	public double basisDeterminant()
	{
		return matrix[0].x * matrix[1].y - matrix[0].y * matrix[1].x;
	}
	
	
	/**
	 * Set the rotation (in radians) of this transform
	 * @param angle New rotation angle (in radians)
	 */
	public void setRotation(double angle)
	{
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		matrix[0].x=cos;
		matrix[0].y=sin;
		matrix[1].x=-sin;
		matrix[1].y=cos;
	}
	
	
	/**
	 * Get the current rotation angle (in radians)
	 * @return The angle of this transform (in radians)
	 */
	public double getRotation()
	{
		double determinant = this.basisDeterminant();
		Transform2D m = orthonormalized();
		if (determinant < 0) 
		{
			m.scaleBasis(new Vector2(1, -1)); // convention to separate rotation and reflection for 2D is to absorb a flip along y into scaling.
		}
		return Math.atan2(m.matrix[0].y, m.matrix[0].x);
	}
	
	
	/**
	 * Scale the whole transform
	 * @param scale
	 */
	public void scale(Vector2 scale)
	{
		this.scaleBasis(scale);
		this.matrix[2] = this.matrix[2].multiply(scale);
	}
	
	
	/**
	 * Scale only the basis by the given vector
	 * @param scale Vector to scale by
	 */
	public void scaleBasis(Vector2 scale)
	{
		this.matrix[0] = this.matrix[0].multiply(scale);
		this.matrix[1] = this.matrix[1].multiply(scale);
	}
	
	
	
	public void translate(double x, double y)
	{
		this.matrix[2].x += x;
		this.matrix[2].y += y;
	}
	
	public void translate(Vector2 v)
	{
		this.translate(v.x, v.y);
	}
	
	
	public Transform2D translated(double x, double y)
	{
		Transform2D clone = this.clone();
		clone.translate(x, y);
		return clone;
	}
	
	public Transform2D translated(Vector2 v)
	{
		return this.translated(v.x, v.y);
	}
	
	
	public Transform2D rotated(double angle)
	{
		return (new Transform2D(angle, Vector2.zero())).multiply(this);
	}
	
	public void rotate(double angle)
	{
		Transform2D rotated = rotated(angle);
		this.matrix = rotated.matrix;
	}
	
	public void orthonormalize()
	{
		// Gram-Schmidt Process

		Vector2 x = matrix[0];
		Vector2 y = matrix[1];

		x.normalize();
		y = y.substract(x.multiply(x.dot(y)));
		y.normalize();

		matrix[0] = x;
		matrix[1] = y;
	}
	
	
	public Transform2D orthonormalized()
	{
		Transform2D t = this.clone();
		t.orthonormalize();
		return t;
	}
	
	
	public Transform2D multiply(Transform2D t2)
	{
		Transform2D t1 = this.clone();
		t1.matrix[2] = t1.xform(t2.matrix[2]);

		double x0, x1, y0, y1;

		x0 = t1.tDotX(t2.matrix[0]);
		x1 = t1.tDotY(t2.matrix[0]);
		y0 = t1.tDotX(t2.matrix[1]);
		y1 = t1.tDotY(t2.matrix[1]);

		t1.matrix[0].x = x0;
		t1.matrix[0].y = x1;
		t1.matrix[1].x = y0;
		t1.matrix[1].y = y1;
		
		return t1;
	}
}
