package org.sean.event;

import org.sean.parser.ProgramOptions;

import java.lang.reflect.Field;

/**
 * one of:<br>
 *           byte (BYTE 1*bytes)<br>
 *           char (WORD 2*bytes)<br>
 *           int (DWORD 4*bytes)<br>
 *
 * Depending on eventId
 */
public class Event {
    public int eventId;
    public Object content;
    public String name;

    public Event(int eventId) {
        this.eventId = eventId;

        if (ProgramOptions.get().reflection) this.name = getFieldName(this.eventId);
    }

    public static String getFieldName(int value) {
        Class<Events> eventsClass = Events.class;

        Field[] fields = eventsClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (field.getInt(null) == value) {
                    return field.getName();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return null; // Field not found
    }
}
