package stugapi.presentation.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import stugapi.application.service.FakturaService;
import stugapi.presentation.dto.FakturaDto;

import java.util.List;

import static stugapi.presentation.dto.FakturaDto.toFakturaDtoBuilder;

@RestController
@RequestMapping("api/v1/faktura")
@AllArgsConstructor
@Slf4j
public class FakturaController {

  private final FakturaService fakturaService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasAnyRole('admin_user','base_user')")
  public FakturaDto createFaktura(@RequestBody  @Valid FakturaDto fakturaDto) {
    log.info("createFaktura: {}", fakturaDto);
    return toFakturaDtoBuilder(fakturaService.saveFaktura(fakturaDto))
      .build();
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('admin_user')")
  public void deleteFaktura(@PathVariable String id) {
    fakturaService.delete(id);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('admin_user','base_user')")
  public FakturaDto updateFaktura(@PathVariable String id, @Valid @RequestBody FakturaDto updateFaktura) {
    return toFakturaDtoBuilder(fakturaService.update(id, updateFaktura))
      .build();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('admin_user','base_user')")
  public FakturaDto getFaktura(@PathVariable String id) {
    return toFakturaDtoBuilder(fakturaService.find(id))
      .build();
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('admin_user','base_user')")
  public List<FakturaDto> getAllFaktura() {
    return fakturaService.findAll()
      .stream()
      .map(FakturaDto::toFakturaDto)
      .toList();
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('admin_user')")
  public void deleteAllFaktura() {
    fakturaService.deleteAll();
  }
}
