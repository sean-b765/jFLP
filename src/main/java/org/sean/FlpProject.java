package org.sean;

import org.sean.event.Event;

import java.util.ArrayList;
import java.util.List;

public class FlpProject {
    private String filename;
    private String version;
    private List<Event> events = new ArrayList<>();
    private float tempo;

    public float getTempo() { return this.tempo; }

    public void setFilename(String fn) { this.filename = fn; }
    public void setTempo(float t) { this.tempo = t; }
    public void setVersion(String v) { this.version = v; }
    public void addEvent(Event e) { this.events.add(e); }
}
