package stugapi.presentation.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import stugapi.application.service.KontaktService;
import stugapi.presentation.dto.KontaktDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/kontakt")
@AllArgsConstructor
@PreAuthorize("hasRole('client_admin')")
public class KontaktController {

  private final KontaktService kontaktService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public KontaktDto createKontakt(@RequestBody @Valid KontaktDto kontaktDto) {
    return KontaktDto.toKontaktDtoBuilder(kontaktService.saveKontakt(kontaktDto))
      .build();
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteKontakt(@PathVariable String id) {
    kontaktService.delete(id);
  }

  @GetMapping("/{id}")
  public KontaktDto getKontakt(@PathVariable String id) {
    return KontaktDto.toKontaktDtoBuilder(kontaktService.find(id))
      .build();
  }

  @PutMapping("/{id}")
  public KontaktDto updateKontakt(@PathVariable String id, @RequestBody KontaktDto updateKontakt) {
    return KontaktDto.toKontaktDtoBuilder(kontaktService.update(id, updateKontakt))
      .build();
  }

  @GetMapping
  public List<KontaktDto> getAllKontakt() {
    return kontaktService.findAll()
      .stream()
      .map(KontaktDto::fromkontakt)
      .toList();
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAllKontakt() {
    kontaktService.deleteAll();
  }
}
