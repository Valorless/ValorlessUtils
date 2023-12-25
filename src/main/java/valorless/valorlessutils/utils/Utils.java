package valorless.valorlessutils.utils;

import java.util.Random;

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
}
