package stugapi.presentation.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.event.annotation.PrepareTestInstance;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import stugapi.application.domain.model.Utlagg;
import stugapi.application.service.UtlaggService;
import stugapi.presentation.dto.UtlaggDto;

import java.time.Instant;
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

  @Test
  public void whenPostUtlagg_thenCreateUtlagg() throws Exception {

    ObjectMapper mapper = new ObjectMapper();
    var outlayDate = Instant.now().toString();
    var inputUtlagg = UtlaggDto.builder()
      .title("Test title")
      .description("Test description")
      .outlayDate(outlayDate)
      .price("1000")
      .build();

    var outputUtlagg = UtlaggDto.builder()
      .id(UUID.randomUUID().toString())
      .title("Test title")
      .description("Test description")
      .outlayDate(outlayDate)
      .price("1000")
      .build();

    given(utlaggService.save(inputUtlagg)).willReturn(Utlagg.fromUtlaggDto(outputUtlagg).build());

    mvc.perform(MockMvcRequestBuilders
      .post("/api/v1/utlagg")
      .content(mapper.writeValueAsBytes(inputUtlagg))
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test title"))
      .andExpect(MockMvcResultMatchers.jsonPath("description").value("Test description"))
      .andExpect(MockMvcResultMatchers.jsonPath("outlayDate").value(outlayDate))
      .andExpect(MockMvcResultMatchers.jsonPath("price").value("1000"));

    verify(utlaggService).save(inputUtlagg);
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
    var outlayDate = Instant.now().toString();
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
      .andExpect(MockMvcResultMatchers.jsonPath("outlayDate").value(outlayDate))
      .andExpect(MockMvcResultMatchers.jsonPath("price").value("1000"));
  }


}
