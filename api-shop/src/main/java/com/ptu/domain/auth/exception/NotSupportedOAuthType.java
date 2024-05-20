package com.ptu.domain.auth.exception;

import com.ptu.common.exception.ClientAuthenticationException;
import com.ptu.common.exception.ErrorCode;

public class NotSupportedOAuthType extends ClientAuthenticationException {

  public NotSupportedOAuthType() {
    super(ErrorCode.NOT_SUPPORTED_OAUTH_TYPE);
  }
}
