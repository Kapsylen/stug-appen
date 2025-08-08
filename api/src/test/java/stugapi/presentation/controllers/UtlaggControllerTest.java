package stugapi.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import stugapi.application.domain.model.Utlagg;
import stugapi.application.service.UtlaggService;
import stugapi.presentation.dto.UtlaggDto;

import java.time.Instant;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UtlaggController.class)
public class UtlaggControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockitoBean
  private UtlaggService utlaggService;

  private ObjectMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ObjectMapper()
      .registerModule(new JavaTimeModule());
  }

  @Test
  public void whenPostUtlagg_thenCreateUtlagg() throws Exception {

    var outlayDate = Instant.now();
    var inputUtlagg = UtlaggDto.builder()
      .title("Test title")
      .description("Test description")
      .outlayDate(outlayDate)
      .price("1000")
      .build();

    var outputUtlagg = Utlagg.builder()
      .id(UUID.randomUUID().toString())
      .title("Test title")
      .description("Test description")
      .outlayDate(outlayDate)
      .price("1000")
      .build();

    given(utlaggService.saveUtlagg(inputUtlagg)).willReturn(outputUtlagg);

    mvc.perform(MockMvcRequestBuilders
      .post("/api/v1/utlagg")
      .content(mapper.writeValueAsString(inputUtlagg))
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test title"))
      .andExpect(MockMvcResultMatchers.jsonPath("description").value("Test description"))
      .andExpect(MockMvcResultMatchers.jsonPath("outlayDate").value(outlayDate.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("price").value("1000"));

    verify(utlaggService).saveUtlagg(inputUtlagg);
  }

  @Test
  public void whenDeleteUtlagg_thenDeleteUtlagg() throws Exception {
    mvc.perform(MockMvcRequestBuilders
      .delete("/api/v1/utlagg/12345678-1234-1234-1234-123456789012")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status()
        .isNoContent());
  }

  @Test
  public void whenGetUtlagg_thenReturnUtlagg() throws Exception {
    var outlayDate = Instant.now();
    var id = UUID.randomUUID().toString();
    var inputUtlagg = UtlaggDto.builder()
      .title("Test title")
      .description("Test description")
      .outlayDate(outlayDate)
      .price("1000")
      .build();

    given(utlaggService.find(any())).willReturn(Utlagg.fromUtlaggDto(inputUtlagg).build());

    mvc.perform(MockMvcRequestBuilders
      .get("/api/v1/utlagg/{id}", id)
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test title"))
      .andExpect(MockMvcResultMatchers.jsonPath("description").value("Test description"))
      .andExpect(MockMvcResultMatchers.jsonPath("outlayDate").value(outlayDate.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("price").value("1000"));
  }

  @Test
  public void whenGetAllUtlagg_thenReturnAllUtlagg() throws Exception {
    var outlayDate = Instant.now();
    var utlagg1 = Utlagg.builder()
      .title("Test title")
      .description("Test description")
      .outlayDate(outlayDate)
      .price("1000")
      .build();

    var outlayDate2 = Instant.now().plus(Period.ofDays(10));
    var utlagg2 = Utlagg.builder()
      .title("Test title 2")
      .description("Test description 2")
      .outlayDate(outlayDate2)
      .price("2000")
      .build();

    var utlagg = List.of(utlagg1, utlagg2);

    given(utlaggService.findAll()).willReturn(utlagg);

    mvc.perform(MockMvcRequestBuilders
      .get("/api/v1/utlagg")
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Test title"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Test description"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].outlayDate").value(outlayDate.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value("1000"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Test title 2"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("Test description 2"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].outlayDate").value(outlayDate2.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].price").value("2000"));

    verify(utlaggService).findAll();
  }

  @Test
  public void whenDeleteAllUtlagg_thenNoContentIsReturned() throws Exception {
    mvc.perform(MockMvcRequestBuilders
      .delete("/api/v1/utlagg")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status()
        .isNoContent());

    verify(utlaggService).deleteAll();
  }

  @Test
  public void whenPutUtlagg_thenUpdateUtlagg() throws Exception {
    var outlayDate = Instant.now();
    var id = UUID.randomUUID().toString();
    var inputUtlagg = UtlaggDto.builder()
      .title("Test title")
      .description("Test description")
      .outlayDate(outlayDate)
      .price("2000")
      .build();

    var outputUtlagg = Utlagg.builder()
      .id(id)
      .title("Test title")
      .description("Test description")
      .outlayDate(outlayDate)
      .price("2000")
      .build();

    given(utlaggService.update(id, inputUtlagg)).willReturn(outputUtlagg);

    mvc.perform(MockMvcRequestBuilders
      .put("/api/v1/utlagg/{id}", id)
        .content(mapper.writeValueAsBytes(inputUtlagg))
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test title"))
      .andExpect(MockMvcResultMatchers.jsonPath("description").value("Test description"))
      .andExpect(MockMvcResultMatchers.jsonPath("outlayDate").value(outlayDate.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("price").value("2000"));

    verify(utlaggService).update(id, inputUtlagg);
  }
}
