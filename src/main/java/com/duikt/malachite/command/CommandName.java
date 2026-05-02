package com.duikt.malachite.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommandName {

    ABOUT("/about"),
    START("/start"),
    LANGUAGE("/language");

    private final String name;
}
