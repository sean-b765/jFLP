package org.sean.factory;

import org.sean.event.Event;
import org.sean.event.Events;

public class EventFactory {
    public static Event build(int eventId, Object data) {
        Event evt = new Event(eventId);
        evt.content = data;
        return evt;
    }
}
