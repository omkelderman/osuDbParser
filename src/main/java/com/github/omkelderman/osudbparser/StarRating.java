package com.github.omkelderman.osudbparser;

import com.github.omkelderman.osudbparser.io.OsuDbInputStream;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing the star-ratings for a beatmap. There are multiple star-ratings available for each beatmap since
 * it can depend on which mod or mods you choose. The only mods that affect the star-rating (as fair as I have seen)
 * are:
 * <ul>
 * <li>Easy</li>
 * <li>HardRock</li>
 * <li>HalfTime</li>
 * <li>DoubleTime</li>
 * </ul>
 * Because <code>Easy</code> and <code>HardRock</code> cannot be applied at the same time, the same is true for
 * <code>HalfTime</code> and <code>DoubleTime</code>, not every combination of these mods exist. In the end there are
 * nine combinations that are valid (as of known at moment of writing this):
 * <ul>
 * <li>No mods</li>
 * <li>Easy</li>
 * <li>HardRock</li>
 * <li>HalfTime</li>
 * <li>DoubleTime</li>
 * <li>Easy + HalfTime</li>
 * <li>Easy + DoubleTime</li>
 * <li>HardRock + HalfTime</li>
 * <li>HardRock + DoubleTime</li>
 * </ul>
 */
public class StarRating {
    // as listed on https://osu.ppy.sh/wiki/Osr_(file_format): Easy | HardRock | HalfTime | DoubleTime
    private static final long AFFECTED_MODS_BITS = (2 | 16 | 256 | 64);
    private static final long EZ_HR_BITS = (2 | 16);
    private static final long HT_DT_BITS = (256 | 64);

    private Map<Long, Double> ratings = new HashMap<>();

    private StarRating() {
    }

    public static StarRating parse(OsuDbInputStream iStream) throws IOException {
        long amount = iStream.readUInt32();
        if (amount == 0) {
            return null;
        }

        StarRating starRating = new StarRating();
        for (long i = 0; i < amount; ++i) {
            iStream.readExpectedUInt8(0x08);
            long modCombo = iStream.readUInt32();
            iStream.readExpectedUInt8(0x0D);
            double rating = iStream.readDouble();
            starRating.ratings.put(modCombo, rating);
        }
        return starRating;
    }

    /**
     * Shortcut for {@link #forMods(long)} called with <code>0L</code>
     *
     * @return Star rating with no mods
     */
    public double forNoMod() {
        return forMods(0L);
    }

    /**
     * Get the star rating for the supplied mod combination
     *
     * @param mods A bitset of mods as specified in the osu!api documentation. Mods that do not affect the star rating
     *             are ignored.
     * @return Star rating for that mod combination
     * @throws IllegalArgumentException If an invalid mod-combo has been given. For example <code>Easy</code> and
     *                                  <code>HardRock</code> enabled at the same time.
     */
    public double forMods(long mods) throws IllegalArgumentException {
        mods = mods & AFFECTED_MODS_BITS;
        if (((mods & EZ_HR_BITS) == EZ_HR_BITS) || ((mods & HT_DT_BITS) == HT_DT_BITS)) {
            // illegal mod-combination detected
            throw new IllegalArgumentException("invalid mods");
        }
        // if this gives a NPE either the file is corrupted or something in the format of that file has changed....
        return ratings.get(mods);
    }

    /**
     * Get the star rating for the supplied mods
     * <p>
     * Each element in the <code>modArray</code> is the bit-offset of that mod as specified in the osu!api
     * documentation.
     * <ul>
     * <li>Easy (2 or 0b10) = 1</li>
     * <li>HardRock (16 or 0b10000) = 4</li>
     * <li>DoubleTime (64 or 0b 1000000) = 6</li>
     * <li>HalfTime (256 or 0b100000000) = 8	</li>
     * </ul>
     *
     * @param modArray An array of mods, where each element is is a number corresponding to a mod according
     *                 to the above information
     * @return Star rating for that mod combination
     * @throws IllegalArgumentException If an invalid mod-combo has been given. For example <code>Easy</code> and
     *                                  <code>HardRock</code> enabled at the same time.
     */
    public double forModArray(int... modArray) throws IllegalArgumentException {
        long mods = 0L;
        for (int mod : modArray) {
            mods |= (1 << mod);
        }
        return forMods(mods);
    }
}
