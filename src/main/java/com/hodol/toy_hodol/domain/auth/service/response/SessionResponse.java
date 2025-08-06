package com.hodol.toy_hodol.domain.auth.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SessionResponse {
    private final String accessToken;
}
