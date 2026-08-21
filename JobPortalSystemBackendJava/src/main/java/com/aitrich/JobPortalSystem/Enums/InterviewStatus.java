package com.aitrich.JobPortalSystem.Enums;

// Status only for the Interview module - kept separate from Application's
// Status enum so nothing about Application/Status.java needs to change.
public enum InterviewStatus {
    SCHEDULED,
    RESCHEDULED,
    COMPLETED,
    CANCELLED
}
