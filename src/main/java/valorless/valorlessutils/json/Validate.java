package valorless.valorlessutils.json;

import java.util.function.Predicate;
import java.util.function.Supplier;

import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Validate {

	// ARGUMENTS

	public static void error(String errorMessage) {
		throw illegalArgumentException(errorMessage);
	}

	public static void error(Supplier<String> errorMessageSupplier) {
		throw illegalArgumentException(errorMessageSupplier);
	}

	private static IllegalArgumentException illegalArgumentException(String errorMessage) {
		return new IllegalArgumentException(errorMessage);
	}

	// Note: Throws a NPE if the supplier is null, similar to how Logger throws a NPE.
	private static IllegalArgumentException illegalArgumentException(
			Supplier<String> errorMessageSupplier
	) {
		return new IllegalArgumentException(errorMessageSupplier.get());
	}

	@EnsuresNonNull("#1")
	public static <T> @NonNull T notNull(@UnknownInitialization @Nullable T object) {
		return notNull(object, "object is null");
	}

	@EnsuresNonNull("#1")
	public static <T> @NonNull T notNull(
			@UnknownInitialization @Nullable T object,
			String errorMessage
	) {
		if (object == null) {
			throw illegalArgumentException(errorMessage);
		} else {
			return Unsafe.initialized(object);
		}
	}

	@EnsuresNonNull("#1")
	public static <T> @NonNull T notNull(
			@UnknownInitialization @Nullable T object,
			Supplier<String> errorMessageSupplier
	) {
		if (object == null) {
			throw illegalArgumentException(errorMessageSupplier);
		}
		return Unsafe.initialized(object);
	}

	@EnsuresNonNull("#1")
	public static String notEmpty(@Nullable String string) {
		return notEmpty(string, "string is null or empty");
	}

	@EnsuresNonNull("#1")
	public static String notEmpty(@Nullable String string, String errorMessage) {
		if (string == null || string.isEmpty()) {
			throw illegalArgumentException(errorMessage);
		}
		return string;
	}

	@EnsuresNonNull("#1")
	public static String notEmpty(@Nullable String string, Supplier<String> errorMessageSupplier) {
		if (string == null || string.isEmpty()) {
			throw illegalArgumentException(errorMessageSupplier);
		}
		return string;
	}

	public static boolean isTrue(boolean expression) {
		return isTrue(expression, "expression evaluates to false");
	}

	public static boolean isTrue(boolean expression, String errorMessage) {
		if (!expression) {
			throw illegalArgumentException(errorMessage);
		}
		return expression;
	}

	public static boolean isTrue(boolean expression, Supplier<String> errorMessageSupplier) {
		if (!expression) {
			error(errorMessageSupplier);
		}
		return expression;
	}

	public static <@Nullable T> T isTrue(T value, Predicate<T> predicate) {
		return isTrue(value, predicate, "predicate evaluates to false");
	}

	public static <@Nullable T> T isTrue(T value, Predicate<T> predicate, String errorMessage) {
		if (!predicate.test(value)) {
			error(errorMessage);
		}
		return value;
	}

	public static <@Nullable T> T isTrue(
			T value,
			Predicate<T> predicate,
			Supplier<String> errorMessageSupplier
	) {
		if (!predicate.test(value)) {
			error(errorMessageSupplier);
		}
		return value;
	}

	public static double isFinite(double value) {
		return isFinite(value, "value is not finite");
	}

	public static double isFinite(double value, String errorMessage) {
		if (!Double.isFinite(value)) {
			error(errorMessage);
		}
		return value;
	}

	public static double isFinite(double value, Supplier<String> errorMessageSupplier) {
		if (!Double.isFinite(value)) {
			error(errorMessageSupplier);
		}
		return value;
	}

	public static double notNaN(double value) {
		return notNaN(value, "value is NaN");
	}

	public static double notNaN(double value, String errorMessage) {
		if (Double.isNaN(value)) {
			error(errorMessage);
		}
		return value;
	}

	public static double notNaN(double value, Supplier<String> errorMessageSupplier) {
		if (Double.isNaN(value)) {
			error(errorMessageSupplier);
		}
		return value;
	}

	public static float isFinite(float value) {
		return isFinite(value, "value is not finite");
	}

	public static float isFinite(float value, String errorMessage) {
		if (!Float.isFinite(value)) {
			error(errorMessage);
		}
		return value;
	}

	public static float isFinite(float value, Supplier<String> errorMessageSupplier) {
		if (!Float.isFinite(value)) {
			error(errorMessageSupplier);
		}
		return value;
	}

	public static float notNaN(float value) {
		return notNaN(value, "value is NaN");
	}

	public static float notNaN(float value, String errorMessage) {
		if (Float.isNaN(value)) {
			error(errorMessage);
		}
		return value;
	}

	public static float notNaN(float value, Supplier<String> errorMessageSupplier) {
		if (Float.isNaN(value)) {
			error(errorMessageSupplier);
		}
		return value;
	}

	public static <T extends Iterable<?>> @NonNull T noNullElements(
			@Nullable T iterable,
			String errorMessage
	) {
		@NonNull T nonNullIterable = notNull(iterable, errorMessage);
		for (Object object : nonNullIterable) {
			notNull(object, errorMessage);
		}
		return nonNullIterable;
	}

	public static <T extends Iterable<?>> @NonNull T noNullElements(
			@Nullable T iterable,
			Supplier<String> errorMessageSupplier
	) {
		@NonNull T nonNullIterable = notNull(iterable, errorMessageSupplier);
		for (Object object : nonNullIterable) {
			notNull(object, errorMessageSupplier);
		}
		return nonNullIterable;
	}

	// STATE

	public static final class State {

		public static void error(String errorMessage) {
			throw illegalStateException(errorMessage);
		}

		public static void error(Supplier<String> errorMessageSupplier) {
			throw illegalStateException(errorMessageSupplier);
		}

		private static IllegalStateException illegalStateException(String errorMessage) {
			return new IllegalStateException(errorMessage);
		}

		// Note: Throws a NPE if the supplier is null, similar to how Logger throws a NPE.
		private static IllegalStateException illegalStateException(
				Supplier<String> errorMessageSupplier
		) {
			return new IllegalStateException(errorMessageSupplier.get());
		}

		@EnsuresNonNull("#1")
		public static <T> @NonNull T notNull(@UnknownInitialization @Nullable T object) {
			return notNull(object, "object is null");
		}

		@EnsuresNonNull("#1")
		public static <T> @NonNull T notNull(
				@UnknownInitialization @Nullable T object,
				String errorMessage
		) {
			if (object == null) {
				throw illegalStateException(errorMessage);
			}
			return Unsafe.initialized(object);
		}

		@EnsuresNonNull("#1")
		public static <T> @NonNull T notNull(
				@UnknownInitialization @Nullable T object,
				Supplier<String> errorMessageSupplier
		) {
			if (object == null) {
				throw illegalStateException(errorMessageSupplier);
			}
			return Unsafe.initialized(object);
		}

		@EnsuresNonNull("#1")
		public static String notEmpty(@Nullable String string) {
			return notEmpty(string, "string is null or empty");
		}

		@EnsuresNonNull("#1")
		public static String notEmpty(@Nullable String string, String errorMessage) {
			if (string == null || string.isEmpty()) {
				throw illegalStateException(errorMessage);
			}
			return string;
		}

		@EnsuresNonNull("#1")
		public static String notEmpty(
				@Nullable String string,
				Supplier<String> errorMessageSupplier
		) {
			if (string == null || string.isEmpty()) {
				throw illegalStateException(errorMessageSupplier);
			}
			return string;
		}

		public static boolean isTrue(boolean expression) {
			return isTrue(expression, "expression evaluates to false");
		}

		public static boolean isTrue(boolean expression, String errorMessage) {
			if (!expression) {
				error(errorMessage);
			}
			return expression;
		}

		public static boolean isTrue(boolean expression, Supplier<String> errorMessageSupplier) {
			if (!expression) {
				error(errorMessageSupplier);
			}
			return expression;
		}

		public static <@Nullable T> T isTrue(T value, Predicate<T> predicate) {
			return isTrue(value, predicate, "predicate evaluates to false");
		}

		public static <@Nullable T> T isTrue(T value, Predicate<T> predicate, String errorMessage) {
			if (!predicate.test(value)) {
				error(errorMessage);
			}
			return value;
		}

		public static <@Nullable T> T isTrue(
				T value,
				Predicate<T> predicate,
				Supplier<String> errorMessageSupplier
		) {
			if (!predicate.test(value)) {
				error(errorMessageSupplier);
			}
			return value;
		}

		public static double isFinite(double value) {
			return isFinite(value, "value is not finite");
		}

		public static double isFinite(double value, String errorMessage) {
			if (!Double.isFinite(value)) {
				error(errorMessage);
			}
			return value;
		}

		public static double isFinite(double value, Supplier<String> errorMessageSupplier) {
			if (!Double.isFinite(value)) {
				error(errorMessageSupplier);
			}
			return value;
		}

		public static double notNaN(double value) {
			return notNaN(value, "value is NaN");
		}

		public static double notNaN(double value, String errorMessage) {
			if (Double.isNaN(value)) {
				error(errorMessage);
			}
			return value;
		}

		public static double notNaN(double value, Supplier<String> errorMessageSupplier) {
			if (Double.isNaN(value)) {
				error(errorMessageSupplier);
			}
			return value;
		}

		public static float isFinite(float value) {
			return isFinite(value, "value is not finite");
		}

		public static float isFinite(float value, String errorMessage) {
			if (!Float.isFinite(value)) {
				error(errorMessage);
			}
			return value;
		}

		public static float isFinite(float value, Supplier<String> errorMessageSupplier) {
			if (!Float.isFinite(value)) {
				error(errorMessageSupplier);
			}
			return value;
		}

		public static float notNaN(float value) {
			return notNaN(value, "value is NaN");
		}

		public static float notNaN(float value, String errorMessage) {
			if (Float.isNaN(value)) {
				error(errorMessage);
			}
			return value;
		}

		public static float notNaN(float value, Supplier<String> errorMessageSupplier) {
			if (Float.isNaN(value)) {
				error(errorMessageSupplier);
			}
			return value;
		}

		public static <T extends Iterable<?>> @NonNull T noNullElements(
				@Nullable T iterable,
				String errorMessage
		) {
			@NonNull T nonNullIterable = notNull(iterable, errorMessage);
			for (Object object : nonNullIterable) {
				notNull(object, errorMessage);
			}
			return nonNullIterable;
		}

		public static <T extends Iterable<?>> @NonNull T noNullElements(
				@Nullable T iterable,
				Supplier<String> errorMessageSupplier
		) {
			@NonNull T nonNullIterable = notNull(iterable, errorMessageSupplier);
			for (Object object : nonNullIterable) {
				notNull(object, errorMessageSupplier);
			}
			return nonNullIterable;
		}

		private State() {
		}
	}

	private Validate() {
	}
}