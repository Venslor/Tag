package com.gihub.venslore.tag.utilities.command;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SubCommand {
    String name;
    String usageHover;
    String args;
    Flag[] flags;
}
