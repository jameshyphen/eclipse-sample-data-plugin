package com.hyphen.sampledata.core;

// Can be a shared package with mock api record
public record Issue(int id, String name, String severity, String updatedAt) {}