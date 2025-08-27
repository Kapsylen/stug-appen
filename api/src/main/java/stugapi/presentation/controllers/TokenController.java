package stugapi.presentation.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import stugapi.application.service.TokenService;
import stugapi.presentation.TokenResponse;

@RestController
@RequestMapping("api/v1/token")
@AllArgsConstructor
public class TokenController {

  private final TokenService tokenService;

@GetMapping
  public Mono<TokenResponse> fetchToken(
    @RequestHeader String grantType,
    @RequestHeader String clientId,
    @RequestHeader String username,
    @RequestHeader String password
) {
    return tokenService.fetchToken(
      grantType,
      clientId,
      username,
      password);
  }
}
