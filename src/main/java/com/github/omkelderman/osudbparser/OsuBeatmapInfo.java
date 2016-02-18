package com.github.omkelderman.osudbparser;

import lombok.Getter;

import java.io.IOException;

@Getter
public class OsuBeatmapInfo {
    private String artistName;
    private String artistNameUnicode;
    private String songTitle;
    private String songTitleUnicode;
    private String creatorName;
    private String difficulty;
    private String audioFileName;
    private String md5BeatmapHash;
    private String osuFileName;
    private int ranked;
    private int hitcircleCount;
    private int sliderCount;
    private int spinnerCount;
    private long lastModificationTime;
    private float approachRate;
    private float circleSize;
    private float hpDrain;
    private float OverallDifficulty;
    private double sliderVelocity;
    private long drainTime;
    private long totalTime;
    private long audioPreviewStartTime;
    private TimingPoint[] timingPoints;
    private long beatmapId;
    private long beatmapSetId;
    private long threadId;
    private int localOffset;
    private float stackLeniency;
    private int gameMode;
    private String source;
    private String tags;
    private int onlineOffset;
    private String font;
    private boolean unplayed;
    private long lastTimePlayed;
    private boolean osz2;
    private String folderName;
    private long lastCheckedTime;
    private boolean ignoreBeatmapSounds;
    private boolean ignoreBeatmapSkin;
    private boolean disableStoryboard;
    private boolean disableVideo;
    private boolean visualOverride;
    private int maniaScrollSpeed;

    private OsuBeatmapInfo() {
    }

    public static void parse(OsuBeatmapInfo[] beatmaps, OsuDbInputStream iStream) throws IOException {
        for (int i = 0; i < beatmaps.length; ++i) {
            beatmaps[i] = parse(iStream);
        }
    }

    public static OsuBeatmapInfo parse(OsuDbInputStream iStream) throws IOException {
        OsuBeatmapInfo beatmapInfo = new OsuBeatmapInfo();
        beatmapInfo.artistName = iStream.readString();
        beatmapInfo.artistNameUnicode = iStream.readString();
        beatmapInfo.songTitle = iStream.readString();
        beatmapInfo.songTitleUnicode = iStream.readString();
        beatmapInfo.creatorName = iStream.readString();
        beatmapInfo.difficulty = iStream.readString();
        beatmapInfo.audioFileName = iStream.readString();
        beatmapInfo.md5BeatmapHash = iStream.readString();
        beatmapInfo.osuFileName = iStream.readString();
        beatmapInfo.ranked = iStream.readUInt8();
        beatmapInfo.hitcircleCount = iStream.readUInt16();
        beatmapInfo.sliderCount = iStream.readUInt16();
        beatmapInfo.spinnerCount = iStream.readUInt16();
        beatmapInfo.lastModificationTime = iStream.readUInt64();
        beatmapInfo.approachRate = iStream.readFloat();
        beatmapInfo.circleSize = iStream.readFloat();
        beatmapInfo.hpDrain = iStream.readFloat();
        beatmapInfo.OverallDifficulty = iStream.readFloat();
        beatmapInfo.sliderVelocity = iStream.readDouble();
        skipUnknownSection(iStream);
        beatmapInfo.drainTime = iStream.readUInt32();
        beatmapInfo.totalTime = iStream.readUInt32();
        beatmapInfo.audioPreviewStartTime = iStream.readUInt32();
        long timingPointCount = iStream.readUInt32();
        if (timingPointCount > Integer.MAX_VALUE) {
            throw new IOException("timingPointCount to much to store the data...");
        }
        beatmapInfo.timingPoints = new TimingPoint[(int) timingPointCount];
        TimingPoint.parse(beatmapInfo.timingPoints, iStream);
        beatmapInfo.beatmapId = iStream.readUInt32();
        beatmapInfo.beatmapSetId = iStream.readUInt32();
        beatmapInfo.threadId = iStream.readUInt32();
        // skip 4 unknown bytes
        iStream.skipFully(4);
        beatmapInfo.localOffset = iStream.readUInt16();
        beatmapInfo.stackLeniency = iStream.readFloat();
        beatmapInfo.gameMode = iStream.readUInt8();
        beatmapInfo.source = iStream.readString();
        beatmapInfo.tags = iStream.readString();
        beatmapInfo.onlineOffset = iStream.readUInt16();
        beatmapInfo.font = iStream.readString();
        beatmapInfo.unplayed = iStream.readBoolean();
        beatmapInfo.lastTimePlayed = iStream.readUInt64();
        beatmapInfo.osz2 = iStream.readBoolean();
        beatmapInfo.folderName = iStream.readString();
        beatmapInfo.lastCheckedTime = iStream.readUInt64(); // when beatmap was checked against osu! repository
        beatmapInfo.ignoreBeatmapSounds = iStream.readBoolean();
        beatmapInfo.ignoreBeatmapSkin = iStream.readBoolean();
        beatmapInfo.disableStoryboard = iStream.readBoolean();
        beatmapInfo.disableVideo = iStream.readBoolean();
        beatmapInfo.visualOverride = iStream.readBoolean();
        // skip "Last modification time (?)"
        iStream.skipFully(4);
        beatmapInfo.maniaScrollSpeed = iStream.readUInt8();
        return beatmapInfo;
    }

    private static void skipUnknownSection(OsuDbInputStream iStream) throws IOException {
//            long amount = iStream.readUInt32();
//            for (long i = 0; i < amount; ++i) {
//                // the Int-Double pairs from https://osu.ppy.sh/wiki/Db_(file_format)
//                iStream.skipFully(14);
//            }
//            // no idea, found 12 null-bytes... i hope this length doesn't depend on something...
//            iStream.skipFully(12);
        // this shit is weird.... there are usally 9 Int-Double pairs, but the 12 padding bytes are no consisten
        // sometimes there are a few before the pairs, but it looks like the 9 is consistent, so this block is
        // hopefully fixed length.... 4 + 9*14 + 12 =
        iStream.skipFully(142);
    }

}
