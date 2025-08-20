package valorless.valorlessutils.encoding;

/**
 * Utility class providing simple Caesar Cipher encoding and decoding methods.
 * <p>
 * The Caesar Cipher is a substitution cipher where each letter in the input
 * is shifted by a specified number of positions in the alphabet.
 * Non-letter characters remain unchanged.
 * <p>
 * This class supports both encoding and decoding with a customizable shift value.
 * 
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * String original = "Hello, World!";
 * String encoded  = Encoder.encode(original, 3); // "Khoor, Zruog!"
 * String decoded  = Encoder.decode(encoded, 3);  // "Hello, World!"
 * }</pre>
 * 
 * The class is stateless and thread-safe.
 */
public class Encoder {
	
	// If you need to encode something.. for.. some reason..

	/**
	 * Encodes a message using the Caesar Cipher algorithm with a specified shift.
	 *
	 * @param message The original message to encode.
	 * @param shift   The number of positions to shift each letter in the alphabet.
	 * @return The encoded message.
	 */
	public static String encode(String message, int shift) {
	    StringBuilder encodedMessage = new StringBuilder();

	    for (int i = 0; i < message.length(); i++) {
	        char originalChar = message.charAt(i);

	        // Check if the character is a letter
	        if (Character.isLetter(originalChar)) {
	            // Determine whether it's an uppercase or lowercase letter
	            char base = Character.isUpperCase(originalChar) ? 'A' : 'a';

	            // Apply the shift and wrap around the alphabet
	            char encodedChar = (char) ((originalChar - base + shift) % 26 + base);

	            // Append the encoded character to the result
	            encodedMessage.append(encodedChar);
	        } else {
	            // If the character is not a letter, keep it unchanged
	            encodedMessage.append(originalChar);
	        }
	    }

	    return encodedMessage.toString();
	}

	/**
	 * Decodes an encoded message using the Caesar Cipher algorithm with a specified shift.
	 *
	 * @param encodedMessage The encoded message to decode.
	 * @param shift          The number of positions to shift each letter in the alphabet (use the same shift value used for encoding).
	 * @return The decoded message.
	 */
	public static String decode(String encodedMessage, int shift) {
	    // Decoding is the same as encoding with a negative shift
	    return encode(encodedMessage, -shift);
	}
}
