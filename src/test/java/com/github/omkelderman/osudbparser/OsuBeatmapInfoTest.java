package com.github.omkelderman.osudbparser;

import com.github.omkelderman.osudbparser.io.OsuDbInputStream;
import org.junit.Test;

import static org.junit.Assert.*;

public class OsuBeatmapInfoTest {

    @Test
    public void testParse() throws Exception {
        OsuBeatmapInfo beatmapInfo = OsuBeatmapInfo.parse(new OsuDbInputStream(ClassLoader.getSystemResourceAsStream("osuBeatmap-single.db")), 20140609);

        assertEquals("Chasers", beatmapInfo.getArtistName());
        assertEquals("Chasers", beatmapInfo.getArtistNameUnicode());
        assertEquals("Lost", beatmapInfo.getSongTitle());
        assertEquals("Lost", beatmapInfo.getSongTitleUnicode());
        assertEquals("ktgster", beatmapInfo.getCreatorName());
        assertEquals("Hard", beatmapInfo.getDifficulty());
        assertEquals("Chasers - Lost.mp3", beatmapInfo.getAudioFileName());
        assertEquals("1e1cbbf6c326e8e908e5be71d9d9602e", beatmapInfo.getMd5BeatmapHash());
        assertEquals("Chasers - Lost (ktgster) [Hard].osu", beatmapInfo.getOsuFileName());
        assertEquals(4, beatmapInfo.getRankedStatusRaw());
        assertEquals(289, beatmapInfo.getHitcircleCount());
        assertEquals(219, beatmapInfo.getSliderCount());
        assertEquals(3, beatmapInfo.getSpinnerCount());
        assertEquals(635914054562956248L, beatmapInfo.getLastModificationTime());
        assertEquals(7F, beatmapInfo.getApproachRate(), 0.0001F);
        assertEquals(4F, beatmapInfo.getCircleSize(), 0.0001F);
        assertEquals(5F, beatmapInfo.getHpDrain(), 0.0001F);
        assertEquals(6F, beatmapInfo.getOverallDifficulty(), 0.0001F);
        assertEquals(1.5D, beatmapInfo.getSliderVelocity(), 0.0001D);
        assertNotNull(beatmapInfo.getStandardStarRating());
        assertNull(beatmapInfo.getTaikoStarRating());
        assertNull(beatmapInfo.getCtbStarRating());
        assertNull(beatmapInfo.getManiaStarRating());
        assertEquals(197L, beatmapInfo.getDrainTime());
        assertEquals(205948L, beatmapInfo.getTotalTime());
        assertEquals(115781L, beatmapInfo.getAudioPreviewStartTime());
        assertEquals(41, beatmapInfo.getTimingPoints().length);
        assertEquals(374113L, beatmapInfo.getBeatmapId());
        assertEquals(151878L, beatmapInfo.getBeatmapSetId());
        assertEquals(187996L, beatmapInfo.getThreadId());
        assertEquals(OsuBeatmapInfo.Grade.NONE, beatmapInfo.getStandardGrade());
        assertEquals(OsuBeatmapInfo.Grade.NONE, beatmapInfo.getTaikoGrade());
        assertEquals(OsuBeatmapInfo.Grade.NONE, beatmapInfo.getCtbGrade());
        assertEquals(OsuBeatmapInfo.Grade.NONE, beatmapInfo.getManiaGrade());
        assertEquals(0, beatmapInfo.getLocalOffset());
        assertEquals(0.7F, beatmapInfo.getStackLeniency(), 0.0001F);
        assertEquals(OsuBeatmapInfo.GameMode.OSU, beatmapInfo.getGameMode());
        assertEquals("", beatmapInfo.getSource());
        assertEquals("hardcore", beatmapInfo.getTags());
        assertEquals(0, beatmapInfo.getOnlineOffset());
        assertEquals("", beatmapInfo.getFont());
        assertTrue(beatmapInfo.isUnplayed());
        assertEquals(0L, beatmapInfo.getLastTimePlayed());
        assertFalse(beatmapInfo.isOsz2());
        assertEquals("151878 Chasers - Lost", beatmapInfo.getFolderName());
        assertEquals(635920983394330576L, beatmapInfo.getLastCheckedTime());
        assertFalse(beatmapInfo.isIgnoreBeatmapSounds());
        assertFalse(beatmapInfo.isIgnoreBeatmapSkin());
        assertFalse(beatmapInfo.isDisableStoryboard());
        assertFalse(beatmapInfo.isDisableVideo());
        assertFalse(beatmapInfo.isVisualOverride());
        assertEquals(0L, beatmapInfo.getLastModificationTime2());
        assertEquals(0, beatmapInfo.getManiaScrollSpeed());

        // calculated data
        assertEquals(OsuBeatmapInfo.RankedStatus.RANKED, beatmapInfo.getRankedStatus());
        assertEquals(170D, beatmapInfo.getBpmMin(), 0.0001D);
        assertEquals(170D, beatmapInfo.getBpmMax(), 0.0001D);
        assertEquals(170D, beatmapInfo.getBpm(), 0.0001D);
        assertFalse(beatmapInfo.isVariableBpm());
    }

    @Test
    public void testParseArray() throws Exception {
        OsuBeatmapInfo[] beatmapInfos = OsuBeatmapInfo.parseArray(new OsuDbInputStream(ClassLoader.getSystemResourceAsStream("osuBeatmap-array.db")), 20140609);
        assertEquals(33, beatmapInfos.length);

        // beatmap-specific options are tested in the other test
    }
}