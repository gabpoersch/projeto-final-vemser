package br.com.dbc.devser.colabore.userService;

import br.com.dbc.devser.colabore.dto.user.UserCreateDTO;
import br.com.dbc.devser.colabore.entity.RoleEntity;
import br.com.dbc.devser.colabore.entity.UserEntity;
import br.com.dbc.devser.colabore.exception.UserColaboreException;
import br.com.dbc.devser.colabore.repository.RoleRepository;
import br.com.dbc.devser.colabore.repository.UserRepository;
import br.com.dbc.devser.colabore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@RequiredArgsConstructor
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    private Authentication authentication = Mockito.mock(Authentication.class);

    private SecurityContext securityContext = Mockito.mock(SecurityContext.class);


    @Mock
    private UserEntity userEntity;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void BeforeEach() {
        ReflectionTestUtils.setField(userService, "objectMapper", objectMapper);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("1");
    }

    /*Create user (UserService)*/
    @Test
    public void shouldThrowAnErrorWithDuplicatedEmail() {

        UserCreateDTO dto = UserCreateDTO
                .builder().name("José")
                .email("josesilva@gmail.com").build();
        UserEntity userEntity = new UserEntity();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(userEntity);
        Exception ex = assertThrows(UserColaboreException.class,
                () -> userService.create(dto));
        assertEquals("Email already exists.", ex.getMessage());
    }

    @Test
    public void shouldCreationUserWork() {

        UserCreateDTO dto = UserCreateDTO
                .builder()
                .name("José")
                .email("josesilva@gmail.com")
                .password("123")
                .profilePhoto(null).build();

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleId(1);
        roleEntity.setName("ROLE_USER");

        UserEntity user = new UserEntity();
        user.setUserId(1L);
        user.setName("exemplo");
        user.setEmail("exemplo@gmail.com");
        user.setPassword("123");
        user.setPhoto(null);
        user.setRoles(roleEntity);

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(null);
        when(roleRepository.findById(1)).thenReturn(Optional.of(roleEntity));
        when(userRepository.save(any())).thenReturn(user);


        assertDoesNotThrow(() -> userService.create(dto));

        verify(userRepository, times(1)).save(any());
        verify(roleRepository, times(1)).findById(1);
        verify(userRepository, times(1)).findByEmail(dto.getEmail());
        verify(userEntity, times(0)).setPhoto(any());

    }

    @Test(expected = UserColaboreException.class)
    public void shouldThrowUserNotFound() throws UserColaboreException {

        UserCreateDTO userCreateMock = dtoMock();
        UserEntity userMock = userMock();

        SecurityContextHolder.setContext(securityContext);

        /*Fluxo*/
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userMock));
        when(userRepository.findByEmail(any())).thenReturn(userMock);
        when(userRepository.save(any())).thenReturn(userMock);


        userService.update(userCreateMock);
    }

    public UserCreateDTO dtoMock() {
        return UserCreateDTO
                .builder()
                .name("José")
                .email("josesilva@gmail.com")
                .password("123")
                .profilePhoto(null).build();
    }

    public RoleEntity roleMock() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleId(1);
        roleEntity.setName("ROLE_USER");
        return roleEntity;
    }

    public UserEntity userMock() {
        UserEntity user = new UserEntity();
        user.setUserId(1L);
        user.setName("exemplo");
        user.setEmail("exemplo@gmail.com");
        user.setPassword("123");
        return user;
    }
}
