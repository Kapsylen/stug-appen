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
import stugapi.application.domain.model.Faktura;
import stugapi.application.domain.model.FakturaEnhet;
import stugapi.application.service.FakturaService;
import stugapi.infrastructure.entities.enums.FakturaStatus;
import stugapi.presentation.dto.FakturaDto;
import stugapi.presentation.dto.FakturaEnhetDto;

import java.time.Instant;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@WebMvcTest(FakturaController.class)
class FakturaControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockitoBean
  private FakturaService fakturaService;

  private ObjectMapper mapper;


  @BeforeEach
  void setUp() {
    mapper = new ObjectMapper()
      .registerModule(new JavaTimeModule());
  }


  @Test
  void whenPostFaktura_thenCreateFaktura() throws Exception {

    Instant issueDate = Instant.now();
    Instant duedate = Instant.now().plus(Period.ofDays(30));

    FakturaDto input = FakturaDto.builder()
      .invoiceNumber("Test invoice number")
      .clientName("Test client name")
      .issueDate(issueDate)
      .dueDate(duedate)
      .items(List.of(
        FakturaEnhetDto.builder()
          .description("Test description")
          .price("1000")
          .quantity("1")
          .total("1")
          .build()
      ))
      .totalAmount("1000")
      .status("paid")
      .build();

    Faktura output = Faktura.builder()
      .id(UUID.randomUUID().toString())
      .invoiceNumber(input.invoiceNumber())
      .clientName(input.clientName())
      .issueDate(input.issueDate())
      .dueDate(input.dueDate())
      .items(List.of(
        FakturaEnhet.builder()
          .id(UUID.randomUUID().toString())
          .description(input.items().getFirst().description())
          .price(input.items().getFirst().price())
          .quantity(input.items().getFirst().quantity())
          .total(input.items().getFirst().total())
          .build()
      ))
      .totalAmount(input.totalAmount())
      .status(input.status() == null ? FakturaStatus.PAID : FakturaStatus.valueOf(input.status().toUpperCase()))
      .build();

    given(fakturaService.saveFaktura(input)).willReturn(output);

    mvc.perform(MockMvcRequestBuilders
      .post("/api/v1/faktura")
      .content(mapper.writeValueAsString(input))
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.invoiceNumber").value("Test invoice number"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.clientName").value("Test client name"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.issueDate").value(issueDate.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.dueDate").value(duedate.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id").isNotEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].description").value("Test description"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].price").value("1000"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].quantity").value("1"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].total").value("1"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.totalAmount").value("1000"));

    verify(fakturaService).saveFaktura(input);
  }

  @Test
  void whenDeleteFaktura_thenDeleteFaktura() throws Exception {
    mvc.perform(MockMvcRequestBuilders
      .delete("/api/v1/faktura/12345678-1234-1234-1234-123456789012")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status()
        .isNoContent());
  }

  @Test
  void whenGetFaktura_thenReturnFaktura() throws Exception {
    var id = UUID.randomUUID().toString();
    Instant issueDate = Instant.now();
    Instant duedate = Instant.now().plus(Period.ofDays(30));
    var output = Faktura.builder()
      .invoiceNumber("Test invoice number")
      .clientName("Test client name")
      .issueDate(issueDate)
      .dueDate(duedate)
      .totalAmount("1000")
      .status(FakturaStatus.PAID)
      .build();

    given(fakturaService.find(id)).willReturn(output);

    mvc.perform(MockMvcRequestBuilders
      .get("/api/v1/faktura/{id}", id)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.invoiceNumber").value("Test invoice number"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.clientName").value("Test client name"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.issueDate").value(issueDate.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.dueDate").value(duedate.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.totalAmount").value("1000"));

    verify(fakturaService).find(id);
  }

  @Test
  public void whenGetAllFaktura_thenReturnAllFaktura() throws Exception {
    Instant issueDate = Instant.now();
    Instant duedate = Instant.now().plus(Period.ofDays(30));
    Instant issueDate2 = Instant.now().plus(Period.ofDays(60));
    Instant dueDate2 = Instant.now().plus(Period.ofDays(90));
    var faktura1 = Faktura.builder()
      .invoiceNumber("Test invoice number")
      .clientName("Test client name")
      .issueDate(issueDate)
      .dueDate(duedate)
      .totalAmount("1000")
      .status(FakturaStatus.PAID)
      .build();

    var faktura2 = Faktura.builder()
      .invoiceNumber("Test invoice number2")
      .clientName("Test client name")
      .issueDate(issueDate2)
      .dueDate(dueDate2)
      .totalAmount("2000")
      .status(FakturaStatus.SENT)
      .build();

    var fakturor = List.of(faktura1, faktura2);

    given(fakturaService.findAll()).willReturn(fakturor);

    mvc.perform(MockMvcRequestBuilders
      .get("/api/v1/faktura")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].invoiceNumber").value("Test invoice number"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientName").value("Test client name"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].issueDate").value(issueDate.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].dueDate").value(duedate.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].invoiceNumber").value("Test invoice number2"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].clientName").value("Test client name"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].issueDate").value(issueDate2.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].dueDate").value(dueDate2.toString()));

    verify(fakturaService).findAll();
  }

  @Test
  public void whenDeleteAllFaktura_thenNoContentIsReturned() throws Exception {
    var id = UUID.randomUUID().toString();
    mvc.perform(MockMvcRequestBuilders
      .delete("/api/v1/faktura/{id}", id)
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status()
        .isNoContent());
  }

  @Test
  public void whenPutFaktura_thenUpdateFaktura() throws Exception {
    Instant issueDate = Instant.now();
    Instant duedate = Instant.now().plus(Period.ofDays(30));
    var id = UUID.randomUUID().toString();
    var inputUpdateFaktura = FakturaDto.builder()
      .invoiceNumber("Test invoice number")
      .clientName("Test client name")
      .issueDate(issueDate)
      .dueDate(duedate)
      .totalAmount("1000")
      .status("SENT")
      .build();

    var outputUpdatedFakura = Faktura.builder()
      .id(UUID.randomUUID().toString())
      .invoiceNumber(inputUpdateFaktura.invoiceNumber())
      .clientName(inputUpdateFaktura.clientName())
      .issueDate(inputUpdateFaktura.issueDate())
      .dueDate(inputUpdateFaktura.dueDate())
      .totalAmount(inputUpdateFaktura.totalAmount())
      .status(FakturaStatus.SENT)
      .build();

    given(fakturaService.update(id, inputUpdateFaktura)).willReturn(outputUpdatedFakura);

    mvc.perform(MockMvcRequestBuilders
      .put("/api/v1/faktura/{id}", id)
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(inputUpdateFaktura)))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$.invoiceNumber").value("Test invoice number"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.clientName").value("Test client name"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.issueDate").value(issueDate.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.dueDate").value(duedate.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.totalAmount").value("1000"));

    verify(fakturaService).update(id, inputUpdateFaktura);
  }
}
