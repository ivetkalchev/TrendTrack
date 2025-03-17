package controller;

import java.util.List;
import org.junit.jupiter.api.*;
import trendtrack.domain.fabric.*;
import static org.mockito.Mockito.*;
import trendtrack.TrendTrackApplication;
import trendtrack.business.FabricService;
import org.springframework.http.MediaType;
import trendtrack.controller.FabricsController;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.web.servlet.MockMvc;
import trendtrack.business.exception.FabricException;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import trendtrack.configuration.security.token.AccessTokenDecoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

@WithMockUser(username = "admin", roles = "ADMIN")
@WebMvcTest(FabricsController.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TrendTrackApplication.class})
class FabricsControllerTest {

    @MockBean
    private FabricService fabricService;

    @MockBean
    private AccessTokenDecoder accessTokenDecode;

    @Autowired
    private MockMvc mockMvc;

    // GET /fabrics
    @Test
    void getAllFabrics_ShouldReturnAllFabrics() throws Exception {
        //arrange
        GetAllFabricResponse response = GetAllFabricResponse.builder()
                .fabrics(List.of(new Fabric(
                        1L,
                        "Cotton",
                        "Soft cotton fabric",
                        Material.COTTON,
                        Color.WHITE,
                        10.0,
                        true,
                        true,
                        100,
                        "url")))
                .build();

        when(fabricService.getFabrics(any(GetAllFabricsRequest.class))).thenReturn(response);

        //act n assert
        mockMvc.perform(get("/fabrics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("name", "Cotton")
                        .param("page", "0")
                        .param("size", "9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fabrics[0].name").value("Cotton"))
                .andExpect(jsonPath("$.fabrics[0].price").value(10.0));

        verify(fabricService, times(1)).getFabrics(any(GetAllFabricsRequest.class));
    }

    // GET /fabrics with no matching filter
    @Test
    void getAllFabrics_ShouldReturnNoFabrics_WhenNoMatchFound() throws Exception {
        //arrange
        GetAllFabricResponse response = GetAllFabricResponse.builder()
                .fabrics(List.of())
                .build();

        when(fabricService.getFabrics(any(GetAllFabricsRequest.class))).thenReturn(response);

        //act n assert
        mockMvc.perform(get("/fabrics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("name", "NonExistent")
                        .param("page", "0")
                        .param("size", "9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fabrics").isEmpty());

        verify(fabricService, times(1)).getFabrics(any(GetAllFabricsRequest.class));
    }

    // GET /fabrics with filters
    @Test
    void getAllFabrics_ShouldReturnFilteredFabrics() throws Exception {
        //arrange
        GetAllFabricResponse response = GetAllFabricResponse.builder()
                .fabrics(List.of(new Fabric(
                        1L,
                        "Cotton",
                        "Soft cotton fabric",
                        Material.COTTON,
                        Color.WHITE,
                        10.0,
                        true,
                        true,
                        100,
                        "url")))
                .build();

        when(fabricService.getFabrics(any(GetAllFabricsRequest.class))).thenReturn(response);

        //act n assert
        mockMvc.perform(get("/fabrics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("name", "Cotton")
                        .param("material", "COTTON")
                        .param("color", "WHITE")
                        .param("minPrice", "5.0")
                        .param("maxPrice", "20.0")
                        .param("page", "0")
                        .param("size", "9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fabrics[0].name").value("Cotton"));

        verify(fabricService, times(1)).getFabrics(any(GetAllFabricsRequest.class));
    }

    // GET /fabrics with pagination
    @Test
    void getAllFabrics_ShouldHandlePagination() throws Exception {
        //arrange
        GetAllFabricResponse response = GetAllFabricResponse.builder()
                .fabrics(List.of(new Fabric(
                                1L,
                                "Cotton",
                                "Soft cotton fabric",
                                Material.COTTON,
                                Color.WHITE,
                                10.0,
                                true,
                                true,
                                100,
                                "url"),
                        new Fabric(
                                2L,
                                "Silk",
                                "Soft silk fabric",
                                Material.SILK,
                                Color.RED,
                                20.0,
                                true,
                                true,
                                200,
                                "url")))
                .build();

        when(fabricService.getFabrics(any(GetAllFabricsRequest.class))).thenReturn(response);

        //act n assert
        mockMvc.perform(get("/fabrics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fabrics[0].name").value("Cotton"))
                .andExpect(jsonPath("$.fabrics[1].name").value("Silk"));

        verify(fabricService, times(1)).getFabrics(any(GetAllFabricsRequest.class));
    }

    // GET /fabrics with invalid parameters
    @Test
    void getAllFabrics_ShouldReturnBadRequest_WhenInvalidPagination() throws Exception {
        //act n assert
        mockMvc.perform(get("/fabrics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "-1")
                        .param("size", "abc"))
                .andExpect(status().isBadRequest());

        verify(fabricService, times(0)).getFabrics(any(GetAllFabricsRequest.class));
    }

    // DELETE /fabrics/{id} - Fabric does not exist
    @Test
    void deleteFabric_ShouldReturnNotFound_WhenFabricDoesNotExist() throws Exception {
        //arrange
        Long fabricId = 1L;
        doThrow(FabricException.fabricDoesNotExist()).when(fabricService).deleteFabric(fabricId);

        //act n assert
        mockMvc.perform(delete("/fabrics/{id}", fabricId)
                        .with(csrf())
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());

        verify(fabricService, times(1)).deleteFabric(fabricId);
    }
}