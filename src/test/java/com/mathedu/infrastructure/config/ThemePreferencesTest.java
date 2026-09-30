package com.mathedu.infrastructure.config;

import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.prefs.Preferences;

import static org.assertj.core.api.Assertions.assertThat;

class ThemePreferencesTest {

    @Test
    void storesAndRestoresDarkTheme() throws Exception {
        String nodeName = "mathlab-test-" + UUID.randomUUID();
        Preferences node = Preferences.userRoot().node(nodeName);
        try {
            ThemePreferences settings = new ThemePreferences(node);
            assertThat(settings.isDarkTheme()).isFalse();

            settings.setDarkTheme(true);
            node.flush();

            assertThat(new ThemePreferences(node).isDarkTheme()).isTrue();
        } finally {
            node.removeNode();
            Preferences.userRoot().flush();
        }
    }
}