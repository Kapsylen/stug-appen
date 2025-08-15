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
import stugapi.presentation.dto.UtlaggDto.UtlaggDtoBuilder;

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

  public static final String BASE_URL = "/api/v1/utlagg";
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

    Instant outlayDate = Instant.now();
    UtlaggDto inputUtlagg = createUtlaggDtoBuilder()
      .outlayDate(outlayDate)
      .build();

    given(utlaggService.saveUtlagg(inputUtlagg)).willReturn(Utlagg.fromUtlaggDto(inputUtlagg).build());

    mvc.perform(MockMvcRequestBuilders
      .post(BASE_URL)
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
      .delete(BASE_URL + "/" + UUID.randomUUID())
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status()
        .isNoContent());
  }

  @Test
  public void whenGetUtlagg_thenReturnUtlagg() throws Exception {
    Instant outlayDate = Instant.now();
    String id = UUID.randomUUID().toString();
    UtlaggDto inputUtlagg = createUtlaggDtoBuilder()
      .id(id)
      .outlayDate(outlayDate)
      .build();

    given(utlaggService.find(any())).willReturn(Utlagg.fromUtlaggDto(inputUtlagg).build());

    mvc.perform(MockMvcRequestBuilders
      .get(BASE_URL + "/{id}", id)
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
    Instant outlayDate = Instant.now();
    Utlagg utlagg1 = Utlagg.fromUtlaggDto(createUtlaggDtoBuilder()
        .outlayDate(outlayDate)
      .build()).build();

    Instant outlayDate2 = Instant.now().plus(Period.ofDays(10));
    Utlagg utlagg2 =
      Utlagg.fromUtlaggDto(createUtlaggDtoBuilder()
          .title("Test title 2")
        .description("Test description 2")
        .outlayDate(outlayDate2)
        .price("2000").build())
        .build();

    List<Utlagg> utlagg = List.of(utlagg1, utlagg2);

    given(utlaggService.findAll()).willReturn(utlagg);

    mvc.perform(MockMvcRequestBuilders
      .get(BASE_URL)
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(utlagg1.title()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(utlagg1.description()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].outlayDate").value(outlayDate.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(utlagg1.price()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(utlagg2.title()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(utlagg2.description()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].outlayDate").value(outlayDate2.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].price").value(utlagg2.price()));

    verify(utlaggService).findAll();
  }

  @Test
  public void whenDeleteAllUtlagg_thenNoContentIsReturned() throws Exception {
    mvc.perform(MockMvcRequestBuilders
      .delete(BASE_URL)
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status()
        .isNoContent());

    verify(utlaggService).deleteAll();
  }

  @Test
  public void whenPutUtlagg_thenUpdateUtlagg() throws Exception {
    Instant outlayDate = Instant.now();
    String id = UUID.randomUUID().toString();
    UtlaggDto inputUtlagg = createUtlaggDtoBuilder().build();

    Utlagg outputUtlagg = Utlagg.builder()
      .id(id)
      .title("Test title")
      .description("Test description")
      .outlayDate(outlayDate)
      .price("2000")
      .build();

    given(utlaggService.update(id, inputUtlagg)).willReturn(outputUtlagg);

    mvc.perform(MockMvcRequestBuilders
      .put(BASE_URL + "/{id}", id)
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

  private static UtlaggDtoBuilder createUtlaggDtoBuilder() {
    return UtlaggDto.builder()
      .title("Test title")
      .description("Test description")
      .outlayDate(Instant.now())
      .price("1000");
  }
}
