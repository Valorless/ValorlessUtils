package valorless.valorlessutils.json;

import org.checkerframework.checker.initialization.qual.Initialized;
import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Utilities related to unchecked casts and nullness suppressions.
 * <p>
 * These utilities should be used sparsely, only if no better solution is available.
 */
public final class Unsafe {

	/**
	 * Performs an unchecked cast to the specified target type.
	 * <p>
	 * This can be used to suppress certain false-positive type checking warnings of the Java
	 * compiler or other tools when the object is known to be of the specified type.
	 * 
	 * @param <T>
	 *            the target type
	 * @param object
	 *            the object to cast
	 * @return the casted object
	 */
	@SuppressWarnings({ "unchecked", "cast.unsafe" })
	public static <T> T cast(@UnknownInitialization @Nullable Object object) {
		return (T) object;
	}

	/**
	 * Performs an unchecked cast to the specified non-<code>null</code> target type.
	 * <p>
	 * This can be used to suppress certain false-positive type checking warnings of the Java
	 * compiler or other tools when the object is known to be of the specified non-<code>null</code>
	 * type.
	 * 
	 * @param <T>
	 *            the target type
	 * @param object
	 *            the object to cast
	 * @return the casted object
	 */
	@EnsuresNonNull("#1")
	public static <T> @NonNull T castNonNull(@UnknownInitialization @Nullable Object object) {
		assert object != null : "@AssumeAssertion(nullness)";
		return cast(object);
	}

	/**
	 * Performs an unchecked cast to the non-<code>null</code> variant of the specified type.
	 * <p>
	 * This can be used to suppress certain false-positive type checking warnings of the Java
	 * compiler or other tools when the object is known to not be <code>null</code>.
	 * <p>
	 * To intentionally 'disguise' a <code>null</code> value as non-<code>null</code> (for example
	 * in tests), use {@link Unsafe#uncheckedNull()} or {@link #nullableAsNonNull(Object)} instead.
	 * 
	 * @param <T>
	 *            the target type
	 * @param object
	 *            the object to cast
	 * @return the casted object
	 */
	@EnsuresNonNull("#1")
	public static <T> @NonNull T assertNonNull(@Nullable T object) {
		assert object != null : "@AssumeAssertion(nullness)";
		return cast(object);
	}

	/**
	 * Performs an unchecked cast to the {@link Initialized} non-<code>null</code> variant of the
	 * specified type.
	 * <p>
	 * This can be used to suppress certain type checking warnings of compiler tools when it is safe
	 * to treat the given object as already fully initialized (e.g. when it is known that the object
	 * reference is only first used once the object has been fully initialized).
	 * 
	 * @param <T>
	 *            the target type
	 * @param object
	 *            the object to cast
	 * @return the casted object
	 */
	@EnsuresNonNull("#1")
	public static <T> @Initialized @NonNull T initialized(@UnknownInitialization @Nullable T object) {
		assert object != null : "@AssumeAssertion(nullness)";
		return cast(object);
	}

	/**
	 * Casts the given object to the nullable variant of the specified type.
	 * <p>
	 * This can be used to cast a known to be falsely non-<code>null</code> typed object to its
	 * corresponding nullable type.
	 * 
	 * @param <T>
	 *            the target type
	 * @param object
	 *            the object to cast
	 * @return the casted object
	 */
	public static <T> @Nullable T nullable(@Nullable T object) {
		return object;
	}

	/**
	 * Returns a <code>null</code> value that bypasses compiler and tooling checks.
	 * <p>
	 * This can be used in cases in which <code>null</code> is known to be a valid value but is
	 * disallowed by the type system.
	 * <p>
	 * Another use case is to initialize non-<code>null</code> typed fields that are known to be
	 * properly set to a non-<code>null</code> value before they are accessed normally for the first
	 * time.
	 * <p>
	 * Another use case are tests that may want to intentionally pass <code>null</code> to a method
	 * that usually does not expect <code>null</code> as an argument.
	 * 
	 * @param <T>
	 *            the target type
	 * @return the <code>null</code> value
	 */
	public static <T> @NonNull T uncheckedNull() {
		return Unsafe.cast(null);
	}

	/**
	 * Performs an unchecked cast to the non-<code>null</code> variant of the specified type.
	 * <p>
	 * This can be used to suppress certain false-positive type checking warnings of the Java
	 * compiler or other tools, for example in cases in which <code>null</code> is known to be a
	 * valid value but is disallowed by the type system.
	 * <p>
	 * Unlike {@link #assertNonNull(Object)}, which is meant for cases in which the given value is
	 * asserted to not be <code>null</code>, the object passed to this method may indeed be
	 * <code>null</code>.
	 * 
	 * @param <T>
	 *            the target type
	 * @param object
	 *            the object to cast
	 * @return the casted object
	 */
	public static <T> @NonNull T nullableAsNonNull(@Nullable T object) {
		return cast(object);
	}

	private Unsafe() {
	}
}