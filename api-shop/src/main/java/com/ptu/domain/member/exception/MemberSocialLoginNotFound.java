package com.ptu.domain.member.exception;

import com.ptu.common.exception.ClientRuntimeException;
import com.ptu.common.exception.ErrorCode;

public class MemberSocialLoginNotFound extends ClientRuntimeException {

  public MemberSocialLoginNotFound() {
    super(ErrorCode.MEMBER_SOCIAL_LOGIN_NOT_FOUND);
  }
}
