package com.github.omkelderman.osudbparser;

import com.github.omkelderman.osudbparser.io.OsuDbInputStream;
import lombok.Getter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

@Getter
public class TimingPoint {
    /**
     * milliseconds per beat
     */
    private double msPerBeat;

    /**
     * Offset in milliseconds
     */
    private double offset;

    /**
     * is it an inherited Timing Point?
     */
    private boolean inherited;

    // calculated non-provided fields:

    /**
     * Beats per minute
     */
    private double bpm;

    private TimingPoint() {
    }

    public static TimingPoint[] parseArray(OsuDbInputStream iStream) throws IOException {
        long timingPointCount = iStream.readUInt32();
        if (timingPointCount > Integer.MAX_VALUE) {
            throw new IOException("timingPointCount to much to store the data...");
        }
        TimingPoint[] timingPoints = new TimingPoint[(int) timingPointCount];
        for (int i = 0; i < timingPointCount; ++i) {
            timingPoints[i] = parse(iStream);
        }
        return timingPoints;
    }

    public static TimingPoint parse(OsuDbInputStream iStream) throws IOException {
        TimingPoint timingPoint = new TimingPoint();
        timingPoint.msPerBeat = iStream.readDouble();
        timingPoint.offset = iStream.readDouble();
        // i think it's a bit silly to store a "is not" value
        timingPoint.inherited = !iStream.readBoolean();

        // calculate non-provided fields.
        timingPoint.bpm = 60000 / timingPoint.msPerBeat;
        return timingPoint;
    }

    public static double calcBpmMax(TimingPoint[] timingPoints) {
        Optional<TimingPoint> max = getNotInheritedTimingPoints(timingPoints).max(TimingPoint::compareByBpm);
        if (max.isPresent()) {
            return max.get().bpm;
        } else {
            return 0D;
        }
    }

    public static double calcBpmMin(TimingPoint[] timingPoints) {
        Optional<TimingPoint> min = getNotInheritedTimingPoints(timingPoints).min(TimingPoint::compareByBpm);
        if (min.isPresent()) {
            return min.get().bpm;
        } else {
            return 0D;
        }
    }

    public static double calcMainBpm(TimingPoint[] timingPoints, long beatmapTotalTime) {
        TimingPoint[] notInheritedTimingPoints = getNotInheritedTimingPoints(timingPoints).toArray(TimingPoint[]::new);
        if (notInheritedTimingPoints.length == 0) {
            return 0D;
        } else if (notInheritedTimingPoints.length == 1) {
            return notInheritedTimingPoints[0].bpm;
        }

        // bpm => duration
        Map<Double, Double> bpmDurations = new HashMap<>();

        double prevBpm = notInheritedTimingPoints[0].bpm;
        double prevOffset = notInheritedTimingPoints[0].offset;
        for (int i = 1; i < notInheritedTimingPoints.length; ++i) {
            double currentOffset = notInheritedTimingPoints[i].offset;

            // each time calculate the duration from the previous TimingPoint to the current one and add that duration
            // to the map with the bpm of the previous TimingPoint as key, since its the duration that bpm has been on.
            double prevTimingPointDuration = currentOffset - prevOffset;
            addToBpmDurationsMap(bpmDurations, prevBpm, prevTimingPointDuration);

            prevBpm = notInheritedTimingPoints[i].bpm;
            prevOffset = currentOffset;
        }
        // there is no next TimingPoint, but there is the end of the map, use that
        double prevTimingPointDuration = beatmapTotalTime - prevOffset;
        addToBpmDurationsMap(bpmDurations, prevBpm, prevTimingPointDuration);

        // get bpm with the longest duration, aka do a max with value compare
        return Collections.max(bpmDurations.entrySet(), (a, b) -> Double.compare(a.getValue(), b.getValue())).getKey();
    }

    private static void addToBpmDurationsMap(Map<Double, Double> bpmDurations, double bpm, double duration) {
        Double bpmDuration = bpmDurations.get(bpm);
        if (bpmDuration == null) {
            bpmDurations.put(bpm, duration);
        } else {
            bpmDurations.put(bpm, bpmDuration + duration);
        }
    }

    private static Stream<TimingPoint> getNotInheritedTimingPoints(TimingPoint[] timingPoints) {
        return Arrays.stream(timingPoints).filter(timingPoint -> !timingPoint.inherited);
    }

    private static int compareByBpm(TimingPoint a, TimingPoint b) {
        return Double.compare(a.bpm, b.bpm);
    }
}
