package org.sean.factory;

import java.io.File;
import java.io.IOException;

public class CommonFactory {
    public static class Bytes {
        public static byte[] contentFromFile(File file) throws IOException {
            return java.nio.file.Files.readAllBytes(file.toPath());
        }
    }

    public static class Files {
        public static File fileFromPath(String path) {
            return new File(path);
        }
    }
}
