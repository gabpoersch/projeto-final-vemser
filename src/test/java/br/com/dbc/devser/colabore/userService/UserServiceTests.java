package br.com.dbc.devser.colabore.userService;

import br.com.dbc.devser.colabore.dto.user.UserCreateDTO;
import br.com.dbc.devser.colabore.entity.RoleEntity;
import br.com.dbc.devser.colabore.entity.UserEntity;
import br.com.dbc.devser.colabore.exception.UserColaboreException;
import br.com.dbc.devser.colabore.repository.RoleRepository;
import br.com.dbc.devser.colabore.repository.UserRepository;
import br.com.dbc.devser.colabore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleEntity roleEntity;

    @Mock
    private UserEntity userEntity;

    @Mock
    private RoleRepository roleRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void BeforeEach() {
        ReflectionTestUtils.setField(userService, "objectMapper", objectMapper);
    }

    /*Start tests create user (UserService)*/
    @Test
    public void shouldThrowAnErrorWithDuplicatedEmail() throws UserColaboreException {
        UserCreateDTO dto = UserCreateDTO
                .builder().name("José")
                .email("josesilva@gmail.com").build();
        UserEntity userEntity = new UserEntity();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(userEntity);
        Exception ex = assertThrows(UserColaboreException.class,
                () -> userService.create(dto));
        assertEquals("Email already exists.", ex.getMessage());
    }

    /*Update*/

//    @Test
//    public void shouldThrowUserNotFound () throws UserColaboreException {
//        UserCreateDTO dto = UserCreateDTO
//                .builder().name("José")
//                .email("josesilva@gmail.com").build();
//        UserEntity userEntity = new UserEntity();
//
//        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(userEntity));
//
//        Exception ex = assertThrows(UserColaboreException.class,
//                () -> userService.update(dto));
//        assertEquals("User not found.", ex.getMessage());
//    }
//

//    @Test
//    public void shouldNotThrowAnErrorWithNewEmail() throws UserColaboreException {
//        UserCreateDTO dto = UserCreateDTO
//                .builder()
//                .name("José")
//                .email("jose@gmail.com")
//                .password("123")
//                .profilePhoto(null)
//                .build();
//
//        UserEntity user = new UserEntity();
//        user.setName("José");
//        user.setEmail("jose@gmail.com");
//        user.setPassword("123");
//
//        RoleEntity roleEntity = new RoleEntity();
//        roleEntity.setRoleId(1);
//        roleEntity.setName("ROLE_USER");
//
//        userEntity.setRoles(roleEntity);
//
//        assertDoesNotThrow(() -> userService.create(dto));
//
//        when(userRepository.findByEmail(dto.getEmail())).thenReturn(null);
//        when(roleRepository.findById(1)).then(Optional.of(roleEntity)).;
//        when(userRepository.save(user)).thenReturn(user);
//        when(user.getUserId()).thenReturn(2L);
//        when(user.getEmail()).thenReturn("anyString()");
//        when(user.getPhoto()).thenReturn(new byte[2]);
//
//    }

}
