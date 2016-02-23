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
     * <p>
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
     * <p>
     * <i>I suppose it's this: https://msdn.microsoft.com/library/system.datetime.ticks(v=vs.100).aspx</i>
     * <p>
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
    private float overallDifficulty;

    /**
     * Slider velocity
     */
    private double sliderVelocity;

    /**
     * Star rating for osu! standard
     * <p>
     * Can be <code>null</code> if the information is not available for this gamemode
     */
    private StarRating standardStarRating;

    /**
     * Star rating for taiko
     * <p>
     * Can be <code>null</code> if the information is not available for this gamemode
     */
    private StarRating taikoStarRating;

    /**
     * Star rating for ctb
     * <p>
     * Can be <code>null</code> if the information is not available for this gamemode
     */
    private StarRating ctbStarRating;

    /**
     * Star rating for mania
     * <p>
     * Can be <code>null</code> if the information is not available for this gamemode
     */
    private StarRating maniaStarRating;

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
     * The highest bpm found in this map
     */
    private double bpmMin;

    /**
     * The lowest bpm found in this map
     */
    private double bpmMax;

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
     * <p>
     * <i>I have no freakin' clue what this actually is....</i>
     */
    private long threadId;

    /**
     * Grade achieved in osu! standard
     * <u>
     * <li>0: silver SS</li>
     * <li>1: silver S</li>
     * <li>2: SS</li>
     * <li>3: S</li>
     * <li>4: A</li>
     * <li>5: B</li>
     * <li>6: C</li>
     * <li>7: D</li>
     * <li>9: no grade</li>
     * </u>
     */
    private int standardGrade;

    /**
     * Grade achieved in Taiko
     * <u>
     * <li>0: silver SS</li>
     * <li>1: silver S</li>
     * <li>2: SS</li>
     * <li>3: S</li>
     * <li>4: A</li>
     * <li>5: B</li>
     * <li>6: C</li>
     * <li>7: D</li>
     * <li>9: no grade</li>
     * </u>
     */
    private int taikoGrade;

    /**
     * Grade achieved in CTB
     * <u>
     * <li>0: silver SS</li>
     * <li>1: silver S</li>
     * <li>2: SS</li>
     * <li>3: S</li>
     * <li>4: A</li>
     * <li>5: B</li>
     * <li>6: C</li>
     * <li>7: D</li>
     * <li>9: no grade</li>
     * </u>
     */
    private int ctbGrade;

    /**
     * Grade achieved in osu!mania
     * <u>
     * <li>0: silver SS</li>
     * <li>1: silver S</li>
     * <li>2: SS</li>
     * <li>3: S</li>
     * <li>4: A</li>
     * <li>5: B</li>
     * <li>6: C</li>
     * <li>7: D</li>
     * <li>9: no grade</li>
     * </u>
     */
    private int maniaGrade;

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
     * <p>
     * <i>I suppose it's this again: https://msdn.microsoft.com/library/system.datetime.ticks(v=vs.100).aspx</i>
     * <p>
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
     * <p>
     * <i>I suppose it's this again: https://msdn.microsoft.com/library/system.datetime.ticks(v=vs.100).aspx</i>
     * <p>
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

    /**
     * Last modification time (?)
     * <p>
     * <b>NOTE: That question-mark comes from the wiki, so it is probably advised to just not use this field!</b>
     * <p>
     * From what I have seen, it looks like this is always <code>0</code>, so its probably unused, or something else.
     */
    private long lastModificationTime2;

    /**
     * Mania scroll speed
     */
    private int maniaScrollSpeed;

    private OsuBeatmapInfo() {
    }

    public static OsuBeatmapInfo[] parse(OsuDbInputStream iStream) throws IOException {
        long beatmapCount = iStream.readUInt32();
        if (beatmapCount > Integer.MAX_VALUE) {
            throw new IOException("beatmapCount to much to store the data...");
        }
        OsuBeatmapInfo[] beatmaps = new OsuBeatmapInfo[(int) beatmapCount];
        for (int i = 0; i < beatmaps.length; ++i) {
            beatmaps[i] = parseSingle(iStream);
        }
        return beatmaps;
    }

    public static OsuBeatmapInfo parseSingle(OsuDbInputStream iStream) throws IOException {
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
        beatmapInfo.overallDifficulty = iStream.readFloat();
        beatmapInfo.sliderVelocity = iStream.readDouble();
        beatmapInfo.standardStarRating = StarRating.parse(iStream);
        beatmapInfo.taikoStarRating = StarRating.parse(iStream);
        beatmapInfo.ctbStarRating = StarRating.parse(iStream);
        beatmapInfo.maniaStarRating = StarRating.parse(iStream);
        beatmapInfo.drainTime = iStream.readUInt32();
        beatmapInfo.totalTime = iStream.readUInt32();
        beatmapInfo.audioPreviewStartTime = iStream.readUInt32();
        beatmapInfo.timingPoints = TimingPoint.parse(iStream);
        beatmapInfo.calcMinMaxBpm();
        beatmapInfo.beatmapId = iStream.readUInt32();
        beatmapInfo.beatmapSetId = iStream.readUInt32();
        beatmapInfo.threadId = iStream.readUInt32();
        beatmapInfo.standardGrade = iStream.readUInt8();
        beatmapInfo.taikoGrade = iStream.readUInt8();
        beatmapInfo.ctbGrade = iStream.readUInt8();
        beatmapInfo.maniaGrade = iStream.readUInt8();
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
        beatmapInfo.lastModificationTime2 = iStream.readUInt32();
        beatmapInfo.maniaScrollSpeed = iStream.readUInt8();
        return beatmapInfo;
    }

    private void calcMinMaxBpm() {
        bpmMin = TimingPoint.calcBpmMin(timingPoints);
        bpmMax = TimingPoint.calcBpmMax(timingPoints);
    }
}
