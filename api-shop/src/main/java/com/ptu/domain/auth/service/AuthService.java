package com.ptu.domain.auth.service;

import com.ptu.domain.auth.controller.dto.request.SignUpOAuthRequest;
import com.ptu.domain.auth.controller.dto.request.SignUpRequest;
import com.ptu.domain.auth.service.dto.SignUpDto;
import com.ptu.domain.member.service.MemberDataAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

  private final MemberDataAccessService memberDataAccessService;

  @Transactional
  public SignUpDto signUp(final SignUpRequest request) {
    return null;
  }

  @Transactional
  public SignUpDto signUpOAuth(final SignUpOAuthRequest request) {
    return null;
  }
}
