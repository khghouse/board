package com.board.service.slack.response;

public record SlackResponse(
        boolean ok,
        String error
) {
}
