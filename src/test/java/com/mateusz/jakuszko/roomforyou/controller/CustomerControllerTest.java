package com.mateusz.jakuszko.roomforyou.controller;

import com.google.gson.Gson;
import com.mateusz.jakuszko.roomforyou.dto.UserDto;
import com.mateusz.jakuszko.roomforyou.facade.UserDbFacade;
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
public class CustomerControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @MockBean
    private UserDbFacade userDbFacade;

    private UserDto createUserDto(Long id, String name) {
        return UserDto.builder()
                .id(id)
                .name(name)
                .surname("Jakuszko")
                .username("matanos")
                .password("abc123")
                .role("Admin")
                .email("mateusz.jakuszko@gmail.com")
                .build();
    }

    @Test
    public void getUser() throws Exception {
        //Given
        UserDto userDto = createUserDto(1L, "Mateusz");
        when(userDbFacade.getUser(1L)).thenReturn(userDto);

        //When & Then
        mockMvc.perform(get("/v1/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Mateusz")))
                .andExpect(jsonPath("$.surname", is("Jakuszko")))
                .andExpect(jsonPath("$.surname", is("Jakuszko")))
                .andExpect(jsonPath("$.role", is("Admin")))
                .andExpect(jsonPath("$.email", is("mateusz.jakuszko@gmail.com")));
    }

    @Test
    public void getUsersTest() throws Exception {
        //Given
        UserDto userDto1 = createUserDto(1L, "Mateusz");
        UserDto userDto2 = createUserDto(2L, "Stasia");
        List<UserDto> userDto = new ArrayList<>();
        userDto.add(userDto1);
        userDto.add(userDto2);
        when(userDbFacade.getUsers()).thenReturn(userDto);

        //When & Then
        mockMvc.perform(get("/v1/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Mateusz")))
                .andExpect(jsonPath("$[0].surname", is("Jakuszko")))
                .andExpect(jsonPath("$[0].username", is("matanos")))
                .andExpect(jsonPath("$[0].role", is("Admin")))
                .andExpect(jsonPath("$[0].email", is("mateusz.jakuszko@gmail.com")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Stasia")))
                .andExpect(jsonPath("$[1].surname", is("Jakuszko")))
                .andExpect(jsonPath("$[1].username", is("matanos")))
                .andExpect(jsonPath("$[1].role", is("Admin")))
                .andExpect(jsonPath("$[1].email", is("mateusz.jakuszko@gmail.com")));
    }

    @Test
    public void createTest() throws Exception {
        //Given
        UserDto userDto = createUserDto(1L, "Mateusz");
        when(userDbFacade.createUser(userDto)).thenReturn(userDto);

        Gson gson = new Gson();
        String jsContent = gson.toJson(userDto);

        //When & Then
        mockMvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsContent))
                .andExpect(status().isOk());
    }

    @Test
    public void updateTest() throws Exception {
        //Given
        UserDto userDto = createUserDto(1L, "Mateusz");
        when(userDbFacade.updateUser(ArgumentMatchers.any(UserDto.class))).thenReturn(userDto);

        Gson gson = new Gson();
        String jsContent = gson.toJson(userDto);

        //When & Then
        mockMvc.perform(put("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept("application/json")
                .content(jsContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Mateusz")))
                .andExpect(jsonPath("$.surname", is("Jakuszko")))
                .andExpect(jsonPath("$.surname", is("Jakuszko")))
                .andExpect(jsonPath("$.role", is("Admin")))
                .andExpect(jsonPath("$.email", is("mateusz.jakuszko@gmail.com")));
    }

    @Test
    public void deleteTest() throws Exception {
        //When & Then
        mockMvc.perform(delete("/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }
}
