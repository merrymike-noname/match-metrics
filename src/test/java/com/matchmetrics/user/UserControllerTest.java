package com.matchmetrics.user;

import com.matchmetrics.controller.controller_advice.GlobalExceptionHandler;
import com.matchmetrics.entity.Team;
import com.matchmetrics.security.controller.UserController;
import com.matchmetrics.security.entity.RegisterRequest;
import com.matchmetrics.security.entity.Role;
import com.matchmetrics.security.entity.UserUpdateRequest;
import com.matchmetrics.security.entity.dto.UserGetDto;
import com.matchmetrics.security.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
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
        UserGetDto user1 = new UserGetDto("John Doe", "john.doe@example.com", "Team A", Role.ROLE_USER);
        UserGetDto user2 = new UserGetDto("Jane Doe", "jane.doe@example.com", "Team B", Role.ROLE_USER);
        List<UserGetDto> expectedUsers = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(expectedUsers);

        mockMvc.perform(get("/matchmetrics/api/v0/users/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{" +
                        "\"name\":\"" + user1.getName() + "\"," +
                        "\"email\":\"" + user1.getEmail() + "\"," +
                        "\"favouriteTeam\":\"" + user1.getFavouriteTeam() + "\"" +
                        "},{" +
                        "\"name\":\"" + user2.getName() + "\"," +
                        "\"email\":\"" + user2.getEmail() + "\"," +
                        "\"favouriteTeam\":\"" + user2.getFavouriteTeam() + "\"" +
                        "}]"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        String email = "john.doe@example.com";
        UserGetDto expectedUser = new UserGetDto("John Doe", email, "Team A", Role.ROLE_USER);

        when(userService.getUserByEmail(email)).thenReturn(expectedUser);

        mockMvc.perform(get("/matchmetrics/api/v0/users/{email}", email)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"name\":\"" + expectedUser.getName() + "\"," +
                        "\"email\":\"" + expectedUser.getEmail() + "\"," +
                        "\"favouriteTeam\":\"" + expectedUser.getFavouriteTeam() + "\"" +
                        "}"));

        verify(userService, times(1)).getUserByEmail(email);
    }

    @Test
    public void testUpdateUser() throws Exception {
        String email = "john.doe@example.com";
        RegisterRequest request = new RegisterRequest();
        UserGetDto updatedUser = new UserGetDto("John Doe", email, "Team A", Role.ROLE_USER);

        when(userService.updateUser(eq(email), any(UserUpdateRequest.class), any(BindingResult.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/matchmetrics/api/v0/users/update/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"John Doe\", \"favouriteTeam\": \"Team A\" }")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"name\":\"" + updatedUser.getName() + "\"," +
                        "\"email\":\"" + updatedUser.getEmail() + "\"," +
                        "\"favouriteTeam\":\"" + updatedUser.getFavouriteTeam() + "\"" +
                        "}"));

        verify(userService, times(1)).updateUser(eq(email), any(UserUpdateRequest.class), any(BindingResult.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testMakeUserAdmin() throws Exception {
        String email = "john.doe@example.com";
        UserGetDto adminUser = new UserGetDto("John Doe", email, "Team A", Role.ROLE_USER);

        when(userService.makeUserAdmin(email)).thenReturn(adminUser);

        mockMvc.perform(put("/matchmetrics/api/v0/users/admin/{email}", email)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"name\":\"" + adminUser.getName() + "\"," +
                        "\"email\":\"" + adminUser.getEmail() + "\"," +
                        "\"favouriteTeam\":\"" + adminUser.getFavouriteTeam() + "\"" +
                        "}"));

        verify(userService, times(1)).makeUserAdmin(email);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteUser() throws Exception {
        String email = "john.doe@example.com";

        doNothing().when(userService).deleteUser(email);

        mockMvc.perform(delete("/matchmetrics/api/v0/users/delete/{email}", email)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(email);
    }

    @Test
    public void testGetFavouriteTeam() throws Exception {
        String email = "john.doe@example.com";
        Team favouriteTeam = new Team("Team A", "Country A", 1500.0f);

        when(userService.getFavouriteTeam(email)).thenReturn(favouriteTeam);

        mockMvc.perform(get("/matchmetrics/api/v0/users/favouriteTeam/{email}", email)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"name\":\"" + favouriteTeam.getName() + "\"," +
                        "\"country\":\"" + favouriteTeam.getCountry() + "\"," +
                        "\"elo\":" + favouriteTeam.getElo() +
                        "}"));

        verify(userService, times(1)).getFavouriteTeam(email);
    }

    @Test
    public void testGetUserName() throws Exception {
        String email = "john.doe@example.com";
        String expectedName = "John Doe";

        when(userService.getUserName(email)).thenReturn(expectedName);

        mockMvc.perform(get("/matchmetrics/api/v0/users/name/{email}", email)
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedName));

        verify(userService, times(1)).getUserName(email);
    }
}

