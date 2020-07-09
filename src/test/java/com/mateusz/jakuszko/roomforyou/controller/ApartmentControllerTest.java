package com.mateusz.jakuszko.roomforyou.controller;

import com.google.gson.Gson;
import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.facade.ApartmentDbFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApartmentControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @MockBean
    private ApartmentDbFacade apartmentDbFacade;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    private ApartmentDto createApartment(Long id) {
        return ApartmentDto.builder()
                .id(id)
                .latitude(11.0)
                .longitude(12.0)
                .city("Terespol")
                .street("WallStreet")
                .streetNumber("13")
                .apartmentNumber(14)
                .build();
    }

    @Test
    public void whenGetApartmentShouldReturnCorrectJson() throws Exception {
        //Given
        ApartmentDto apartmentDto = createApartment(1L);
        when(apartmentDbFacade.getApartment(1L)).thenReturn(apartmentDto);
        //When & Then
        mockMvc.perform(get("/v1/apartments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.city", is("Terespol")))
                .andExpect(jsonPath("$.street", is("WallStreet")))
                .andExpect(jsonPath("$.streetNumber", is("13")))
                .andExpect(jsonPath("$.apartmentNumber", is(14)))
                .andExpect(jsonPath("$.latitude", is(11.0)))
                .andExpect(jsonPath("$.longitude", is(12.0)));
    }

    @Test
    public void whenGetApartmentsShouldReturnListOfAllApartmentsInCorrectJson() throws Exception {
        //Given
        ApartmentDto apartmentDto1 = createApartment(1L);
        ApartmentDto apartmentDto2 = createApartment(2L);
        List<ApartmentDto> apartmentDtos = new ArrayList<>();
        apartmentDtos.add(apartmentDto1);
        apartmentDtos.add(apartmentDto2);
        when(apartmentDbFacade.getApartments()).thenReturn(apartmentDtos);
        //When & Then
        mockMvc.perform(get("/v1/apartments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].street", is("WallStreet")))
                .andExpect(jsonPath("$[0].city", is("Terespol")))
                .andExpect(jsonPath("$[0].streetNumber", is("13")))
                .andExpect(jsonPath("$[0].apartmentNumber", is(14)))
                .andExpect(jsonPath("$[0].latitude", is(11.0)))
                .andExpect(jsonPath("$[0].longitude", is(12.0)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].city", is("Terespol")))
                .andExpect(jsonPath("$[1].street", is("WallStreet")))
                .andExpect(jsonPath("$[1].streetNumber", is("13")))
                .andExpect(jsonPath("$[1].apartmentNumber", is(14)))
                .andExpect(jsonPath("$[1].latitude", is(11.0)))
                .andExpect(jsonPath("$[1].longitude", is(12.0)));
    }

    @Test
    public void whenCreateApartmentShouldReturnCorrectHttpStatus() throws Exception {
        //Given
        ApartmentDto apartmentDto = createApartment(1L);
        when(apartmentDbFacade.createApartment(apartmentDto)).thenReturn(apartmentDto);

        Gson gson = new Gson();
        String jsContent = gson.toJson(apartmentDto);

        //When & Then
        mockMvc.perform(post("/v1/apartments")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsContent))
                .andExpect(status().isOk());
    }

    @Test
    public void whenUpdateApartmentShouldReturnCorrectJson() throws Exception {
        //Given
        ApartmentDto apartmentDto = createApartment(1L);
        when(apartmentDbFacade.updateApartment(ArgumentMatchers.any(ApartmentDto.class))).thenReturn(apartmentDto);

        Gson gson = new Gson();
        String jsContent = gson.toJson(apartmentDto);

        //When & Then
        mockMvc.perform(put("/v1/apartments")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept("application/json")
                .content(jsContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.city", is("Terespol")))
                .andExpect(jsonPath("$.street", is("WallStreet")))
                .andExpect(jsonPath("$.streetNumber", is("13")))
                .andExpect(jsonPath("$.apartmentNumber", is(14)))
                .andExpect(jsonPath("$.latitude", is(11.0)))
                .andExpect(jsonPath("$.longitude", is(12.0)));
    }

    @Test
    public void whenDeleteApartmentShouldReturnCorrectHttpStatus() throws Exception {
        //When & Then
        mockMvc.perform(delete("/v1/apartments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }
}
