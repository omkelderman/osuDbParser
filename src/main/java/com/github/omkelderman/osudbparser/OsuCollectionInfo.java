package com.github.omkelderman.osudbparser;

import com.github.omkelderman.osudbparser.io.OsuDbInputStream;

import java.io.IOException;

public class OsuCollectionInfo {
    private String name;
    private String[] beatmaps;

    private OsuCollectionInfo() {
    }

    public static OsuCollectionInfo[] parseArray(OsuDbInputStream iStream) throws IOException {
        long collectionCount = iStream.readUInt32();
        if (collectionCount > Integer.MAX_VALUE) {
            throw new IOException("collectionCount to much to store the data...");
        }
        OsuCollectionInfo[] collections = new OsuCollectionInfo[(int) collectionCount];
        for (int i = 0; i < collections.length; i++) {
            collections[i] = parse(iStream);
        }
        return collections;
    }

    private static OsuCollectionInfo parse(OsuDbInputStream iStream) throws IOException {
        OsuCollectionInfo collectionInfo = new OsuCollectionInfo();
        collectionInfo.name = iStream.readString();
        long beatmapCount = iStream.readUInt32();
        if (beatmapCount > Integer.MAX_VALUE) {
            throw new IOException("beatmapCount to much to store the data...");
        }
        collectionInfo.beatmaps = new String[(int) beatmapCount];
        for (int i = 0; i < collectionInfo.beatmaps.length; ++i) {
            collectionInfo.beatmaps[i] = iStream.readString();
        }
        return collectionInfo;
    }
}
