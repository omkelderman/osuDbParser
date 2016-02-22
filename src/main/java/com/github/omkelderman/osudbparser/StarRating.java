package com.github.omkelderman.osudbparser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StarRating {
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
            int leByte = iStream.readUInt8();
            if (leByte != 0x08) {
                throw new IOException("expected byte 0x08");
            }
            long modCombo = iStream.readUInt32();
            leByte = iStream.readUInt8();
            if (leByte != 0x0D) {
                throw new IOException("expected byte 0x0D");
            }
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
        // '0L' should always exist, every mode has a nomod-star-rating
        // if this not exists, this whole StarRating-object shouldn't have existed in the first place!
        return forMods(0L);
    }

    /**
     * Get the star rating for the supplied mod combination
     *
     * @param mods A bitset of mods as specified in the osu!api documentation
     * @return Star rating for that mod combination or <code>null</code> if not available for that mod combination
     */
    public Double forMods(long mods) {
        return ratings.get(mods);
    }

    /**
     * Get the star rating for the supplied mods
     * <p>
     * Each element in the <code>modArray</code> is the bit-offset of that mod as specified in the osu!api
     * documentation. Some examples:
     * <ul>
     *     <li>NoFail (1) = 0</li>
     *     <li>Easy (2) = 1</li>
     *     <li>Hidden (8) = 3</li>
     *     <li>HardRock (16) = 4</li>
     *     <li>HardRock (16) = 4</li>
     *     <li>DoubleTime (64) = 6</li>
     *     <li>Flashlight (1024) = 10</li>
     * </ul>
     *
     *
     * @param modArray An array of mods, where each element is is a number corresponding to a mod according
     *                 to the above information
     * @return Star rating for that mod combination or <code>null</code> if not available for that mod combination
     */
    public Double forModArray(int... modArray) {
        long mods = 0L;
        for (int mod : modArray) {
            mods |= (1 << mod);
        }
        return forMods(mods);
    }
}
