package org.sean.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.sean.FlpProject;
import org.sean.event.Event;
import org.sean.event.Events;
import org.sean.exception.UnexpectedEndOfStreamException;
import org.sean.factory.CommonFactory;
import org.sean.factory.EventFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

public class Parser {
    private byte[] content;
    private int cursor;
    private FlpProject project;
    private String filename;

    public Parser(byte[] content) {
        this.content = content;
        this.project = new FlpProject();

        try {
            validateHeader();
            validateDataChunk();
        } catch (UnexpectedEndOfStreamException ignored) { }
    }

    public Parser(String filepath) throws IOException {
        File file = CommonFactory.Files.fileFromPath(filepath);
        this.filename = filepath.split("/")[filepath.split("/").length - 1];
        this.content = CommonFactory.Bytes.contentFromFile(file);
        this.project = new FlpProject();

        try {
            validateHeader();
            validateDataChunk();
        } catch (UnexpectedEndOfStreamException ignored) { }
    }

    public void parse() {
        long start = System.currentTimeMillis();

        System.out.println("Starting parse of " + this.filename);
        parseAllEvents(true);

        long end = System.currentTimeMillis() - start;
        System.out.println("Done parsing " + filename + " | Tempo " + project.getTempo() + " | Took " + end + "ms | Thread " + Thread.currentThread().getName());
    }

    public void parseOnlyKnownEvents() {
        parseAllEvents(false);
    }

    private void validateHeader() throws IllegalArgumentException, UnexpectedEndOfStreamException {
        String header = "";
        for (int i = 0; i < 4; i++) {
            header += (char) readByte();
        }

        if (!header.equals("FLhd")) throw new IllegalArgumentException("Invalid FLP file.");
        // skip the header data for now ( DWORD length + 3 WORD events )
        cursor += 10;
    }

    private void validateDataChunk() throws IllegalArgumentException, UnexpectedEndOfStreamException {
        String header = "";
        for (int i = 0; i < 4; i++) {
            header += (char) readByte();
        }

        if (!header.equals("FLdt")) throw new IllegalArgumentException("Could not parse FLdt chunk");
    }

    private void parseAllEvents(boolean addUnknownEvents) {
        try {
            while (cursor < content.length - 1) {
                Event evt = readEvent();
                if (addUnknownEvents || evt.name != null) this.project.addEvent(evt);

                if (ProgramOptions.get().onlyTempo && evt.eventId == Events.FLP_Tempo || evt.eventId == Events.FLP_FineTempo) {
                    System.out.println(this.project.getTempo() + " Found tempo :: " + cursor + " / " + (content.length - 1));
                    break;
                }
            }
        } catch (UnexpectedEndOfStreamException ex) {
//            ex.printStackTrace();
        }
    }

    private Event readEvent() throws UnexpectedEndOfStreamException {
        // first 4 bytes of event as the eventId
        int eventId = readByte();
        // first chunk = byte
        int data = readByte();

        if (eventId >= Events.FLP_Word && eventId < Events.FLP_Text) {
            // word
            data = data | (readByte() << 8);
        }
        if (eventId >= Events.FLP_Int && eventId < Events.FLP_Text) {
            // dword
            data = data | (readByte() << 16);
            data = data | (readByte() << 24);
        }
        if (eventId >= Events.FLP_Text) {
            // variable-length chunk
            // 1. get a byte
            // 2. read first 7 bits of byte into "size"
            // 3. if bit 7 (most significant bit) is off, continue reading bytes into size by starting again from (1.)
            int len = data & 0x7F;
            int shift = 0;
            // (3)
            while ((data & 0x80) != 0) {
                // (1)
                data = readByte();
                // (2)
                len = len | ((data & 0x7F) << (shift += 7));
            }

            return buildEvent(eventId, readString(len));
        }

        return buildEvent(eventId, data);
    }

    private Event buildEvent(int eventId, int data) {
        switch (eventId) {
            case Events.FLP_FineTempo:
                project.setTempo((float) data / 1000);
                break;
            case Events.FLP_Tempo:
                project.setTempo(data);
                break;
        }

        return EventFactory.build(eventId, data);
    }

    private Event buildEvent(int eventId, String data) {
        switch (eventId) {
            case Events.FLP_Text_Version:
                project.setVersion(data);
                break;
        }
        return EventFactory.build(eventId, data);
    }

    private int readInt() throws UnexpectedEndOfStreamException {
        int rtn = readByte();
        rtn = rtn | (readByte() << 8);
        rtn = rtn | (readByte() << 16);
        rtn = rtn | (readByte() << 24);
        return rtn;
    }

    private int readByte() throws UnexpectedEndOfStreamException {
        if (cursor + 1 >= content.length) throw new UnexpectedEndOfStreamException();
        return content[cursor++] & 0xFF;
    }

    private String readString(int len) throws UnexpectedEndOfStreamException {
        if (cursor + len >= content.length) throw new UnexpectedEndOfStreamException();
        String result = "";
        for (int i = 0; i < len; i++) {
            result += (char) readByte();
        }
        return result;
    }

    public String serializeProject(boolean parseUnicodeStrings) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(project);
    }
}
