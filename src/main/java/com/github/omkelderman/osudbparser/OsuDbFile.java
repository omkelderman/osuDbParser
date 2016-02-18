package com.github.omkelderman.osudbparser;

import lombok.Getter;
import lombok.ToString;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Class representing the osu!.db file inside the osu installation directory
 */
@Getter
@ToString
public class OsuDbFile {
    private long osuVersion;
    private long folderCount;
    private boolean accountUnlocked;
    private String playerName;
    private OsuBeatmapInfo[] beatmaps;

    private OsuDbFile() {
    }

    public static OsuDbFile parse(String filename) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filename)) {
            return parse(fileInputStream);
        }
    }

    public static OsuDbFile parse(FileInputStream fileInputStream) throws IOException {
        return parse(new OsuDbInputStream(fileInputStream));
    }

    public static OsuDbFile parse(OsuDbInputStream iStream) throws IOException {
        long version = iStream.readUInt32();
        if (version < 20140609) {
            throw new IOException("osu version to old");
        }

        OsuDbFile file = new OsuDbFile();
        file.osuVersion = version;
        file.folderCount = iStream.readUInt32();
        file.accountUnlocked = iStream.readBoolean();
        // skip "DateTime - Date the account will be unlocked"
        iStream.skipFully(8);
        file.playerName = iStream.readString();
        long beatmapCount = iStream.readUInt32();
        if (beatmapCount > Integer.MAX_VALUE) {
            throw new IOException("beatmapCount to much to store the data...");
        }
        file.beatmaps = new OsuBeatmapInfo[(int) beatmapCount];
        OsuBeatmapInfo.parse(file.beatmaps, iStream);

        return file;
    }

}
