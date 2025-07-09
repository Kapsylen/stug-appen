package stugapi.presentation.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import stugapi.application.service.UtlaggService;
import stugapi.presentation.dto.UtlaggDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/utlagg")
@AllArgsConstructor
public class UtlaggController {

  private final UtlaggService utlaggService;

  @PostMapping
  public UtlaggDto saveUtlagg(@RequestBody UtlaggDto utlaggDto) {
    return UtlaggDto.utlaggDtoBuilder(utlaggService.save(utlaggDto))
      .build();
  }

  @DeleteMapping("/{id}")
  public void deleteUtlagg(@PathVariable String id) {
    utlaggService.delete(id);
  }

  @GetMapping("/{id}")
  public UtlaggDto getUtlagg(@PathVariable String id) {
    return UtlaggDto
      .utlaggDtoBuilder(utlaggService.find(id))
      .build();
  }

  @PutMapping("/{id}")
  public UtlaggDto updateUtlagg(@RequestBody UtlaggDto updateUtlagg, @PathVariable String id) {
    return UtlaggDto
      .utlaggDtoBuilder(utlaggService.update(updateUtlagg, id))
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
  public void deleteAllUtlagg() {
      utlaggService.deleteAll();
  }
}
