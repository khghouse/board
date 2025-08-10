package com.board.global.infrastructure.slack;

import lombok.Getter;

@Getter
public enum SlackChannel {

    NOTIFICATION("C089MA18UNL");

    private final String channelId;

    SlackChannel(String channelId) {
        this.channelId = channelId;
    }

}
