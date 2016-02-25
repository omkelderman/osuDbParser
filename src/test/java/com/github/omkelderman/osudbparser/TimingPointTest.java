package com.github.omkelderman.osudbparser;

import com.github.omkelderman.osudbparser.io.OsuDbInputStream;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimingPointTest {

    private void assertIsTheSingleTutorialTimingPoint(TimingPoint timingPoint) {
        assertFalse("should be not inherited timing point", timingPoint.isInherited());
        assertEquals(timingPoint.getOffset(), 243, 0.1);
        assertEquals(timingPoint.getBpm(), 160.375, 0.001);
        assertEquals(timingPoint.getMsPerBeat(), 60000 / 160.375, 0.001);
    }

    @Test
    public void testParse() throws Exception {
        TimingPoint timingPoint = TimingPoint.parse(new OsuDbInputStream(ClassLoader.getSystemResourceAsStream("timingPoint.db")));
        assertNotNull(timingPoint);

        assertIsTheSingleTutorialTimingPoint(timingPoint);
    }

    @Test
    public void testParseArraySingle() throws Exception {
        TimingPoint[] timingPoints = TimingPoint.parseArray(new OsuDbInputStream(ClassLoader.getSystemResourceAsStream("timingPointsSingle.db")));
        assertNotNull(timingPoints);

        assertEquals(timingPoints.length, 1);
        assertIsTheSingleTutorialTimingPoint(timingPoints[0]);

        // all the calc-methods should provide the same bpm, since there is only one timing-point
        assertEquals(TimingPoint.calcBpmMin(timingPoints), 160.375, 0.001);
        assertEquals(TimingPoint.calcBpmMax(timingPoints), 160.375, 0.001);
        // second arg (beatmapTotalTime) should be ignored when providing array of only 1
        assertEquals(TimingPoint.calcMainBpm(timingPoints, 0), 160.375, 0.001);
    }

    @Test
    public void testParseArrayMultiple() throws Exception {
        TimingPoint[] timingPoints = TimingPoint.parseArray(new OsuDbInputStream(ClassLoader.getSystemResourceAsStream("timingPointsMultiple.db")));
        assertNotNull(timingPoints);

        assertEquals(timingPoints.length, 43);

        // not gonna assert each individual timing-point, should be covered by other test methods

        // but there values are interesting: (this is https://osu.ppy.sh/b/557815 btw)
        assertEquals(TimingPoint.calcBpmMin(timingPoints), 91.000, 0.001);
        assertEquals(TimingPoint.calcBpmMax(timingPoints), 182.000, 0.001);
        assertEquals(TimingPoint.calcMainBpm(timingPoints, 209306), 182.000, 0.001);
    }

    @Test
    public void testParseArrayEmpty() throws Exception {
        TimingPoint[] timingPoints = new TimingPoint[0];

        assertEquals(TimingPoint.calcBpmMin(timingPoints), 0, 0.001);
        assertEquals(TimingPoint.calcBpmMax(timingPoints), 0, 0.001);
        // second arg (beatmapTotalTime) should be ignored when providing array of 0 length
        assertEquals(TimingPoint.calcMainBpm(timingPoints, 0), 0, 0.001);
    }
}