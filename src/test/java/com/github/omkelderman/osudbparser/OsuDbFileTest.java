package com.github.omkelderman.osudbparser;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OsuDbFileTest {
    private static OsuDbFile file;

    @BeforeClass
    public static void loadOsuDb() throws IOException {
        file = OsuDbFile.parse(ClassLoader.getSystemResourceAsStream("osu!.db"));
    }

    @Test
    public void testOsuVersion() {
        long expectedVersion = 20160217;
        long version = file.getOsuVersion();

        assertEquals(expectedVersion, version);
    }


    @Test
    public void testFolderCount() {
        long expectedFolderCount = 10532;
        long folderCount = file.getFolderCount();

        assertEquals(expectedFolderCount, folderCount);
    }

    @Test
    public void testIsAccountUnlocked() {
        boolean expectedAccountUnlocked = file.isAccountUnlocked();

        assertTrue(expectedAccountUnlocked);
    }

    @Test
    public void testGetPlayerName() {
        String playerName = "oliebol";
        String expectedPlayerName = file.getPlayerName();

        assertEquals(expectedPlayerName, playerName);
    }
}