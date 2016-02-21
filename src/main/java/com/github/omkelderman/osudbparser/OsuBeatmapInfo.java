package com.github.omkelderman.osudbparser;

import lombok.Getter;

import java.io.IOException;

@Getter
public class OsuBeatmapInfo {
    /**
     * Artist name
     */
    private String artistName;

    /**
     * Artist name, in Unicode
     */
    private String artistNameUnicode;

    /**
     * Song title
     */
    private String songTitle;

    /**
     * Song title, in Unicode
     */
    private String songTitleUnicode;

    /**
     * Creator name
     */
    private String creatorName;

    /**
     * Difficulty (e.g. Hard, Insane, etc.)
     */
    private String difficulty;

    /**
     * Audio file name
     */
    private String audioFileName;

    /**
     * MD5 hash of the beatmap
     */
    private String md5BeatmapHash;

    /**
     * Name of the .osu file corresponding to this beatmap
     */
    private String osuFileName;

    /**
     * Ranked status (4 = ranked, 5 = approved, 2 = pending/graveyard)
     *
     * <i>Someone verify this, I don't trust the wiki page I copied this info from....</i>
     */
    private int ranked;

    /**
     * Number of hitcircles
     */
    private int hitcircleCount;

    /**
     * Number of sliders (note: this will be present in every mode)
     */
    private int sliderCount;

    /**
     * Number of spinners (note: this will be present in every mode)
     */
    private int spinnerCount;

    /**
     * Last modification time, Windows ticks.
     *
     * <i>I suppose it's this: https://msdn.microsoft.com/library/system.datetime.ticks(v=vs.100).aspx</i>
     *
     * <b>NOTE: if this is a negative value, it's an integer overflow....</b>
     */
    private long lastModificationTime;

    /**
     * Approach rate
     */
    private float approachRate;

    /**
     * Circle size
     */
    private float circleSize;

    /**
     * HP drain
     */
    private float hpDrain;

    /**
     * Overall difficulty
     */
    private float OverallDifficulty;

    /**
     * Slider velocity
     */
    private double sliderVelocity;

    // skipped the "Int-Double pairs", aparently they now have a meaning, need to look into that

    /**
     * Drain time, in seconds
     */
    private long drainTime;

    /**
     * Total time, in milliseconds
     */
    private long totalTime;

    /**
     * Time when the audio preview when hovering over a beatmap in beatmap select starts, in milliseconds.
     */
    private long audioPreviewStartTime;

    /**
     * Array of timing points
     */
    private TimingPoint[] timingPoints;

    /**
     * Beatmap ID
     */
    private long beatmapId;

    /**
     * Beatmap set ID
     */
    private long beatmapSetId;

    /**
     * Thread ID
     *
     * <i>I have no freakin' clue what this actually is....</i>
     */
    private long threadId;

    /**
     * Local beatmap offset
     */
    private int localOffset;

    /**
     * Stack leniency
     */
    private float stackLeniency;

    /**
     * Osu gameplay mode. 0x00 = osu!Standard, 0x01 = Taiko, 0x02 = CTB, 0x03 = Mania
     */
    private int gameMode;

    /**
     * Song source
     */
    private String source;

    /**
     * Song tags
     */
    private String tags;

    /**
     * Online offset
     */
    private int onlineOffset;

    /**
     * Font used for the title of the song
     */
    private String font;

    /**
     * Is beatmap unplayed
     */
    private boolean unplayed;

    /**
     * Last time when beatmap was played
     *
     * <i>I suppose it's this again: https://msdn.microsoft.com/library/system.datetime.ticks(v=vs.100).aspx</i>
     *
     * <b>NOTE: if this is a negative value, it's an integer overflow....</b>
     */
    private long lastTimePlayed;

    /**
     * Is the beatmap osz2
     */
    private boolean osz2;

    /**
     * Folder name of the beatmap, relative to Songs folder
     */
    private String folderName;

    /**
     * Last time when beatmap was checked against osu! repository
     *
     * <i>I suppose it's this again: https://msdn.microsoft.com/library/system.datetime.ticks(v=vs.100).aspx</i>
     *
     * <b>NOTE: if this is a negative value, it's an integer overflow....</b>
     */
    private long lastCheckedTime;

    /**
     * Ignore beatmap sounds
     */
    private boolean ignoreBeatmapSounds;

    /**
     * Ignore beatmap skin
     */
    private boolean ignoreBeatmapSkin;

    /**
     * Disable storyboard
     */
    private boolean disableStoryboard;

    /**
     * Disable video
     */
    private boolean disableVideo;

    /**
     * Visual override
     */
    private boolean visualOverride;

    // skipped "Int | Last modification time (?)"

    /**
     * Mania scroll speed
     */
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
        beatmapInfo.lastCheckedTime = iStream.readUInt64();
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
        // this is mostly complete guessing...... HALP, LETS PRAY THIS IS GUD
        // 4 times 4byte blocks, once of them contains the number of "Int-Double pairs", but its unclear when....
        // so keep reading till amount is not null, read the pairs, read the remainer of the 4byte blocks...
        // yes, I know this doesn't make any sense at all, but this is what my experimenting has found out.
        // it works on my 90gb of beatmaps
        int count = 4;
        long amount = 0;
        while (amount == 0 && count > 0) {
            amount = iStream.readUInt32();
            --count;
        }
        for (long i = 0; i < amount; ++i) {
            // the Int-Double pairs from https://osu.ppy.sh/wiki/Db_(file_format)
            iStream.skipFully(14);
        }
        iStream.skipFully(4 * count);
    }
}
