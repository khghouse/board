package com.board.global.infrastructure.slack.dto;

public record SlackResponse(
        boolean ok,
        String error
) {
}
