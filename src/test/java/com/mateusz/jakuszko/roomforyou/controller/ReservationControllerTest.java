package com.mateusz.jakuszko.roomforyou.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mateusz.jakuszko.roomforyou.adapter.gson.LocalDateAdapter;
import com.mateusz.jakuszko.roomforyou.dto.ReservationDto;
import com.mateusz.jakuszko.roomforyou.facade.ReservationDbFacade;
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

import java.time.LocalDate;
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
public class ReservationControllerTest {
    @Autowired
    LocalDateAdapter localDateAdapter;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @MockBean
    private ReservationDbFacade reservationDbFacade;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    private ReservationDto createReservation(Long id) {
        return ReservationDto.builder()
                .id(id)
                .endDate(LocalDate.of(2020, 5, 6))
                .startDate(LocalDate.of(2020, 4, 5))
                .build();
    }

    @Test
    public void whenGetReservationShouldReturnCorrectJson() throws Exception {
        //Given
        ReservationDto reservationDto = createReservation(1L);
        when(reservationDbFacade.getReservation(1L)).thenReturn(reservationDto);
        //When & Then
        mockMvc.perform(get("/v1/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.startDate", is("2020-04-05")))
                .andExpect(jsonPath("$.endDate", is("2020-05-06")));
    }

    @Test
    public void whenGetReservationsShouldReturnListOfAllReservationsInCorrectJson() throws Exception {
        //Given
        ReservationDto reservationDto1 = createReservation(1L);
        ReservationDto reservationDto2 = createReservation(2L);
        List<ReservationDto> reservations = new ArrayList<>();
        reservations.add(reservationDto1);
        reservations.add(reservationDto2);
        when(reservationDbFacade.getReservations()).thenReturn(reservations);
        //When & Then
        mockMvc.perform(get("/v1/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].startDate", is("2020-04-05")))
                .andExpect(jsonPath("$[0].endDate", is("2020-05-06")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].startDate", is("2020-04-05")))
                .andExpect(jsonPath("$[1].endDate", is("2020-05-06")));
    }

    @Test
    public void whenCreateReservationShouldReturnCorrectHttpStatus() throws Exception {
        //Given
        ReservationDto reservationDto = createReservation(1L);
        when(reservationDbFacade.createReservation(ArgumentMatchers.any(ReservationDto.class))).thenReturn(reservationDto);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, localDateAdapter)
                .create();
        String jsContent = gson.toJson(reservationDto);
        //When & Then
        mockMvc.perform(post("/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsContent))
                .andExpect(status().isOk());
    }

    @Test
    public void whenUpdateReservationShouldReturnCorrectJson() throws Exception {
        //Given
        ReservationDto reservationDto = createReservation(1L);
        when(reservationDbFacade.updateReservation(ArgumentMatchers.any(ReservationDto.class))).thenReturn(reservationDto);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, localDateAdapter)
                .create();
        String jsContent = gson.toJson(reservationDto);
        //When & Then
        mockMvc.perform(put("/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept("application/json")
                .content(jsContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.startDate", is("2020-04-05")))
                .andExpect(jsonPath("$.endDate", is("2020-05-06")));
    }

    @Test
    public void whenDeleteReservationShouldReturnCorrectHttpStatus() throws Exception {
        //When & Then
        mockMvc.perform(delete("/v1/reservations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")).andExpect(status().isOk());
    }
}

