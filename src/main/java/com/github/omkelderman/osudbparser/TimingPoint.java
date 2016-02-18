package com.github.omkelderman.osudbparser;

import lombok.Getter;

import java.io.IOException;

@Getter
public class TimingPoint {
    private double bpm;
    private double offset;
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
        timingPoint.bpm = iStream.readDouble();
        timingPoint.offset = iStream.readDouble();
        timingPoint.inherited = iStream.readBoolean();
        return timingPoint;
    }
}
