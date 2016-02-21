package com.github.omkelderman.osudbparser;

import lombok.Getter;

import java.io.IOException;
import java.util.Arrays;

@Getter
public class TimingPoint {
    /**
     * milliseconds per beat
     */
    private double msPerBeat;

    /**
     * Beats per minute
     */
    private double bpm;

    /**
     * Offset in milliseconds
     */
    private double offset;

    /**
     * is it an inherited Timing Point?
     */
    private boolean inherited;

    private TimingPoint() {
    }

    public static void parse(TimingPoint[] timingPoints, OsuDbInputStream iStream) throws IOException {
        for (int i = 0; i < timingPoints.length; ++i) {
            timingPoints[i] = parse(iStream);
        }
    }

    private static TimingPoint parse(OsuDbInputStream iStream) throws IOException {
        TimingPoint timingPoint = new TimingPoint();
        timingPoint.msPerBeat = iStream.readDouble();
        timingPoint.bpm = 60000 / timingPoint.msPerBeat;
        timingPoint.offset = iStream.readDouble();
        timingPoint.inherited = !iStream.readBoolean();
        return timingPoint;
    }

    public static double calcBpmMax(TimingPoint[] timingPoints) {
        return Arrays.stream(timingPoints).filter(TimingPoint::isNotInherited).max(TimingPoint::compareByBpm).get().bpm;
    }
    public static double calcBpmMin(TimingPoint[] timingPoints) {
        return Arrays.stream(timingPoints).filter(TimingPoint::isNotInherited).min(TimingPoint::compareByBpm).get().bpm;
    }

    private boolean isNotInherited() {
        return !inherited;
    }

    private static int compareByBpm(TimingPoint a, TimingPoint b) {
        return Double.compare(a.bpm, b.bpm);
    }
}
