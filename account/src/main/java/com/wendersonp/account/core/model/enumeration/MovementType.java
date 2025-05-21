package com.wendersonp.account.core.model.enumeration;

public enum MovementType {

    WITHDRAW("W"), DEPOSIT("D");

    private final String value;

    MovementType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
