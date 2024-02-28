package valorless.valorlessutils.text;

public class TextFeatures {
	
	/**
	 * Creates a progress bar with specified parameters.
	 * @param progress The current progress.
	 * @param total The total progress value.
	 * @param barLength The length of the progress bar.
	 * @return The created progress bar as a string.
	 */
	public static String CreateBar(double progress, double total, int barLength) {
	    double filledRatio = (double) progress / total;
	    int filledLength = (int) (barLength * filledRatio);
	    int remainingLength = barLength - filledLength;
	    StringBuilder bar = new StringBuilder("[");
	    for (int i = 0; i < filledLength; i++) {
	        bar.append("⬛");
	    }
	    for (int i = 0; i < remainingLength; i++) {
	        bar.append("⬜");
	    }
	    bar.append("]");
	    return bar.toString();
	}

	/**
	 * Creates a progress bar with specified parameters and customizable styles and colors.
	 * @param progress The current progress.
	 * @param total The total progress value.
	 * @param barLength The length of the progress bar.
	 * @param barColor The color of the progress bar. i.e. '§e'
	 * @param fillColor The color of the filled part of the progress bar. i.e. '§e'
	 * @param barStyle The style character for the progress bar. i.e. '#'
	 * @param fillStyle The style character for the filled part of the progress bar. i.e. '='
	 * @return The created progress bar as a string with customizable styles and colors.
	 */
	public static String CreateBar(double progress, double total, int barLength, String barColor, String fillColor, char barStyle, char fillStyle) {
	    double filledRatio = (double) progress / total;
	    int filledLength = (int) (barLength * filledRatio);
	    int remainingLength = barLength - filledLength;
	    StringBuilder bar = new StringBuilder(barColor + "[");
	    for (int i = 0; i < filledLength; i++) {
	        bar.append(fillColor);
	        bar.append(fillStyle);
	    }
	    for (int i = 0; i < remainingLength; i++) {
	        bar.append(barColor);
	        bar.append(barStyle);
	    }
	    bar.append(barColor + "]&r");
	    return bar.toString();
	}

	/**
	 * Limits the characters in a text to a specified length.
	 * @param text The text to limit.
	 * @param length The maximum length of the text.
	 * @return The limited text.
	 */
	public static String LimitCharacters(String text, int length) {
	    String t = "";
	    try {
	        if(length > text.length()) length = text.length();
	        for(int i = 0; i < length; i++) {
	            t = t + text.charAt(i);
	        }
	    } catch (Exception e) { return text; }
	    return t;
	}

	/**
	 * Limits the decimal places in a number to a specified length.
	 * @param text The number to limit.
	 * @param length The maximum length of the decimal places.
	 * @return The number with limited decimal places.
	 */
	public static String LimitDecimal(String text, int length) {
	    String t = "";
	    try {
	        String[] split = text.split("\\.");
	        t = split[0] + ".";
	        if(length > text.length()) length = split[1].length();
	        for(int i = 0; i < length; i++) {
	            t = t + split[1].charAt(i);
	        }
	    } catch (Exception e) { return text; }
	    return t;
	}
	
}
