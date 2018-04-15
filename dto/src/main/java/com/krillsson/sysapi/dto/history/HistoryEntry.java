package com.krillsson.sysapi.dto.history;

public class HistoryEntry<T> {
    private String date;
    private T value;

    public HistoryEntry(String date, T value) {
        this.date = date;
        this.value = value;
    }

    public HistoryEntry() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
