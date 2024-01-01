package valorless.valorlessutils.types;

/**
 * A generic class representing a 3D vector with components of a number type.
 *
 * @param <T> The type of the vector components, which must be number.
 */
public class Vector3<T extends Number> {
    
    public T x, y, z;

    /**
     * Constructs a Vector3 instance with the specified values for its X, Y, and Z components.
     *
     * @param x The value for the X component.
     * @param y The value for the Y component.
     * @param z The value for the Z component.
     */
    public Vector3(T x, T y, T z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Sets the value of a specific component in the vector.
     *
     * @param component The vector component to set (X, Y, or Z).
     * @param value The value to set for the specified component.
     */
    public void Set(Vector component, T value) {
        if (component == Vector.X) {
            x = value;
        }
        if (component == Vector.Y) {
            y = value;
        }
        if (component == Vector.Z) {
            z = value;
        }
    }

    /**
     * Sets the values of all components in the vector.
     *
     * @param x The value for the X component.
     * @param y The value for the Y component.
     * @param z The value for the Z component.
     */
    public void Set(T x, T y, T z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Checks if the current Vector3 instance is equal to another Vector3 instance.
     *
     * @param other The Vector3 instance to compare with.
     * @return True if all corresponding components are equal, false otherwise.
     */
    public Boolean Equals(Vector3<T> other) {
    	int i = 0;
    	if(other.x.equals(this.x)) i += 1;
    	if(other.y.equals(this.y)) i += 1;
    	if(other.z.equals(this.z)) i += 1;
    	if(i == 3) return true;
    	return false;
    }
}
