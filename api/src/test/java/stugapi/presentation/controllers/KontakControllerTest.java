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
import stugapi.application.domain.model.Kontakt;
import stugapi.application.service.KontaktService;
import stugapi.presentation.dto.KontaktDto;

import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@WebMvcTest(KontaktController.class)
public class KontakControllerTest {

  @Autowired
  MockMvc mvc;

  @MockitoBean
  private KontaktService kontaktService;

  @Test
  public void whenPostKontak_thenCreateKontak() throws Exception {
    ObjectMapper mapper = new ObjectMapper();

    KontaktDto inputKontakt = KontaktDto.builder()
      .name("Henrik Söderberg")
      .category("Söderberg Roofing")
      .category("Roofer")
      .phone("+46-70-890-1234")
      .email("henrik@soderbergroofing.se")
      .address("akgatan 89, 12352 Stockholm")
      .notes("Master Roofer, Solar & Green Roof Specialist. Rate: 640 SEK/h. Rating: 4.8/5")
      .status("ACTIVE")
      .build();

    Kontakt kontaktOutput = Kontakt.builder()
      .name(inputKontakt.name())
      .category(inputKontakt.category())
      .phone(inputKontakt.phone())
      .email(inputKontakt.email())
      .address(inputKontakt.address())
      .notes(inputKontakt.notes())
      .status(Kontakt.KontaktStatus.fromString(inputKontakt.status()))
      .build();

    given(kontaktService.saveKontakt(inputKontakt)).willReturn(kontaktOutput);

    mvc.perform(MockMvcRequestBuilders
      .post("/api/v1/kontakt")
      .accept(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsBytes(inputKontakt))
      .contentType(MediaType.APPLICATION_JSON)
    )
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(inputKontakt.name()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(inputKontakt.category()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(inputKontakt.phone()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(inputKontakt.email()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.address").value(inputKontakt.address()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.notes").value(inputKontakt.notes()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(inputKontakt.status()));

    verify(kontaktService).saveKontakt(inputKontakt);
  }

  @Test
  public void whenDeleteKontak_thenDeleteKontak() throws Exception {
    mvc.perform(MockMvcRequestBuilders
      .delete("/api/v1/kontakt/12345678-1234-1234-1234-123456789012")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status()
        .isNoContent());
  }

  @Test
  public void whenGetKontakt_thenReturnKontakt() throws Exception {
    var id = UUID.randomUUID().toString();
    var outputKontakt = Kontakt.builder()
      .name("Henrik Söderberg")
      .company("Söderberg Roofing")
      .category("Roofer")
      .phone("+46-70-890-1234")
      .email("henrik@soderbergroofing.se")
      .address("akgatan 89, 12352 Stockholm")
      .notes("Master Roofer, Solar & Green Roof Specialist. Rate: 640 SEK/h. Rating: 4.8/5")
      .status(Kontakt.KontaktStatus.ACTIVE)
      .build();


    given(kontaktService.find(id)).willReturn(outputKontakt);

    mvc.perform(MockMvcRequestBuilders
      .get("/api/v1/kontakt/{id}", id)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(outputKontakt.name()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(outputKontakt.category()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(outputKontakt.phone()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(outputKontakt.email()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.address").value(outputKontakt.address()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.notes").value(outputKontakt.notes()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(outputKontakt.status().toString()));

    verify(kontaktService).find(id);
  }

  @Test
  public void whenDeleteKontakt_thenNoContentIsReturned() throws Exception {
    var id = UUID.randomUUID().toString();
    mvc.perform(MockMvcRequestBuilders
      .delete("/api/v1/kontakt/{id}", id)
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status()
        .isNoContent());
  }

  @Test
  public void whenUpdateKontakt_thenKontaktIsUpdated() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    var id = UUID.randomUUID().toString();

    KontaktDto inputUpdateKontakt = KontaktDto.builder()
      .name("Henrik Söderberg")
      .company("Söderberg Roofing")
      .category("Roofer")
      .phone("+46-70-890-1234")
      .email("henrik@soderbergroofing.se")
      .address("akgatan 89, 12352 Stockholm")
      .notes("Master Roofer, Solar & Green Roof Specialist. Rate: 640 SEK/h. Rating: 4.8/5")
      .status("ACTIVE")
      .build();

    Kontakt kontaktUpdatedOutput = Kontakt.builder()
      .name(inputUpdateKontakt.name())
      .category(inputUpdateKontakt.category())
      .phone(inputUpdateKontakt.phone())
      .email(inputUpdateKontakt.email())
      .address(inputUpdateKontakt.address())
      .notes(inputUpdateKontakt.notes())
      .status(Kontakt.KontaktStatus.fromString(inputUpdateKontakt.status()))
      .build();

    given(kontaktService.update(id, inputUpdateKontakt)).willReturn(kontaktUpdatedOutput);

    mvc.perform(MockMvcRequestBuilders
      .put("/api/v1/kontakt/{id}", id)
      .content(mapper.writeValueAsBytes(inputUpdateKontakt))
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(inputUpdateKontakt.name()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(inputUpdateKontakt.category()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(inputUpdateKontakt.phone()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(inputUpdateKontakt.email()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.address").value(inputUpdateKontakt.address()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.notes").value(inputUpdateKontakt.notes()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(inputUpdateKontakt.status()));

    verify(kontaktService).update(id, inputUpdateKontakt);
  }


  @Test
  public void whenGetAllKontakt_thenReturnAllKontakt() throws Exception {

    Kontakt kontakt1 = Kontakt.builder()
      .name("Henrik Söderberg")
      .company("Söderberg Roofing")
      .category("Roofer")
      .phone("+46-70-890-1234")
      .email("henrik@soderbergroofing.se")
      .address("akgatan 89, 12352 Stockholm")
      .notes("Master Roofer, Solar & Green Roof Specialist. Rate: 640 SEK/h. Rating: 4.8/5")
      .status(Kontakt.KontaktStatus.ACTIVE)
      .build();

    Kontakt kontakt2 = Kontakt.builder()
      .name("Maria Johansson")
      .company("Modern Plumbing AB")
      .category("Plumber")
      .phone("+46-70-234-5678")
      .email("maria@modernplumbing.se")
      .address("Rörgatan 8, 12346 Stockholm")
      .notes("Master Plumber, Heat Pump Specialist. Green Plumbing Certified. Rate: 625 SEK/h. Rating: 4.9/5")
      .status(Kontakt.KontaktStatus.ACTIVE)
      .build();

    given(kontaktService.findAll()).willReturn(List.of(kontakt1, kontakt2));

    mvc.perform(MockMvcRequestBuilders
      .get("/api/v1/kontakt")
      .contentType(MediaType.APPLICATION_JSON)
    )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(kontakt1.name()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].company").value(kontakt1.company()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].category").value(kontakt1.category()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].phone").value(kontakt1.phone()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(kontakt1.email()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].address").value(kontakt1.address()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(kontakt2.name()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].company").value(kontakt2.company()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].category").value(kontakt2.category()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].phone").value(kontakt2.phone()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value(kontakt2.email()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].address").value(kontakt2.address()));

    verify(kontaktService).findAll();
  }
}
