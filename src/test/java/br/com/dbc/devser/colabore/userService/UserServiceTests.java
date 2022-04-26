package br.com.dbc.devser.colabore.userService;

import br.com.dbc.devser.colabore.dto.user.UserCreateDTO;
import br.com.dbc.devser.colabore.dto.user.UserDTO;
import br.com.dbc.devser.colabore.entity.RoleEntity;
import br.com.dbc.devser.colabore.entity.UserEntity;
import br.com.dbc.devser.colabore.exception.UserColaboreException;
import br.com.dbc.devser.colabore.repository.RoleRepository;
import br.com.dbc.devser.colabore.repository.UserRepository;
import br.com.dbc.devser.colabore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

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

    @Test
    public void shouldThrowUserNotFound () throws UserColaboreException {
        UserCreateDTO dto = UserCreateDTO
                .builder().name("José")
                .email("josesilva@gmail.com").build();
        UserEntity userEntity = new UserEntity();

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(userEntity));

        Exception ex = assertThrows(UserColaboreException.class,
                () -> userService.update(dto));
        assertEquals("User not found.", ex.getMessage());
    }

    /*End tests create user (UserService)*/

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
//        RoleEntity roleEntity = new RoleEntity();
//        roleEntity.setRoleId(1);
//        roleEntity.setName("suida");
//
//        assertDoesNotThrow(()->userService.create(dto));
//
//        when(userRepository.findByEmail(dto.getEmail())).thenReturn(null);
//        when(roleRepository.findById(1).orElseThrow(()-> new UserColaboreException("Role not found!")))
//                .thenReturn(roleEntity);
//        when(userRepository.save(new UserEntity())).thenReturn(user);
//        when (user.getUserId()).thenReturn(2L);
//        when (user.getEmail()).thenReturn("anyString()");
//        when (user.getPhoto()).thenReturn(new byte[2]);
//
//    }

}
