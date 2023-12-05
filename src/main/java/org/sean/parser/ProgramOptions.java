package org.sean.parser;

import java.util.Arrays;
import java.util.List;

public class ProgramOptions {
    private static ProgramOptions instance = null;
    /** -t Only retrieve the tempo */
    public boolean onlyTempo = false;
    /** -p Parse each file in parallel */
    public boolean parallel = false;
    /** -r Add the event name using java reflection (big performance hit) */
    public boolean reflection = false;
    public List<String> filenames;

    public ProgramOptions(String[] args) {
        if (instance != null) {
            System.err.println("Cannot instantiate ProgramOptions twice");
            System.exit(-1);
        }

        if (Arrays.asList(args).contains("--help") || Arrays.asList(args).contains("-h")) {
            System.out.println("Usage:\njflp [path1] [path2] ...\nPlease provide paths relative to this jar.\nOptions:\n\t-t --tempo stop early once tempo has been determined\n\t-p --parallel parse each file in parallel\n\t-r --reflection retrieve event \"name\" along with the events (performance heavier)");
            System.exit(-1);
            return;
        }

        if (Arrays.asList(args).contains("--parallel") || Arrays.asList(args).contains("-p")) {
            this.parallel = true;
        }

        if (Arrays.asList(args).contains("--reflection") || Arrays.asList(args).contains("-r")) {
            this.reflection = true;
        }

        if (Arrays.asList(args).contains("--tempo") || Arrays.asList(args).contains("-t")) {
            this.onlyTempo = true;
        }

        this.filenames = Arrays.stream(args).filter(s -> !s.startsWith("-")).toList();

        instance = this;
    }

    public static ProgramOptions get() {
        return instance;
    }
}
