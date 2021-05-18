package dataTypes;


/**
 * Class that represents a two dimensional vector with two doubles.
 */
public class Vector2 {
	
	public Vector2()
	{
		this.x=0;
		this.y=0;
	}
	
	public Vector2(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public double x;
	public double y;
	
	
	
	
	@Override
	public String toString() {
		super.toString();
		return "("+this.x+", "+this.y+")";
	}
	
	
	
	/**
	 * Clones the vector.
	 * @return A copy/clone of this vector
	 */
	public Vector2 clone()
	{
		return new Vector2(this.x, this.y);
	}
	
	//------Arthmical operations
	//---Add
	public Vector2 add(double x, double y)
	{
		return new Vector2(this.x+x, this.y+y);
	}
	
	public Vector2 add(Vector2 v)
	{
		return this.add(v.x, v.y);
	}
	
	public Vector2 substract(Vector2 v)
	{
		return this.add(-v.x, -v.y);
	}
	
	//---Multiply
	public Vector2 multiply(double x, double y)
	{
		return new Vector2(this.x*x, this.y*y);
	}
	
	public Vector2 multiply(double d)
	{
		return this.multiply(d,d);
	}
	
	public Vector2 multiply(Vector2 v)
	{
		return this.multiply(v.x, v.y);
	}
	
	//---Divide
	public Vector2 divide(double x, double y) 
	{
		if(x==0 || y==0)
		{
			//throw new Exception("Dividing a vector by ("+x+", "+y+") : DIVIDING BY ZERO");
			//TODO: do something ?
			return this.clone();
		}
		else
		{
			return new Vector2(this.x/x, this.y/y);
		}
	}
	
	public Vector2 divide(double d)
	{
		return this.divide(d,d);
	}
	
	public Vector2 divide(Vector2 v)
	{
		return this.divide(v.x, v.y);
	}
	
	//------ .. operations ?
	
	/**
	 * Returns the dot product (x1*x2+y1*y2) of this vector with a vector v.
	 * @param v The other vector
	 * @return The dot product as a double
	 */
	public double dot(Vector2 v)
	{
		return this.x*v.x + this.y*v.y;
	}

	
	/**
	 * Returns the cross product (x1*y2-x2*y1) of this vector with a vector v.
	 * @param v The other vector
	 * @return The cros product as a double.
	 */
	public double cross(Vector2 v)
	{
		return x * v.y - y * v.x;
	}
	
	//------ Geometrical operations
	
	public void rotate(double angle)
	{
		double costeta = Math.cos(angle);
		double sinteta = Math.sin(angle);
		double oldX = this.x;
		double oldY = this.y;
		this.x = oldX*costeta - oldY*sinteta;
		this.y = oldX*sinteta + oldY*costeta;
	}
	
	
	public double angle()
	{
		return Math.atan2(this.y, this.x);
	}
	
	/**
	 * Gives the magnitude of this {@link Vector2}
	 * magnitude=sqrt(x²+y²)
	 * @return The magnitude
	 */
	public double length ()
	{
		return Math.sqrt(this.x*this.x + this.y*this.y);
	}
	
	public double lengthSquared()
	{
		return this.x * this.x + this.y * this.y;
	}
	
	/**
	 * Normalize the {@link Vector2}
	 */
	public void normalize()
	{
		double magnitude = this.length();
		if(magnitude!=0)
		{
			this.x /= magnitude;
			this.y /= magnitude;
		}
	}
	
	
	/**
	 * Get a normalized copy of the {@link Vector2}
	 * @return A normalized copy of this {@link Vector2}
	 */
	public Vector2 getNormalized()
	{
		Vector2 clone = this.clone();
		clone.normalize();
		return clone;
	}
	
	/**
	 * Returns the distance between the positions represented by this
	 * vector and the vector v
	 * @param v	The other vector
	 * @return	Distance between this and v
	 */
	public double distanceTo(Vector2 v)
	{
		Vector2 offset = this.clone();
		offset.substract(v);
		return offset.length();
	}
	
	/**
	 * Returns the angle in radians between this vector and the other
	 * @param v
	 * @return
	 */
	public double angleTo(Vector2 v)
	{
		return Math.atan2(this.cross(v), this.dot(v));
	}
	
	
	public void clampLength(float maxLength)
	{
		double lengthSquared = this.lengthSquared();
		if(lengthSquared > maxLength * maxLength)
		{
			double length = Math.sqrt(lengthSquared);
			this.normalize();
			this.x *= maxLength;
			this.y *= maxLength;
		}
	}
	
	//------ Constant vectors
	
	public static Vector2 zero()
	{
		return new Vector2(0,0);
	}
	
	
	public static Vector2 up()
	{
		return new Vector2(0,1);
	}
	
	
	public static Vector2 down()
	{
		return new Vector2(0,-1);
	}
	
	
	public static Vector2 right()
	{
		return new Vector2(1,0);
	}
	
	
	public static Vector2 left()
	{
		return new Vector2(-1,0);
	}

}
