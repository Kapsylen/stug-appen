package stugapi.application.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import stugapi.presentation.TokenResponse;

@Service
@Slf4j
public class TokenService {

  @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
  private String issuerUri;

  private final WebClient.Builder client;

  public TokenService(WebClient.Builder client) {
    this.client = client;
  }

  public Mono<TokenResponse> fetchToken(
    String grantType,
    String clientId,
    String username,
    String password) {

 return client
   .baseUrl(issuerUri)
   .build().post()
   .uri("/protocol/openid-connect/token")
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .bodyValue(formData(grantType, clientId, username, password))
      .retrieve()
      .bodyToMono(TokenResponse.class);
  }

  private MultiValueMap<String, String> formData(String grantType, String clientId, String username, String password) {
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", grantType);
    formData.add("client_id", clientId);
    formData.add("username", username);
    formData.add("password", password);
    return formData;
  }
}


