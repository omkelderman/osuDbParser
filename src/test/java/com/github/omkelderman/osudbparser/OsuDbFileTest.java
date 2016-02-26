package com.github.omkelderman.osudbparser;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OsuDbFileTest {

    @Test
    public void testParse() throws IOException {
        OsuDbFile file = OsuDbFile.parse(ClassLoader.getSystemResourceAsStream("osu!.db"));

        assertEquals(20160226, file.getOsuVersion());
        assertEquals(9, file.getFolderCount());
        assertTrue(file.isAccountUnlocked());
        assertEquals("oliebol", file.getPlayerName());
        assertEquals(33, file.getBeatmaps().length);
    }

    @Test(expected = IOException.class)
    public void testParseOutdatedOsuVersion() throws IOException {
        OsuDbFile.parse(ClassLoader.getSystemResourceAsStream("osu!-outdated.db"));
    }
}