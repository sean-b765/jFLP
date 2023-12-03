package org.sean;

import org.sean.event.Event;
import org.sean.event.Events;
import org.sean.exception.EndOfStreamException;
import org.sean.factory.EventFactory;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private byte[] content;
    private int cursor;
    public FlpProject project;

    private List<Event> events = new ArrayList<>();

    public Parser(byte[] content) {
        this.content = content;
        this.project = new FlpProject();

        try {
            validateHeader();
            validateDataChunk();
        } catch (EndOfStreamException ignored) { }

        parseAllEvents();
        System.out.println("Done");
    }

    private void validateHeader() throws IllegalArgumentException, EndOfStreamException {
        String header = "";
        for (int i = 0; i < 4; i++) {
            header += (char) readByte();
        }

        if (!header.equals("FLhd")) throw new IllegalArgumentException("Invalid FLP file.");
        // skip the header data for now ( DWORD length + 3 WORD events )
        cursor += 10;
    }

    private void validateDataChunk() throws IllegalArgumentException, EndOfStreamException {
        String header = "";
        for (int i = 0; i < 4; i++) {
            header += (char) readByte();
        }

        if (!header.equals("FLdt")) throw new IllegalArgumentException("Could not parse FLdt chunk");
    }

    private void parseAllEvents() {
        try {
            while (cursor < content.length - 1) {
                Event evt = readNextEvent();
                if (evt != null && evt.name != null) {
                    System.out.println(evt.name);
                    events.add(evt);
                }
            }
        } catch (EndOfStreamException ex) {
//            ex.printStackTrace();
        }
    }

    private Event readNextEvent() throws EndOfStreamException {
        // first 4 bytes of event as the eventId
        int eventId = readByte();
        int data = readByte();

        if (eventId >= Events.FLP_Word && eventId < Events.FLP_Text) {
            data = data | (readByte() << 8);
        }
        if (eventId >= Events.FLP_Int && eventId < Events.FLP_Text) {
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

    private int readInt() throws EndOfStreamException {
        int rtn = readByte();
        rtn = rtn | (readByte() << 8);
        rtn = rtn | (readByte() << 16);
        rtn = rtn | (readByte() << 24);
        return rtn;
    }

    private int readByte() throws EndOfStreamException {
        if (cursor + 1 >= content.length) throw new EndOfStreamException();
        return content[cursor++] & 0xFF;
    }

    private String readString(int len) throws EndOfStreamException {
        if (cursor + len >= content.length) throw new EndOfStreamException();
        String result = "";
        for (int i = 0; i < len; i++) {
            result += (char) readByte();
        }
        return result;
    }
}
