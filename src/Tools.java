public class Tools {

	/* Returns double the area of the triangle created by a, b, and c .
	 * @return < 0:  the points are oriented clockwise.
	 * @return == 0: the points lie on the same line.
	 * @return > 0:  the points are oriented counter-clockwise. */
	public static float area2(Vector2 a, Vector2 b, Vector2 c) {
		return (a.x - c.x) * (b.y - c.y) - (a.y - c.y) * (b.x - c.x);
	}
	
	/* Returns true if the 3 Vector2s (points) are oriented clockwise */
	public static boolean isCW(Vector2 a, Vector2 b, Vector2 c) {
		return (area2(a, b, c) < 0);
	}
}

/* A simple data structure for storing to floats. Useful for point coordinates. */
class Vector2 {
	public float x, y; // TODO this should probably be private with getters and setters, but its fine as is currently.
	
	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}
}