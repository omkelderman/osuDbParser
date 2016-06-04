package com.github.omkelderman.osudbparser;

import com.github.omkelderman.osudbparser.io.OsuDbInputStream;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class representing the osu!.db file inside the osu installation directory
 */
@Getter
public class OsuDbFile {
    /**
     * osu! version (e.g. 20150203)
     */
    private long osuVersion;

    /**
     * "Folder Count"
     * <p>
     * <i>actually what I have observed is more count of things (folders and random files) inside the
     * songs-directory</i>
     */
    private long folderCount;

    /**
     * AccountUnlocked (only false when the account is locked or banned in any way)
     */
    private boolean accountUnlocked;

    // there should be a "Date the account will be unlocked" field here
    // "skipped" it cause the wiki said "DateTime" as Data Type, was to lazy to figure out what that actually is
    @Getter(AccessLevel.NONE)
    private byte[] unknownDateTimeBytes = new byte[8];

    /**
     * Player name
     */
    private String playerName;

    /**
     * Array of beatmaps
     */
    private OsuBeatmapInfo[] beatmaps;

    // "skipped" unknown int
    @Getter(AccessLevel.NONE)
    private byte[] unknownIntBytes = new byte[4];

    private OsuDbFile() {
    }

    public static OsuDbFile parse(String filename) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filename)) {
            return parse(fileInputStream);
        }
    }

    public static OsuDbFile parse(InputStream fileInputStream) throws IOException {
        return parse(new OsuDbInputStream(fileInputStream));
    }

    public static OsuDbFile parse(OsuDbInputStream iStream) throws IOException {
        long version = iStream.readUInt32();
        if (version < 20140609) {
            throw new IOException("osu version too old");
        }

        OsuDbFile file = new OsuDbFile();
        file.osuVersion = version;
        file.folderCount = iStream.readUInt32();
        file.accountUnlocked = iStream.readBoolean();
        iStream.readFully(file.unknownDateTimeBytes);
        file.playerName = iStream.readString();
        file.beatmaps = OsuBeatmapInfo.parseArray(iStream, version);
        iStream.readFully(file.unknownIntBytes);
        return file;
    }

}
