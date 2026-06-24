package com.todomvc.data;

public enum Filter {

    ALL("#/", "All"),
    ACTIVE("#/active", "Active"),
    COMPLETED("#/completed", "Completed");

    private final String hash;
    private final String label;

    Filter(String hash, String label) {
        this.hash = hash;
        this.label = label;
    }

    public String hash() {
        return hash;
    }

    public String label() {
        return label;
    }
}
