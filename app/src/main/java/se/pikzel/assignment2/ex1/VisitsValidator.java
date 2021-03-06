package se.pikzel.assignment2.ex1;

import android.content.Context;

import se.pikzel.assignment2.UIMessage;

/**
 * @author Pontus Palmenäs
 */
public class VisitsValidator {
    public static boolean isInputValid(Context context, int year, String country) {
        if (year > 2025 || year < 1915) {
            new UIMessage(context).showErrorMessage("Come on, time traveller...");
            return false;
        }
        if (country.length() == 0) {
            new UIMessage(context).showErrorMessage("Enter country.");
            return false;
        }
        return true;
    }


}
