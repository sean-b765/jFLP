package org.sean;

import org.sean.factory.CommonFactory;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        File file = CommonFactory.Files.fileFromPath("./sample.flp");
        Parser parser = new Parser(CommonFactory.Bytes.contentFromFile(file));
        System.out.println(parser.project);
    }
}