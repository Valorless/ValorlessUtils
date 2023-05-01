package valorless.valorlessutils.json;

import java.util.Map;
import java.util.function.Supplier;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import valorless.valorlessutils.json.CommonMessageArguments.CombinedMessageArguments;
import valorless.valorlessutils.json.CommonMessageArguments.MapMessageArguments;
import valorless.valorlessutils.json.CommonMessageArguments.PrefixedMessageArguments;

/**
 * Provides message arguments by key.
 */
public interface MessageArguments {

	/**
	 * Creates a {@link MessageArguments} that is backed by the given {@link Map}.
	 * <p>
	 * The returned {@link MessageArguments} is a view on the underlying Map: It dynamically
	 * reflects changes to the underlying Map.
	 * 
	 * @param arguments
	 *            the mapping from keys to message arguments, not <code>null</code>
	 * @return the {@link MessageArguments}
	 */
	public static MessageArguments ofMap(Map<? extends @NonNull String, @NonNull ?> arguments) {
		return new MapMessageArguments(arguments);
	}

	// ----

	/**
	 * Gets the message argument for the specified key.
	 * <p>
	 * The returned message argument can be of any type. The caller of this method is responsible
	 * for formatting it appropriately for use as message argument. The returned object may also be
	 * a {@link Supplier}: In this case, the object returned by the Supplier is meant to be used as
	 * the actual message argument.
	 * 
	 * @param key
	 *            the key
	 * @return the message argument, or <code>null</code> if there is none for the given key
	 */
	public @Nullable Object get(String key);

	/**
	 * Creates a {@link MessageArguments} that combines this and the given other
	 * {@link MessageArguments}.
	 * <p>
	 * If no argument is found for a key inside this {@link MessageArguments}, the given other
	 * {@link MessageArguments} is queried for an argument.
	 * 
	 * @param other
	 *            the other {@link MessageArguments}
	 * @return the combined {@link MessageArguments}
	 */
	default MessageArguments combinedWith(MessageArguments other) {
		return new CombinedMessageArguments(this, other);
	}

	/**
	 * Creates a {@link MessageArguments} that prefixes the keys of this {@link MessageArguments}.
	 * <p>
	 * The returned {@link MessageArguments} is a view: The prefix is dynamically stripped from the
	 * passed keys in order to construct the key with which the lookup is performed in this original
	 * {@link MessageArguments}.
	 * 
	 * @param keyPrefix
	 *            the key prefix, not <code>null</code>
	 * @return the prefixed {@link MessageArguments}
	 */
	default MessageArguments prefixed(String keyPrefix) {
		Validate.notNull(keyPrefix, "keyPrefix is null");
		if (keyPrefix.isEmpty()) return this; // No prefix
		return new PrefixedMessageArguments(this, keyPrefix);
	}
}