package com.newtyf.cnp_patients_app.data.session;

import com.newtyf.cnp_patients_app.data.model.Nutritionist;

public class SessionManager {

    private static Nutritionist currentNutritionist;

    public static void setCurrentNutritionist(Nutritionist nutritionist) {
        currentNutritionist = nutritionist;
    }

    public static Nutritionist getCurrentNutritionist() {
        return currentNutritionist;
    }

    public static String getCurrentId() {
        return currentNutritionist != null ? currentNutritionist.getId() : null;
    }

    public static void clear() {
        currentNutritionist = null;
    }

    public static boolean isLoggedIn() {
        return currentNutritionist != null;
    }
}
