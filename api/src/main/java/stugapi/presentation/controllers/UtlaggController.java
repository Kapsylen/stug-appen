package stugapi.presentation.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import stugapi.application.service.UtlaggService;
import stugapi.presentation.dto.UtlaggDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/utlagg")
@AllArgsConstructor
@Log4j2
public class UtlaggController {

  private final UtlaggService utlaggService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UtlaggDto saveUtlagg(@RequestBody UtlaggDto utlaggDto) {
    log.info("Create utlägg: {}", utlaggDto);
    return UtlaggDto.utlaggDtoBuilder(utlaggService.save(utlaggDto))
      .build();
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
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
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAllUtlagg() {
      utlaggService.deleteAll();
  }
}
