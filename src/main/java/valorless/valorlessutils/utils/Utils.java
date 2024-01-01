package valorless.valorlessutils.utils;

import java.util.Random;

import valorless.valorlessutils.types.Vector3;

/**
 * Utility class consisting of various functions.
 */
public class Utils {

    /**
     * Checks if a given string is null or empty, considering various conditions including whitespace and length.
     * @param string The input string to be checked.
     * @return true if the string is null or empty, false otherwise.
     */
    public static boolean IsStringNullOrEmpty(String string) {
        if(string == null) return true;
        else if(string.isEmpty()) return true;
        else if(string.isBlank()) return true;
        else if(string.trim().isEmpty()) return true;
        else if(string.length() == 0) return true;
        else return false;
    }

    /**
     * Calculates the percentage of a given value relative to a maximum value.
     * @param current The current value.
     * @param max The maximum value.
     * @return The percentage value.
     */
    public static double Percent(double current, double max) {
        return (current / max) * 100;
    }

    /**
     * Calculates the percentage of a given float value relative to a maximum float value.
     * @param current The current float value.
     * @param max The maximum float value.
     * @return The percentage value.
     */
    public static float Percent(float current, float max) {
        return (current / max) * 100;
    }

    /**
     * Determines if an event occurs based on a given percentage chance.
     * @param percent The percentage chance of the event occurring.
     * @return true if the event occurs, false otherwise. The decision is made randomly.
     */
    public static Boolean Chance(double percent) {
        Random rand = new Random();
        double i = rand.nextInt((0 - 100) + 1) + 0;
        if (i <= percent) return true;
        else return false;
    }
    
    /**
     * Generates a random integer within the specified range (inclusive).
     *
     * @param min The minimum value of the range.
     * @param max The maximum value of the range.
     * @return A randomly generated integer within the specified range.
     */
    public static Integer RandomRange(Integer min, Integer max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Generates a random double within the specified range (inclusive).
     *
     * @param min The minimum value of the range.
     * @param max The maximum value of the range.
     * @return A randomly generated double within the specified range.
     */
    public static double RandomRange(double min, double max) {
        Random rand = new Random();
        return rand.nextDouble((max - min) + 1) + min;
    }
    
    /**
     * Clamps a value between a minimum and maximum value.
     *
     * @param value The value to clamp.
     * @param min   The minimum allowed value.
     * @param max   The maximum allowed value.
     * @param <T>   The type of the value (must implement Comparable).
     * @return The clamped value between min and max.
     */
    public static <T extends Comparable<T>> T Clamp(T value, T min, T max) {
        try {
            if (value.compareTo(min) < 0) {
                return min;
            } else if (value.compareTo(max) > 0) {
                return max;
            } else {
                return value;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Clamps an Integer value between 0 and 1.
     *
     * @param value The Integer value to clamp.
     * @return The clamped Integer value between 0 and 1.
     */
    public static Integer Clamp01(Integer value) {
        if (value < 0) return 0;
        if (value > 1) return 1;
        return value;
    }

    /**
     * Clamps a long value between 0 and 1.
     *
     * @param value The long value to clamp.
     * @return The clamped long value between 0 and 1.
     */
    public static long Clamp01(long value) {
        if (value < 0) return 0;
        if (value > 1) return 1;
        return value;
    }

    /**
     * Clamps a double value between 0 and 1.
     *
     * @param value The double value to clamp.
     * @return The clamped double value between 0 and 1.
     */
    public static double Clamp01(double value) {
        if (value < 0) return 0;
        if (value > 1) return 1;
        return value;
    }

    /**
     * Clamps a float value between 0 and 1.
     *
     * @param value The float value to clamp.
     * @return The clamped float value between 0 and 1.
     */
    public static float Clamp01(float value) {
        if (value < 0) return 0;
        if (value > 1) return 1;
        return value;
    }
    
    /**
     * Utility class for converting between Boolean and Integer values.
     */
    public static class Bool {

    	/**
    	 * Converts an Integer value to a Boolean.
    	 *
    	 * @param value The Integer value to convert.
    	 * @return The corresponding Boolean value (true for 1, false for 0).
    	 */
        public static Boolean FromValue(Integer value) {
        	value = Clamp01(value);
            if (value == 1) return true;
            if (value == 0) return false;
            return null;
        }

        /**
         * Converts a Boolean value to an Integer.
         *
         * @param bool The Boolean value to convert.
         * @return The corresponding Integer value (1 for true, 0 for false).
         */
        public static Integer ToValue(Boolean bool) {
            if (bool == true) return 1;
            if (bool == false) return 0;
            return null;
        }
    }
}
