package org.sean;

import org.sean.parser.Parser;
import org.sean.parser.ProgramOptions;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new ProgramOptions(args);

        if (ProgramOptions.get().parallel) {
            for (String filename : ProgramOptions.get().filenames) {
                Parser parser = new Parser(filename);
                Thread thread = new Thread(() -> {
                    parser.parse();
                    Thread.currentThread().interrupt();
                });
                thread.start();
            }
        } else {
            for (String filename : ProgramOptions.get().filenames) {
                Parser parser = new Parser(filename);
                parser.parse();
            }
        }
    }
}