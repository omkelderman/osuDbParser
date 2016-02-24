package com.github.omkelderman.osudbparser;

import com.github.omkelderman.osudbparser.io.OsuDbInputStream;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StarRatingTest {

    @Test
    public void testParseEmpty() throws Exception {
        StarRating starRating = StarRating.parse(new OsuDbInputStream(ClassLoader.getSystemResourceAsStream("starRatingEmpty.db")));

        assertNull(starRating);
    }

    @Test
    public void testParse1() throws Exception {
        StarRating starRating = StarRating.parse(new OsuDbInputStream(ClassLoader.getSystemResourceAsStream("starRating1.db")));

        assertNotNull(starRating);

        assertEquals(2.50, starRating.forNoMod(), 0.1); // nomod
        assertEquals(2.24, starRating.forModArray(1), 0.1); // EZ
        assertEquals(3.09, starRating.forModArray(1, 6), 0.1); // EZ+DT
        assertEquals(1.79, starRating.forModArray(1, 8), 0.1); // EZ+HT
        assertEquals(2.86, starRating.forModArray(4), 0.1); // HR
        assertEquals(3.98, starRating.forModArray(4, 6), 0.1); // HR+DT
        assertEquals(2.33, starRating.forModArray(4, 8), 0.1); // HR+HT
        assertEquals(3.42, starRating.forModArray(6), 0.1); // DT
        assertEquals(2.02, starRating.forModArray(8), 0.1); // HT
    }

    @Test
    public void testParse2() throws Exception {
        // FUUUUUUU - FUUUUUUU - FUUUUUAAAAAAACK!!!!!
        StarRating starRating = StarRating.parse(new OsuDbInputStream(ClassLoader.getSystemResourceAsStream("starRating2.db")));

        assertNotNull(starRating);

        assertEquals(126.49, starRating.forNoMod(), 0.1); // nomod
        assertEquals(98.65, starRating.forModArray(1), 0.1); // EZ
        assertEquals(105.31, starRating.forModArray(1, 6), 0.1); // EZ+DT
        assertEquals(92.75, starRating.forModArray(1, 8), 0.1); // EZ+HT
        assertEquals(157.78, starRating.forModArray(4), 0.1); // HR
        assertEquals(168.52, starRating.forModArray(4, 6), 0.1); // HR+DT
        assertEquals(148.28, starRating.forModArray(4, 8), 0.1); // HR+HT
        assertEquals(135.07, starRating.forModArray(6), 0.1); // DT
        assertEquals(118.89, starRating.forModArray(8), 0.1); // HT
    }

    @Test
    public void testForModsExtraMods() throws Exception {
        StarRating starRating = StarRating.parse(new OsuDbInputStream(ClassLoader.getSystemResourceAsStream("starRating1.db")));

        assertNotNull(starRating);

        assertEquals(2.86, starRating.forModArray(4, 3), 0.1); // HR + HD
        assertEquals(2.33, starRating.forModArray(4, 8, 3), 0.1); // HR+HT + HD
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForModsIllegalModsEZHR() throws Exception {
        StarRating starRating = StarRating.parse(new OsuDbInputStream(ClassLoader.getSystemResourceAsStream("starRating1.db")));

        assertNotNull(starRating);

        // EZ+HR is illegal
        starRating.forModArray(1, 4); // EZ + HR
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForModsIllegalModsEZHRHD() throws Exception {
        StarRating starRating = StarRating.parse(new OsuDbInputStream(ClassLoader.getSystemResourceAsStream("starRating1.db")));

        assertNotNull(starRating);

        // EZ+HR is illegal
        starRating.forModArray(1, 4, 3); // EZ + HR + HD
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForModsIllegalModsHTDT() throws Exception {
        StarRating starRating = StarRating.parse(new OsuDbInputStream(ClassLoader.getSystemResourceAsStream("starRating1.db")));

        assertNotNull(starRating);

        // HT+DT is illegal
        starRating.forModArray(8, 6); // HT + DT
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForModsIllegalModsHTDTHD() throws Exception {
        StarRating starRating = StarRating.parse(new OsuDbInputStream(ClassLoader.getSystemResourceAsStream("starRating1.db")));

        assertNotNull(starRating);

        // HT+DT is illegal
        starRating.forModArray(8, 6, 3); // HT + DT + HD
    }

}