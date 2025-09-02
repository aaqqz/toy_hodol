package com.hodol.toy_hodol.domain.auth.controller.request;

import com.hodol.toy_hodol.domain.auth.service.request.SignupServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class SignupRequest {

    private final String email;
    private final String password;
    private final String name;

    public SignupServiceRequest toServiceRequest() {
        return SignupServiceRequest.of(this);
    }
}
