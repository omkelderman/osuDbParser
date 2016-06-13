package com.github.omkelderman.osudbparser;

import com.github.omkelderman.osudbparser.io.OsuDbInputStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CollectionsDbFile {
    /**
     * osu! version (e.g. 20150203)
     */
    private long osuVersion;

    /**
     * Array of collections
     */
    private OsuCollectionInfo[] collections;

    private CollectionsDbFile() {
    }

    public static CollectionsDbFile parse(String filename) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filename)) {
            return parse(fileInputStream);
        }
    }

    private static CollectionsDbFile parse(InputStream inputStream) throws IOException {
        return parse(new OsuDbInputStream(inputStream));
    }

    private static CollectionsDbFile parse(OsuDbInputStream iStream) throws IOException {
        CollectionsDbFile file = new CollectionsDbFile();
        file.osuVersion = iStream.readUInt32();
        file.collections = OsuCollectionInfo.parseArray(iStream);
        return file;
    }
}
