package com.wendersonp.account.core.model.enumeration;

public enum BlockStatus {
    BLOCKED("B"), UNBLOCKED("U");

    private final String value;

    BlockStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
