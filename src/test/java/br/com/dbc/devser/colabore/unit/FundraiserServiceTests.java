package br.com.dbc.devser.colabore.unit;

import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserCreateDTO;
import br.com.dbc.devser.colabore.entity.CategoryEntity;
import br.com.dbc.devser.colabore.entity.FundraiserEntity;
import br.com.dbc.devser.colabore.entity.RoleEntity;
import br.com.dbc.devser.colabore.entity.UserEntity;
import br.com.dbc.devser.colabore.exception.FundraiserException;
import br.com.dbc.devser.colabore.exception.UserColaboreException;
import br.com.dbc.devser.colabore.repository.CategoryRepository;
import br.com.dbc.devser.colabore.repository.FundraiserRepository;
import br.com.dbc.devser.colabore.repository.UserRepository;
import br.com.dbc.devser.colabore.service.FundraiserService;
import br.com.dbc.devser.colabore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FundraiserServiceTests {

    @InjectMocks
    private FundraiserService fundraiserService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private FundraiserRepository fundraiserRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private FundraiserEntity fundraiserEntity;

    @Mock
    private UserEntity userEntity;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Authentication authentication = Mockito.mock(Authentication.class);

    private final SecurityContext securityContext = Mockito.mock(SecurityContext.class);



    @Before
    public void BeforeEach() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(fundraiserService, "objectMapper", objectMapper);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("1");
    }


    @Test
    public void shouldSaveFundraiser() throws UserColaboreException {
        FundraiserCreateDTO fundraiserCreateDTO = fundMockDTO();

        UserEntity user = userFundMock();
        FundraiserEntity fundraiser = new FundraiserEntity();
        fundraiser.setFundraiserId(1L);

        SecurityContextHolder.setContext(securityContext);

        when(userService.getLoggedUser()).thenReturn(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(fundraiserRepository.save(any())).thenReturn(fundraiser);

        fundraiserService.saveFundraiser(fundraiserCreateDTO);
    }

//    @Test
//    public void shouldUpdateFundraiser() throws FundraiserException, UserColaboreException {
//
//        when(fundraiserRepository.findById(any())).thenReturn(Optional.of(fundEntityMock()));
//        fundraiserService.updateFundraiser(1L, fundMockDTO());
//    }

    public RoleEntity roleMock() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleId(1);
        roleEntity.setName("ROLE_USER");
        return roleEntity;
    }

    public UserEntity userFundMock() {
        UserEntity userMock = new UserEntity();
        userMock.setUserId(1L);
        userMock.setName("exemplo");
        userMock.setEmail("exemplo@gmail.com");
        userMock.setPassword("123");
        userMock.setPhoto(null);
        userMock.setRoles(roleMock());
        return userMock;
    }

    public Set<String> categoryMock() {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName("categorytest");
        categoryEntity.setCategoryId(1L);

        Set<String> categoryEntities = new HashSet<>();
        categoryEntities.add(String.valueOf(categoryEntity));
        return categoryEntities;
    }

    public Set<CategoryEntity> categoryEntityMock() {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName("categorytest");
        categoryEntity.setCategoryId(1L);

        Set<CategoryEntity> categoryEntities = new HashSet<>();
        categoryEntities.add(categoryEntity);
        return categoryEntities;
    }

    public FundraiserCreateDTO fundMockDTO() {
        FundraiserCreateDTO fundraiserCreateDTO = new FundraiserCreateDTO();
        fundraiserCreateDTO.setTitle("FundTest");
        fundraiserCreateDTO.setGoal(new BigDecimal("100.0"));
        fundraiserCreateDTO.setAutomaticClose(false);
        fundraiserCreateDTO.setDescription("Teste da Fundraiser");
        fundraiserCreateDTO.setEndingDate(LocalDate.of(2023, 4, 30));
        fundraiserCreateDTO.setCoverPhoto(null);
        fundraiserCreateDTO.setCategories(categoryMock());
        return fundraiserCreateDTO;
    }

    public FundraiserEntity fundEntityMock() {
        FundraiserEntity fundraiserEntityMock = new FundraiserEntity();
        fundraiserEntityMock.setFundraiserId(1L);
        fundraiserEntityMock.setTitle("FundTest");
        fundraiserEntityMock.setDescription("Teste da Fundraiser");
        fundraiserEntityMock.setGoal(new BigDecimal("100.0"));
        fundraiserEntityMock.setCurrentValue(new BigDecimal("0.0"));
        fundraiserEntityMock.setCover(null);
        fundraiserEntityMock.setStatusActive(true);
        fundraiserEntityMock.setCreationDate(LocalDateTime.now());
        fundraiserEntityMock.setEndingDate(LocalDate.of(2023, 4, 30));
        fundraiserEntityMock.setLastUpdate(LocalDateTime.now());
        fundraiserEntityMock.setAutomaticClose(true);
        fundraiserEntityMock.setFundraiserCreator(userFundMock());
        fundraiserEntityMock.setCategoriesFundraiser(categoryEntityMock());
        return fundraiserEntityMock;
    }
}


