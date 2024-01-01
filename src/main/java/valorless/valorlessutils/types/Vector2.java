package valorless.valorlessutils.types;

/**
 * A generic class representing a 2D vector with components of a number type.
 *
 * @param <T> The type of the vector components, which must be number.
 */
public class Vector2<T extends Number> {
    
    public T x, y;

    /**
     * Constructs a Vector2 instance with the specified values for its X and Y components.
     *
     * @param x The value for the X component.
     * @param y The value for the Y component.
     */
    public Vector2(T x, T y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the value of a specific component in the vector.
     *
     * @param component The vector component to set (X or Y).
     * @param value The value to set for the specified component.
     */
    public void Set(Vector component, T value) {
        if (component == Vector.X) {
            x = value;
        }
        if (component == Vector.Y) {
            y = value;
        }
    }

    /**
     * Sets the values of both components in the vector.
     *
     * @param x The value for the X component.
     * @param y The value for the Y component.
     */
    public void Set(T x, T y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Checks if the current Vector2 instance is equal to another Vector3 instance.
     *
     * @param other The Vector2 instance to compare with.
     * @return True if all corresponding components are equal, false otherwise.
     */
    public Boolean Equals(Vector2<T> other) {
    	int i = 0;
    	if(other.x.equals(this.x)) i += 1;
    	if(other.y.equals(this.y)) i += 1;
    	if(i == 2) return true;
    	return false;
    }
}
