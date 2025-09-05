package stugapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

public class CustomJwtConverter implements Converter<Jwt, CustomJwt> {

  @Value("${jwt.auth.converter.resource-id}")
  private String resourceId;

  @Override
  public CustomJwt convert(@NonNull Jwt jwt) {
    // Extract claims and authorities as needed
    Collection<GrantedAuthority> authorities = extractResourceRoles(jwt);

    // You can also map other information from the Jwt to the custom token
    var customJwt = new CustomJwt(jwt, authorities);
    customJwt.setFirstname(jwt.getClaimAsString("given_name"));
    customJwt.setLastname(jwt.getClaimAsString("family_name"));
    return customJwt;
  }

  private Collection<GrantedAuthority> extractResourceRoles(Jwt jwt) {
    Map<String, Object> resourceAccess;
    Map<String, Object> resource;
    Collection<String> resourceRoles;
    if (jwt.getClaim("resource_access") == null) {
      return Set.of();
    }
    resourceAccess = jwt.getClaim("resource_access");

    if (resourceAccess.get(resourceId) == null) {
      return Set.of();
    }
    resource = (Map<String, Object>) resourceAccess.get(resourceId);

    resourceRoles = (Collection<String>) resource.get("roles");
    return resourceRoles
      .stream()
      .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
      .collect(Collectors.toSet());
  }
}
