package br.com.dbc.devser.colabore.unit;

import br.com.dbc.devser.colabore.dto.user.UserCreateDTO;
import br.com.dbc.devser.colabore.dto.user.UserDTO;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

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

    private final Authentication authentication = Mockito.mock(Authentication.class);

    private final SecurityContext securityContext = Mockito.mock(SecurityContext.class);

    private final MultipartFile multipartFile = Mockito.mock(MultipartFile.class);

    @Mock
    private UserEntity userEntity;

    @Mock
    private UserCreateDTO userCreateDTO;

    @Mock
    private UserDTO userExposedDTO;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void BeforeEach() {
        ReflectionTestUtils.setField(userService, "objectMapper", objectMapper);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("1");
    }

    /* >>>>>>>>>>> CREATE USER - THROW ERROR DUPLICATE EMAIL <<<<<<<<<<<<<<<*/

    @Test(expected = UserColaboreException.class)
    public void shouldThrowAnErrorWithDuplicatedEmail() throws UserColaboreException {
        UserCreateDTO dto = dtoMock();
        UserEntity userEntity = userMock();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(userEntity);

        userService.create(dto);
    }

    /* ############ CREATE USER - END TEST ##############*/


    /* >>>>>>>>>>>>>> CREATE USER - SHOULD WORK  <<<<<<<<<<<<<<<<*/

    @Test
    public void shouldCreationUserWork() throws UserColaboreException {

        UserCreateDTO dto = dtoMock();
        RoleEntity roleEntity = roleMock();
        UserEntity user = userMock();

        /*Não tem email duplicado*/
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(null);
        /*Retorna um role entity mockado*/
        when(roleRepository.findById(1)).thenReturn(Optional.of(roleEntity));
        /*Salva no método BuildExposedDto e retorna o mock*/
        when(userRepository.save(any())).thenReturn(user);

        userService.create(dto);

        verify(userRepository, times(1)).save(any());
        verify(roleRepository, times(1)).findById(1);
        verify(userRepository, times(1)).findByEmail(dto.getEmail());
        verify(userEntity, times(0)).setPhoto(any());

    }


    /* ########## CREATE USER - END SHOULD WORK ##########*/



    /* >>>>>>>>>> CREATE USER - SET PHOTO ENTITY <<<<<<<<**/

//    @Test
//    public void shouldSetPhotoEntity() throws UserColaboreException, IOException {
////        MockMultipartFile multipartFile = getMockMultipart();
//
//        UserCreateDTO dto = dtoMock();
////        dto.setProfilePhoto(multipartFile);
//
//        RoleEntity role = roleMock();
//
//        UserEntity user = userMock();
//
//        when(userRepository.findByEmail(dto.getEmail())).thenReturn(null);
//        when(roleRepository.findById(1)).thenReturn(Optional.of(role));
//        when(userCreateDTO.getProfilePhoto()).thenReturn(any());
//        when(userRepository.save(any())).thenReturn(user);
//
//
//        /*Método chamado no setPhotoEntity*/
//        verify(userEntity, times(1)).setPhoto(any(), eq(multipartFile));
//        userService.create(dtoMock());
//        /*Método chamado no buildExposedDTO*/
////        verify(userExposedDTO, times(1)).setProfilePhoto(any());
//    }



    /* ############## CREATE USER - SET PHOTO ENTITY ########### */




    /* >>>>>>>>>>>>> UPDATE USER - THROW USER NOT FOUND <<<<<<<<<<<<< */

    @Test(expected = UserColaboreException.class)
    public void shouldThrowUserNotFound() throws UserColaboreException {

        UserCreateDTO userCreateMock = dtoMock();
        UserEntity userMock = userMock();

        SecurityContextHolder.setContext(securityContext);

        /*Fluxo*/
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userMock));
        when(userRepository.findByEmail(any())).thenReturn(userMock);

        userService.update(userCreateMock);
    }


    /* ############# UPDATE USER - THROW USER NOT FOUND ########## */


    /* >>>>>>>>>>>>>> UPDATE USER - SHOULD UPDATE USER <<<<<<< */

    @Test
    public void shouldUpdateUser() throws UserColaboreException {
        UserCreateDTO dto = dtoMock();
        dto.setEmail("exemplo@gmail.com");

        UserEntity user = userMock();

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        userService.update(dto);

        verify(userRepository, times(1)).save(any());
    }


    /* ############# UPDATE USER - SHOULD UPDATE USER ########## */

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

    public MockMultipartFile getMockMultipart() {
        return new MockMultipartFile("data", "filename.txt"
                , "text/plain", "some xml".getBytes());
    }
}
