package br.com.dbc.devser.colabore.unit;

import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserCreateDTO;
import br.com.dbc.devser.colabore.dto.user.UserCreateDTO;
import br.com.dbc.devser.colabore.entity.CategoryEntity;
import br.com.dbc.devser.colabore.entity.FundraiserEntity;
import br.com.dbc.devser.colabore.entity.RoleEntity;
import br.com.dbc.devser.colabore.entity.UserEntity;
import br.com.dbc.devser.colabore.repository.CategoryRepository;
import br.com.dbc.devser.colabore.repository.FundraiserRepository;
import br.com.dbc.devser.colabore.repository.UserRepository;
import br.com.dbc.devser.colabore.service.FundraiserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private FundraiserRepository fundraiserRepository;

    @Mock
    private UserRepository userRepository;


    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void BeforeEach() {
        ReflectionTestUtils.setField(fundraiserService, "objectMapper", objectMapper);
    }


    @Test
    public void shouldCreateFundraiser() {
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

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName("categorytest");
        categoryEntity.setCategoryId(1L);

        Set<String> categoryEntities = new HashSet<>();
        categoryEntities.add(String.valueOf(categoryEntity));

        FundraiserCreateDTO fundraiserCreateDTO = new FundraiserCreateDTO();
        fundraiserCreateDTO.setTitle("FundTest");
        fundraiserCreateDTO.setGoal(new BigDecimal("100.0"));
        fundraiserCreateDTO.setAutomaticClose(false);
        fundraiserCreateDTO.setDescription("Teste da Fundraiser");
        fundraiserCreateDTO.setEndingDate(LocalDate.of(2022, 4, 30));
        fundraiserCreateDTO.setCoverPhoto(null);
        fundraiserCreateDTO.setCategories(categoryEntities);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> fundraiserService.saveFundraiser(fundraiserCreateDTO));

        verify(fundraiserRepository, times(1)).save(any());

    }
}
