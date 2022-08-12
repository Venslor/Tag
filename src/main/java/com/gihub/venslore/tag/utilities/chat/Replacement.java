package com.gihub.venslore.tag.utilities.chat;

import java.util.HashMap;
import java.util.Map;

public class Replacement {
    private Map<Object, Object> replacements = new HashMap<Object, Object>();
    private String message;

    public Replacement(String message) {
        this.message = message;
    }

    public Replacement add(Object current, Object replacement) {
        this.replacements.put(current, replacement);
        return this;
    }

    public String toString() {
        this.replacements.keySet().forEach(current -> {
            this.message = this.message.replace(String.valueOf(current), String.valueOf(this.replacements.get(current)));
        });
        return Color.translate(this.message);
    }

    public String toString(boolean ignored) {
        this.replacements.keySet().forEach(current -> {
            this.message = this.message.replace(String.valueOf(current), String.valueOf(this.replacements.get(current)));
        });
        return this.message;
    }

    public Map<Object, Object> getReplacements() {
        return this.replacements;
    }

    public String getMessage() {
        return this.message;
    }

    public void setReplacements(Map<Object, Object> replacements) {
        this.replacements = replacements;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

