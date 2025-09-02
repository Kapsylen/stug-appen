package stugapi.presentation.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import stugapi.application.service.ArendeService;
import stugapi.presentation.dto.ArendeDto;

import java.util.List;

import static stugapi.presentation.dto.ArendeDto.toArendeDtoBuilder;

@RestController
@RequestMapping("api/v1/arende")
@AllArgsConstructor
@Slf4j
public class ArendeController {

  private final ArendeService arendeService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasAnyRole('admin_user','base_user')")
  public ArendeDto createArende(@RequestBody @Valid ArendeDto arendeDto) {
    log.info("createArende {}", arendeDto);
    return toArendeDtoBuilder(arendeService.saveArende(arendeDto))
      .build();
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('admin_user')")
  public void deleteArende(@PathVariable String id) {
    arendeService.deleteById(id);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('admin_user','base_user')")
  public ArendeDto getArende(@PathVariable String id) {
    return toArendeDtoBuilder(arendeService.findById(id))
      .build();
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('admin_user')")
  public ArendeDto updateArende(@PathVariable String id, @Valid @RequestBody ArendeDto updateArende) {
    return toArendeDtoBuilder(arendeService.update(id, updateArende))
      .build();
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('admin_user','base_user')")
  public List<ArendeDto> getAllArende() {
    return arendeService.findAll().stream().map(arenden -> ArendeDto.toArendeDtoBuilder(arenden).build()).toList();
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('admin_user')")
  public void deleteAllArende() {
    arendeService.deleteAll();
  }
}
