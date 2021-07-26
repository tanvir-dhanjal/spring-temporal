package com.codejam.dao.entities;

public enum FixtureStatus {
    SCHEDULED,
    PENDING_PLAYED_CONFIRMATION,
    CONFIRMED,
    PLAYED,
    PENDING_SCORE_UPDATE,
    SCORE_PUBLISHED,
    PENDING_SCORE_CONFIRMATION,
    SCORE_VALIDATED,
    INVALID,
    CANCELED,
    COMPLETED;

    public static FixtureStatus getEnum(String s) {
        switch (s) {
            case "scheduled": return SCHEDULED;
            case "confirmed": return CONFIRMED;
            case "played": return PLAYED;
            case "score_published": return SCORE_PUBLISHED;
            case "score_validated": return SCORE_VALIDATED;
            case "canceled": return CANCELED;
            default: return INVALID;
        }
    }
}
