package org.sean.factory;

import org.sean.event.Event;
import org.sean.event.Events;

public class EventFactory {
    public static Event build(int eventId, Object data) {
        if (eventId == Events.FLP_Byte || eventId == Events.FLP_Word || eventId == Events.FLP_Text || eventId == Events.FLP_Int) return null;
        Event evt = new Event(eventId);
        evt.content = data;
        return evt;
    }
}
