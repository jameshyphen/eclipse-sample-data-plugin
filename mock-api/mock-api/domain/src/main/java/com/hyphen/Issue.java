package com.hyphen;

import java.time.Instant;

public record Issue(int id, String name, String severity, String updatedAt) {
}