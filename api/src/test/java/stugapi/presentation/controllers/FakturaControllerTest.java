package stugapi.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import stugapi.application.domain.model.Faktura;
import stugapi.application.service.FakturaService;
import stugapi.infrastructure.entities.FakturaStatus;
import stugapi.presentation.dto.FakturaDto;

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

  @Test
  void whenPostFaktura_thenCreateFaktura() throws Exception {

    ObjectMapper mapper = new ObjectMapper();

    FakturaDto input = FakturaDto.builder()
      .invoiceNumber("Test invoice number")
      .clientName("Test client name")
      .issueDate("2021-01-01")
      .dueDate("2021-01-31")
      .totalAmount("1000")
      .status("Paid")
      .build();

    Faktura output = Faktura.builder()
      .id(UUID.randomUUID().toString())
      .invoiceNumber("Test invoice number")
      .clientName("Test client name")
      .issueDate("2021-01-01")
      .dueDate("2021-01-31")
      .totalAmount("1000")
      .status(FakturaStatus.PAID)
      .build();

    given(fakturaService.saveFaktura(input)).willReturn(output);

    mvc.perform(MockMvcRequestBuilders
      .post("/api/v1/faktura")
      .content(mapper.writeValueAsBytes(input))
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.invoiceNumber").value("Test invoice number"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.clientName").value("Test client name"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.issueDate").value("2021-01-01"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.dueDate").value("2021-01-31"))
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
    var output = Faktura.builder()
      .invoiceNumber("Test invoice number")
      .clientName("Test client name")
      .issueDate("2021-01-01")
      .dueDate("2021-01-31")
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
      .andExpect(MockMvcResultMatchers.jsonPath("$.issueDate").value("2021-01-01"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.dueDate").value("2021-01-31"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.totalAmount").value("1000"));

    verify(fakturaService).find(id);
  }

  @Test
  public void whenGetAllFaktura_thenReturnAllFaktura() throws Exception {
    var faktura1 = Faktura.builder()
      .invoiceNumber("Test invoice number")
      .clientName("Test client name")
      .issueDate("2021-01-01")
      .dueDate("2021-01-31")
      .totalAmount("1000")
      .status(FakturaStatus.PAID)
      .build();

    var faktura2 = Faktura.builder()
      .invoiceNumber("Test invoice number2")
      .clientName("Test client name")
      .issueDate("2021-02-01")
      .dueDate("2021-02-31")
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
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].issueDate").value("2021-01-01"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].dueDate").value("2021-01-31"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].invoiceNumber").value("Test invoice number2"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].clientName").value("Test client name"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].issueDate").value("2021-02-01"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].dueDate").value("2021-02-31"));

    verify(fakturaService).findAll();
  }

  @Test
  public void whenDeleteAllFaktura_thenDeleteAllFaktura() throws Exception {
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
    ObjectMapper mapper = new ObjectMapper();
    var id = UUID.randomUUID().toString();
    var inputUpdateFaktura = FakturaDto.builder()
      .invoiceNumber("Test invoice number")
      .clientName("Test client name")
      .issueDate("2021-01-01")
      .dueDate("2021-01-31")
      .totalAmount("1000")
      .status("Sent")
      .build();

    var outputUpdatedFakura = Faktura.builder()
      .id(UUID.randomUUID().toString())
      .invoiceNumber("Test invoice number")
      .clientName("Test client name")
      .issueDate("2021-01-01")
      .dueDate("2021-01-31")
      .totalAmount("1000")
      .status(FakturaStatus.SENT)
      .build();

    given(fakturaService.update(id, inputUpdateFaktura)).willReturn(outputUpdatedFakura);

    mvc.perform(MockMvcRequestBuilders
      .put("/api/v1/faktura/{id}", id)
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsBytes(inputUpdateFaktura)))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$.invoiceNumber").value("Test invoice number"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.clientName").value("Test client name"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.issueDate").value("2021-01-01"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.dueDate").value("2021-01-31"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.totalAmount").value("1000"));

    verify(fakturaService).update(id, inputUpdateFaktura);
  }
}
