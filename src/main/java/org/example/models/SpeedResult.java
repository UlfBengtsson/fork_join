package org.example.models;

import java.util.List;

public class SpeedResult<T> {
    private String name;
    private long duration;
    private List<T> collected;

    public SpeedResult(String name, long duration, List<T> collected) {
        this.name = name;
        this.duration = duration;
        this.collected = collected;
    }

    public String getName() {
        return name;
    }

    public long getDuration() {
        return duration;
    }

    public List<T> getCollected() {
        return collected;
    }
}
