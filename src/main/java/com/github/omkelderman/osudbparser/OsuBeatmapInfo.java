package com.github.omkelderman.osudbparser;

import com.github.omkelderman.osudbparser.io.OsuDbInputStream;
import lombok.Getter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
     * Raw ranked status value.
     * Use {@link #getRankedStatus()} instead if possible!
     * <p>
     * According to wiki: 4 = ranked, 5 = approved, 2 = pending/graveyard
     * <br>
     * <i><strike>Someone verify this, I don't trust the wiki page I copied this info from....</strike></i>
     * <p>
     * From my own observations: 0 = unkown, 1 = not submitted, 6 = qualified, and the values from the wiki seems to be
     * correct
     */
    private int rankedStatusRaw;

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
     */
    private Grade standardGrade;

    /**
     * Grade achieved in Taiko
     */
    private Grade taikoGrade;

    /**
     * Grade achieved in CTB
     */
    private Grade ctbGrade;

    /**
     * Grade achieved in osu!mania
     */
    private Grade maniaGrade;

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
    private GameMode gameMode;

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

    // calculated non-provided fields:

    /**
     * The highest bpm found in this map
     */
    private double bpmMin;

    /**
     * The lowest bpm found in this map
     */
    private double bpmMax;

    /**
     * The main / most used bpm. It looks like this is the value in parentheses as shown in-game.
     * <i>At least I sincerely hope it is...</i>
     * <p>
     * <b>NOTE: This is NOT (at least half the time I tested it) the bpm value provided by the osu!-api or website! I
     * have no clue where that value is based on...</b>
     */
    private double bpm;

    /**
     * Is the bpm variable? If yes, <code>bpmMin</code> and <code>bpmMax</code> are different and
     * <code>bpm</code> contains the "main" bpm. If not, they are all the same.
     */
    private boolean variableBpm;

    /**
     * The ranked status of this beatmap. <b>Note that this can return <code>null</code> if there is a value in the
     * file that I don't know about (yet), even though there is {@link RankedStatus#UNKNOWN}.</b>
     * <p>
     * So to summarize: there are two types of "unknown":
     * <ul>
     * <li>Unkown by me (the dev): <code>null</code></li>
     * <li>Unkown by osu: <code>{@link RankedStatus#UNKNOWN}</code>. This usually happens when the beatmap has not yet
     * been selected in the menu.</li>
     * </ul>
     * In case of the first one (well, actually always :P) {@link #getRankedStatusRaw()} will have the raw value, of which you
     * may be able to do something with.
     */
    private RankedStatus rankedStatus;

    private OsuBeatmapInfo() {
    }

    public static OsuBeatmapInfo[] parseArray(OsuDbInputStream iStream) throws IOException {
        long beatmapCount = iStream.readUInt32();
        if (beatmapCount > Integer.MAX_VALUE) {
            throw new IOException("beatmapCount to much to store the data...");
        }
        OsuBeatmapInfo[] beatmaps = new OsuBeatmapInfo[(int) beatmapCount];
        for (int i = 0; i < beatmaps.length; ++i) {
            beatmaps[i] = parse(iStream);
        }
        return beatmaps;
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
        beatmapInfo.rankedStatusRaw = iStream.readUInt8();
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
        beatmapInfo.timingPoints = TimingPoint.parseArray(iStream);
        beatmapInfo.beatmapId = iStream.readUInt32();
        beatmapInfo.beatmapSetId = iStream.readUInt32();
        beatmapInfo.threadId = iStream.readUInt32();
        beatmapInfo.standardGrade = Grade.valueOf(iStream.readUInt8());
        beatmapInfo.taikoGrade = Grade.valueOf(iStream.readUInt8());
        beatmapInfo.ctbGrade = Grade.valueOf(iStream.readUInt8());
        beatmapInfo.maniaGrade = Grade.valueOf(iStream.readUInt8());
        beatmapInfo.localOffset = iStream.readUInt16();
        beatmapInfo.stackLeniency = iStream.readFloat();
        beatmapInfo.gameMode = GameMode.valueOf(iStream.readUInt8());
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

        // calculate non-provided fields.
        beatmapInfo.calcMinMaxBpm();
        beatmapInfo.rankedStatus = RankedStatus.valueOf(beatmapInfo.rankedStatusRaw);
        return beatmapInfo;
    }

    private void calcMinMaxBpm() {
        bpmMin = TimingPoint.calcBpmMin(timingPoints);
        bpmMax = TimingPoint.calcBpmMax(timingPoints);

        // if bpmMin and bpmMax are different, there is a variable bpm
        variableBpm = !(bpmMax - bpmMin < 0.001);

        if (variableBpm) {
            // in that case, we also need to calculate the "main" bpm
            bpm = TimingPoint.calcMainBpm(timingPoints, totalTime);
        } else {
            // otherwise it's just the same
            bpm = bpmMax;
        }
    }

    public enum GameMode {
        //0x00 = osu!Standard, 0x01 = Taiko, 0x02 = CTB, 0x03 = Mania
        OSU(0), TAIKO(1), CTB(2), MANIA(3);

        private final int value;

        GameMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        private static final Map<Integer, GameMode> MAP;

        static {
            MAP = new HashMap<>();
            for (GameMode mode : values()) {
                MAP.put(mode.value, mode);
            }
        }

        public static GameMode valueOf(int i) {
            return MAP.get(i);
        }
    }

    public enum Grade {
        SSH(0), SS(1), SH(2), S(3), A(4), B(5), C(6), D(7), NONE(9);

        private final int value;

        Grade(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        private static final Map<Integer, Grade> MAP;

        static {
            MAP = new HashMap<>();
            for (Grade grade : values()) {
                MAP.put(grade.value, grade);
            }
        }

        public static Grade valueOf(int value) {
            return MAP.get(value);
        }
    }

    public enum RankedStatus {
        UNKNOWN(0), NOT_SUBMITTED(1), GRAVEYARD(2), RANKED(4), APPROVED(5), QUALIFIED(6);

        private final int value;

        RankedStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        private static final Map<Integer, RankedStatus> MAP;

        static {
            MAP = new HashMap<>();
            for (RankedStatus grade : values()) {
                MAP.put(grade.value, grade);
            }
        }

        public static RankedStatus valueOf(int value) {
            return MAP.get(value);
        }
    }
}
