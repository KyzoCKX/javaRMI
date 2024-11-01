package com.dcom.utils;

public class Validator {
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)?@dhel\\.com$";
        return email != null && email.matches(emailRegex);
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 4 && password.length() <= 20;
    }

    public static boolean isValidName(String name) {
        return name != null && name.length() >= 3 && name.length() <= 30;
    }

    public static boolean isPositiveDouble(double number) {
        return number > 0;
    }

    public static boolean isPositiveDouble(String numberStr) {
        try {
            double number = Double.parseDouble(numberStr);
            return isPositiveDouble(number);
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
