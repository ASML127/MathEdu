package com.mathedu.infrastructure.config;

import java.util.Objects;
import java.util.prefs.Preferences;

/** Stores user-facing appearance preferences outside the project directory. */
public final class ThemePreferences {

    private static final String DARK_THEME_KEY = "darkTheme";
    private final Preferences preferences;

    public ThemePreferences(Preferences preferences) {
        this.preferences = Objects.requireNonNull(preferences, "preferences");
    }

    public static ThemePreferences userDefaults() {
        return new ThemePreferences(Preferences.userNodeForPackage(ThemePreferences.class));
    }

    public boolean isDarkTheme() {
        return preferences.getBoolean(DARK_THEME_KEY, false);
    }

    public void setDarkTheme(boolean darkTheme) {
        preferences.putBoolean(DARK_THEME_KEY, darkTheme);
    }
}