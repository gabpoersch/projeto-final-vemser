package br.com.dbc.devser.colabore.unit;

import br.com.dbc.devser.colabore.dto.donate.DonateCreateDTO;
import br.com.dbc.devser.colabore.entity.FundraiserEntity;
import br.com.dbc.devser.colabore.entity.UserEntity;
import br.com.dbc.devser.colabore.exception.FundraiserException;
import br.com.dbc.devser.colabore.repository.FundraiserRepository;
import br.com.dbc.devser.colabore.service.DonationService;
import br.com.dbc.devser.colabore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DonationServiceTests {

    @InjectMocks
    private DonationService donationService;

    @Mock
    private UserService userService;

    @Mock
    private FundraiserRepository fundraiserRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Authentication authentication = Mockito.mock(Authentication.class);

    private final SecurityContext securityContext = Mockito.mock(SecurityContext.class);

    @BeforeEach
    public void initMethods() {
        ReflectionTestUtils.setField(donationService, "objectMapper", objectMapper);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("1");
    }

    /* >>>>>>>>>>>>>   SHOULD THROW FUNDRAISER NOT FOUND  <<<<<<<<<<<<<< */

    @Test(expected = FundraiserException.class)
    public void shouldThrowFundraiserNotFound() throws Exception {

        DonateCreateDTO donateCreateDTO = new DonateCreateDTO();
        UserEntity userMock = userMock();

        when(userService.getLoggedUserId()).thenReturn(userMock);
        when(fundraiserRepository.findById(anyLong())).thenReturn(Optional.empty());

        donationService.makeDonation(1L, donateCreateDTO);
    }

    /* >>>>>>>>>>>>>   SHOULD THROW FUNDRAISER ERROR CLOSED  <<<<<<<<<<<<<< */

    @Test(expected = FundraiserException.class)
    public void shouldThrowAnErrorForAClosedFundraiser() throws Exception {

        DonateCreateDTO donateCreateDTO = new DonateCreateDTO();
        UserEntity userMock = userMock();

        FundraiserEntity fundEntity = new FundraiserEntity();
        fundEntity.setStatusActive(false);

        when(userService.getLoggedUserId()).thenReturn(userMock);
        when(fundraiserRepository.findById(anyLong())).thenReturn(Optional.of(fundEntity));

        donationService.makeDonation(1L, donateCreateDTO);
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
