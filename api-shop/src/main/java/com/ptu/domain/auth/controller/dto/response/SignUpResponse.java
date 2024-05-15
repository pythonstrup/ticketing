package com.ptu.domain.auth.controller.dto.response;

import com.ptu.domain.member.domain.MemberSocialLogin;

public record SignUpResponse(long memberId, long socialLoginId) {

  public static SignUpResponse of(final MemberSocialLogin socialLogin) {
    return new SignUpResponse(socialLogin.getMember().getId(), socialLogin.getId());
  }
}
