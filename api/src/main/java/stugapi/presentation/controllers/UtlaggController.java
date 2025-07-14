package stugapi.presentation.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import stugapi.application.service.UtlaggService;
import stugapi.presentation.dto.UtlaggDto;

import java.util.List;

import static stugapi.presentation.dto.UtlaggDto.toUtlaggDtoBuilder;

@RestController
@RequestMapping("api/v1/utlagg")
@AllArgsConstructor
public class UtlaggController {

  private final UtlaggService utlaggService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UtlaggDto saveUtlagg(@RequestBody UtlaggDto utlaggDto) {
    return toUtlaggDtoBuilder(utlaggService.saveUtlagg(utlaggDto))
      .build();
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUtlagg(@PathVariable String id) {
    utlaggService.delete(id);
  }

  @GetMapping("/{id}")
  public UtlaggDto getUtlagg(@PathVariable String id) {
    return toUtlaggDtoBuilder(utlaggService.find(id))
      .build();
  }

  @PutMapping("/{id}")
  public UtlaggDto updateUtlagg(@PathVariable String id, @RequestBody UtlaggDto updateUtlagg) {
    return toUtlaggDtoBuilder(utlaggService.update(id, updateUtlagg))
      .build();
  }

  @GetMapping
  public List<UtlaggDto> getAllUtlagg() {
    return utlaggService.findAll()
      .stream()
      .map(UtlaggDto::fromUtlagg)
      .toList();
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAllUtlagg() {
    utlaggService.deleteAll();
  }
}
