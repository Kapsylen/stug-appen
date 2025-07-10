package stugapi.presentation.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import stugapi.application.service.FakturaService;
import stugapi.presentation.dto.FakturaDto;

import java.util.List;

import static stugapi.presentation.dto.FakturaDto.toFakturaDtoBuilder;

@RestController
@RequestMapping("api/v1/faktura")
@AllArgsConstructor
public class FakturaController {

  private final FakturaService fakturaService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public FakturaDto createFaktura(@RequestBody FakturaDto fakturaDto) {
    return toFakturaDtoBuilder(fakturaService.saveFaktura(fakturaDto))
      .build();
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteFaktura(String id) {
    fakturaService.delete(id);
  }

  @PutMapping("/{id}")
  public FakturaDto updateFaktura(@PathVariable String id, @RequestBody FakturaDto updateFaktura) {
    return toFakturaDtoBuilder(fakturaService.update(id, updateFaktura))
      .build();
  }

  @GetMapping("/{id}")
  public FakturaDto getFaktura(@PathVariable String id) {
    return toFakturaDtoBuilder(fakturaService.find(id))
      .build();
  }

  @GetMapping
  public List<FakturaDto> getAllFaktura() {
    return fakturaService.findAll()
      .stream()
      .map(FakturaDto::toFakturaDto)
      .toList();
  }

  @DeleteMapping
  public void deleteAllFaktura() {
    fakturaService.deleteAll();
  }
}
