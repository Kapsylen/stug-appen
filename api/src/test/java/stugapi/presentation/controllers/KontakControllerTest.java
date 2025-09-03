package stugapi.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import stugapi.application.domain.model.Kontakt;
import stugapi.application.service.KontaktService;
import stugapi.config.SecurityConfig;
import stugapi.presentation.dto.KontaktDto;
import stugapi.presentation.dto.KontaktDto.KontaktDtoBuilder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(KontaktController.class)
@Import(SecurityConfig.class)
public class KontakControllerTest {

  public static final String BASE_URL = "/api/v1/kontakt";
  @Autowired
  MockMvc mvc;

  @MockitoBean
  private KontaktService kontaktService;

  private ObjectMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ObjectMapper()
      .registerModule(new JavaTimeModule());
  }


  @Test
  @WithMockUser(roles = "base_user")
  public void whenPostKontak_thenCreateKontak() throws Exception {

    KontaktDto inputKontakt = createKontaktBuilder()
      .build();

    Kontakt kontaktOutput = Kontakt.fromKontaktDto(inputKontakt).build();

    given(kontaktService.saveKontakt(inputKontakt)).willReturn(kontaktOutput);

    mvc.perform(MockMvcRequestBuilders
      .post(BASE_URL)
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
  @WithMockUser(roles = "admin_user")
  public void whenDeleteKontakt_thenDeleteKontak() throws Exception {
    mvc.perform(MockMvcRequestBuilders
      .delete(BASE_URL + "/" + UUID.randomUUID())
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status()
        .isNoContent());
  }

  @Test
  @WithMockUser(roles = "base_user")
  public void whenDeleteKontakt_thenForbidden() throws Exception {
    mvc.perform(MockMvcRequestBuilders
        .delete(BASE_URL + "/" + UUID.randomUUID())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status()
        .isForbidden());
  }

  @Test
  @WithMockUser(roles = "base_user")
  public void whenGetKontakt_thenReturnKontakt() throws Exception {
    var id = UUID.randomUUID().toString();
    Kontakt outputKontakt = Kontakt.fromKontaktDto(createKontaktBuilder().build()).build();

    given(kontaktService.find(id)).willReturn(outputKontakt);

    mvc.perform(MockMvcRequestBuilders
      .get(BASE_URL + "/{id}", id)
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
  @WithMockUser(roles = "admin_user")
  public void whenDeleteKontakts_thenNoContentIsReturned() throws Exception {
    mvc.perform(MockMvcRequestBuilders
      .delete(BASE_URL)
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status()
        .isNoContent());
  }

  @Test
  @WithMockUser(roles = "base_user")
  public void whenDeleteKontakts_thenForbidden() throws Exception {
    mvc.perform(MockMvcRequestBuilders
        .delete(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status()
        .isForbidden());
  }

  @Test
  @WithMockUser(roles = "admin_user")
  public void whenUpdateKontakt_thenKontaktIsUpdated() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    var id = UUID.randomUUID().toString();

    KontaktDto inputUpdateKontakt = createKontaktBuilder().build();

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
      .put(BASE_URL + "/{id}", id)
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
  @WithMockUser(roles = "base_user")
  public void whenUpdateKontakt_thenForbidden() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    var id = UUID.randomUUID().toString();

    KontaktDto inputUpdateKontakt = createKontaktBuilder().build();

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
        .put(BASE_URL + "/{id}", id)
        .content(mapper.writeValueAsBytes(inputUpdateKontakt))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status()
        .isForbidden());
  }


  @Test
  @WithMockUser(roles = "base_user")
  public void whenGetAllKontakt_thenReturnAllKontakt() throws Exception {

    Kontakt kontakt1 = Kontakt.fromKontaktDto(createKontaktBuilder().build()).build();

    Kontakt kontakt2 = Kontakt.fromKontaktDto(createKontaktBuilder()
      .name("Maria Johansson")
      .company("Modern Plumbing AB")
      .category("Plumber")
      .phone("+46-70-234-5678")
      .email("maria@modernplumbing.se")
      .address("Rörgatan 8, 12346 Stockholm")
      .notes("Master Plumber, Heat Pump Specialist. Green Plumbing Certified. Rate: 625 SEK/h. Rating: 4.9/5")
      .status(Kontakt.KontaktStatus.ACTIVE.name())
      .build()).build();

    given(kontaktService.findAll()).willReturn(List.of(kontakt1, kontakt2));

    mvc.perform(MockMvcRequestBuilders
      .get(BASE_URL)
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

  // Validation fields, expect 400 Bad Request:
  /**
   * Name
   * Company
   * Phone
   * Email
   * Status
   * */

  @ParameterizedTest(name = "[{index} {0}]")
  @MethodSource("invalidNameCases")
  @WithMockUser(roles = "base_user")
  public void whenCreate_nameIsNullOrBlankOrInvalid_thenReturn400(String name, String expectedError) throws Exception {

    // Given

    KontaktDto invalidInput = createKontaktBuilder()
      .name(name)
      .build();

    // When & Then
    mvc.perform(post(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(invalidInput)))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status").value(400))
      .andExpect(jsonPath("$.error").value("Bad Request"))
      .andExpect(jsonPath("$.message").value("Validation failed"))
      .andExpect(jsonPath("$.details").value(expectedError))
      .andExpect(jsonPath("$.timestamp").exists());

  }
  private static Stream<Arguments> invalidNameCases() {
    return Stream.of(
      Arguments.of(
        null,
        "name: Name is required"
      ),
      Arguments.of(
        " ",
        "name: Name must be between 2 and 100 characters"
      ),
      Arguments.of(
        "a",
        "name: Name must be between 2 and 100 characters"
      ),
      Arguments.of(
        "a".repeat(101),
        "name: Name must be between 2 and 100 characters"
      )
    );
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidCompanyCases")
  @WithMockUser(roles = "base_user")
  public void whenCreate_companyIsNullOrBlankOrInvalid_thenReturn400(String company, String expectedError) throws Exception {
    // Given

    KontaktDto invalidInput = createKontaktBuilder()
      .company(company)
      .build();

    // When & Then
    mvc.perform(post(BASE_URL)
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(invalidInput)))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status").value(400))
      .andExpect(jsonPath("$.error").value("Bad Request"))
      .andExpect(jsonPath("$.message").value("Validation failed"))
      .andExpect(jsonPath("$.details").value(expectedError))
      .andExpect(jsonPath("$.timestamp").exists());
  }

  private static Stream<Arguments> invalidCompanyCases() {
    return Stream.of(
      Arguments.of(
        null,
        "company: Company is required"
      ),
      Arguments.of("",
        "company: Company is required"
      ),
      Arguments.of(" ",
        "company: Company is required")
    );
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidPhoneCases")
  @WithMockUser(roles = "base_user")
  public void whenCreate_phoneIsNullOrBlankOrInvalid_thenReturn400(String phone, String expectedError) throws Exception {
    // Given

    KontaktDto invalidContactInput = createKontaktBuilder()
      .phone(phone)
      .build();

    // When & Then

    mvc.perform(post(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(invalidContactInput)))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status").value(400))
      .andExpect(jsonPath("$.error").value("Bad Request"))
      .andExpect(jsonPath("$.message").value("Validation failed"))
      .andExpect(jsonPath("$.details").value(expectedError))
      .andExpect(jsonPath("$.timestamp").exists());
  }

  private static Stream<Arguments> invalidPhoneCases() {
    return Stream.of(
      Arguments.of(
        null,
        "phone: Phone number is required"
      ),
      Arguments.of(
        "",
        "phone: Phone number is required"
      ),
      Arguments.of(
        "abc123",
        "phone: Phone number can only contain numbers, spaces, hyphens, parentheses, and optional '+' prefix"
      ),
      Arguments.of(
        "123$456",
        "phone: Phone number can only contain numbers, spaces, hyphens, parentheses, and optional '+' prefix"
      ),
      Arguments.of(
        "++46-70-123-4567", // double plus
        "phone: Phone number can only contain numbers, spaces, hyphens, parentheses, and optional '+' prefix"
      ),
      Arguments.of(
        "++91122228888", // double space
        "phone: Phone number can only contain numbers, spaces, hyphens, parentheses, and optional '+' prefix"
      )
    );
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidEmailCases")
  @WithMockUser(roles = "base_user")
  public void whenCreate_emailIsNullOrBlankOrInvalid_thenReturn400(String email, String expectedError) throws Exception {
    // Given

    KontaktDto invalidInput = createKontaktBuilder()
      .email(email)
      .build();

    // When & Then

    mvc.perform(post(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(invalidInput)))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status").value(400))
      .andExpect(jsonPath("$.error").value("Bad Request"))
      .andExpect(jsonPath("$.message").value("Validation failed"))
      .andExpect(jsonPath("$.details").value(expectedError))
      .andExpect(jsonPath("$.timestamp").exists());
  }

private static Stream<Arguments> invalidEmailCases() {
    return Stream.of(
      Arguments.of(
        null,
        "email: Email is required"
      ),
      Arguments.of(
        "",
        "email: Email is required"
      ),
      Arguments.of("plainaddress",  // missing @ symbol
        "email: Please provide a valid email address"),
      Arguments.of("@domain.com",
        "email: Please provide a valid email address" // domain starts with dot
        ),
      Arguments.of("user@", // missing domain
        "email: Please provide a valid email address"
      ),
      Arguments.of("user@.com", // domain starts with dot
        "email: Please provide a valid email address"
      ),
      Arguments.of("user@.domain.com", // domain starts with dot
        "email: Please provide a valid email address"
      ),
      Arguments.of("user@domain..com", // consecutive dots
        "email: Please provide a valid email address"
      ),
      Arguments.of("user@domain..com", // missing domain
        "email: Please provide a valid email address"
      ),
      Arguments.of("user name@domain.com", // spaces in local part
        "email: Please provide a valid email address"
      ),
      Arguments.of("user@@domain.com", // multiple @ symbols
        "email: Please provide a valid email address"
      ),
      Arguments.of(".user@domain.com", // starts with dot
        "email: Please provide a valid email address"
      ),
      Arguments.of("user.@domain.com", // ends with dot
        "email: Please provide a valid email address"
      ),
      Arguments.of("us..er@domain.com", // onsecutive dots
        "email: Please provide a valid email address"
      ),
      Arguments.of("user@domain.com.", // ends with dot
        "email: Please provide a valid email address"
      ));
}

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidStatusCases")
  @WithMockUser(roles = "base_user")
  public void whenCreate_statusIsNullOrBlankOrInvalid_thenReturn400(String status, String expectedError) throws Exception {
    // Given

    KontaktDto invalidInput = createKontaktBuilder()
      .status(status)
      .build();

    // When & Then
    mvc.perform(post(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(invalidInput)))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status").value(400))
      .andExpect(jsonPath("$.error").value("Bad Request"))
      .andExpect(jsonPath("$.message").value("Validation failed"))
      .andExpect(jsonPath("$.details").value(expectedError))
      .andExpect(jsonPath("$.timestamp").exists());
  }

  private static Stream<Arguments> invalidStatusCases() {
    return Stream.of(
      Arguments.of(
        null,
        "status: Status is required"
      ),
      Arguments.of(
        " ",
        "status: Status must be either ACTIVE or INACTIVE"
      ),
      Arguments.of(
        "",
        "status: Status must be either ACTIVE or INACTIVE"
      ),
      Arguments.of(
        "a",
        "status: Status must be either ACTIVE or INACTIVE"
      )
    );
  }

  private static KontaktDtoBuilder createKontaktBuilder() {
    return KontaktDto.builder()
      .name("Henrik Söderberg")
      .company("Söderberg Roofing")
      .category("Roofer")
      .phone("+46-70-890-1234")
      .email("henrik@soderbergroofing.se")
      .address("akgatan 89, 12352 Stockholm")
      .notes("Master Roofer, Solar & Green Roof Specialist. Rate: 640 SEK/h. Rating: 4.8/5")
      .status("ACTIVE");
  }
}
