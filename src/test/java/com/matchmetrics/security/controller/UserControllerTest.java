package com.matchmetrics.security.controller;

import com.matchmetrics.controller.TeamController;
import com.matchmetrics.controller.controller_advice.GlobalExceptionHandler;
import com.matchmetrics.security.entity.UserUpdateRequest;
import com.matchmetrics.security.entity.dto.UserGetDto;
import com.matchmetrics.security.entity.Role;
import com.matchmetrics.entity.Team;
import com.matchmetrics.security.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @MockBean
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        UserGetDto user1 = new UserGetDto("John Doe", "john@example.com", "Team A", Role.ROLE_USER);
        UserGetDto user2 = new UserGetDto("Jane Doe", "jane@example.com", "Team B", Role.ROLE_ADMIN);
        List<UserGetDto> expectedUsers = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(expectedUsers);

        mockMvc.perform(get("/matchmetrics/api/v0/users/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{" +
                                          "\"name\":\"" + user1.getName() + "\"," +
                                          "\"email\":\"" + user1.getEmail() + "\"," +
                                          "\"favouriteTeam\":\"" + user1.getFavouriteTeam() + "\"," +
                                          "\"role\":\"" + user1.getRole() +
                                          "\"},{" +
                                          "\"name\":\"" + user2.getName() + "\"," +
                                          "\"email\":\"" + user2.getEmail() + "\"," +
                                          "\"favouriteTeam\":\"" + user2.getFavouriteTeam() + "\"," +
                                          "\"role\":\"" + user2.getRole() +
                                          "\"}]"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        UserGetDto user = new UserGetDto("John Doe", "john@example.com", "Team A", Role.ROLE_USER);

        when(userService.getUserByEmail("john@example.com")).thenReturn(user);

        mockMvc.perform(get("/matchmetrics/api/v0/users/john@example.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                                          "\"name\":\"" + user.getName() + "\"," +
                                          "\"email\":\"" + user.getEmail() + "\"," +
                                          "\"favouriteTeam\":\"" + user.getFavouriteTeam() + "\"," +
                                          "\"role\":\"" + user.getRole() +
                                          "\"}"));

        verify(userService, times(1)).getUserByEmail("john@example.com");
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserGetDto updatedUser = new UserGetDto("John Doe", "john@example.com", "Team A", Role.ROLE_USER);

        when(userService.updateUser(eq("john@example.com"), any(UserUpdateRequest.class), any())).thenReturn(updatedUser);

        mockMvc.perform(put("/matchmetrics/api/v0/users/update/john@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe\", \"email\": \"john@example.com\", \"password\": \"newpassword\", \"favouriteTeam\": \"Team A\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                                          "\"name\":\"" + updatedUser.getName() + "\"," +
                                          "\"email\":\"" + updatedUser.getEmail() + "\"," +
                                          "\"favouriteTeam\":\"" + updatedUser.getFavouriteTeam() + "\"," +
                                          "\"role\":\"" + updatedUser.getRole() +
                                          "\"}"));

        verify(userService, times(1)).updateUser(eq("john@example.com"), any(UserUpdateRequest.class), any());
    }

    @Test
    public void testMakeUserAdmin() throws Exception {
        UserGetDto adminUser = new UserGetDto("John Doe", "john@example.com", "Team A", Role.ROLE_ADMIN);

        when(userService.makeUserAdmin("john@example.com")).thenReturn(adminUser);

        mockMvc.perform(put("/matchmetrics/api/v0/users/admin/john@example.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                                          "\"name\":\"" + adminUser.getName() + "\"," +
                                          "\"email\":\"" + adminUser.getEmail() + "\"," +
                                          "\"favouriteTeam\":\"" + adminUser.getFavouriteTeam() + "\"," +
                                          "\"role\":\"" + adminUser.getRole() +
                                          "\"}"));

        verify(userService, times(1)).makeUserAdmin("john@example.com");
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser("john@example.com");

        mockMvc.perform(delete("/matchmetrics/api/v0/users/delete/john@example.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser("john@example.com");
    }

    @Test
    public void testGetFavouriteTeam() throws Exception {
        Team team = new Team("Team A", "USA", 1200);

        when(userService.getFavouriteTeam("john@example.com")).thenReturn(team);

        mockMvc.perform(get("/matchmetrics/api/v0/users/favouriteTeam/john@example.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                                          "\"name\":\"" + team.getName() + "\"," +
                                          "\"country\":\"" + team.getCountry() + "\"," +
                                          "\"elo\":" + team.getElo() +
                                          "}"));

        verify(userService, times(1)).getFavouriteTeam("john@example.com");
    }

    @Test
    public void testGetUserName() throws Exception {
        String userName = "John Doe";

        when(userService.getUserName("john@example.com")).thenReturn(userName);

        mockMvc.perform(get("/matchmetrics/api/v0/users/name/john@example.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(userName));

        verify(userService, times(1)).getUserName("john@example.com");
    }

}
