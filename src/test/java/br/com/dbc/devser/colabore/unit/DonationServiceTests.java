package br.com.dbc.devser.colabore.unit;

import br.com.dbc.devser.colabore.dto.donate.DonateCreateDTO;
import br.com.dbc.devser.colabore.email.service.MailService;
import br.com.dbc.devser.colabore.entity.DonationEntity;
import br.com.dbc.devser.colabore.entity.FundraiserEntity;
import br.com.dbc.devser.colabore.entity.UserEntity;
import br.com.dbc.devser.colabore.exception.FundraiserException;
import br.com.dbc.devser.colabore.repository.DonationRepository;
import br.com.dbc.devser.colabore.repository.FundraiserRepository;
import br.com.dbc.devser.colabore.service.DonationService;
import br.com.dbc.devser.colabore.service.FundraiserService;
import br.com.dbc.devser.colabore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DonationServiceTests {

    @Mock
    private final ObjectMapper objectMapper = new ObjectMapper();
    @InjectMocks
    private DonationService donationService;
    @Mock
    private UserService userService;
    @Mock
    private FundraiserRepository fundraiserRepository;
    @Mock
    private DonationRepository donationRepository;
    @Mock
    private FundraiserService fundraiserService;
    @Mock
    private MailService mailService;

    @BeforeEach
    public void initMethods() {
        ReflectionTestUtils.setField(donationService, "objectMapper", objectMapper);
    }

    @Test
    public void shouldMakeDonation() throws Exception {
        when(objectMapper.convertValue(any(), any(Class.class))).thenReturn(donationEntityMock());
        when(fundraiserService.findById(anyLong())).thenReturn(fundEntityMock());
        when(donationRepository.save(any())).thenReturn(donationEntityMock());
        doNothing().when(mailService).donatorMailService(any(), any());
        donationService.makeDonation(fundEntityMock().getFundraiserId(), donateCreateDTOMock());
    }

    /* >>>>>>>>>>>>>   SHOULD THROW FUNDRAISER ERROR CLOSED  <<<<<<<<<<<<<< */

    @Test(expected = FundraiserException.class)
    public void shouldThrowAnErrorForAClosedFundraiser() throws Exception {
        FundraiserEntity fundraiserEntity = new FundraiserEntity();
        fundraiserEntity.setStatusActive(false);
        when(fundraiserService.findById(anyLong())).thenReturn(fundraiserEntity);
        donationService.makeDonation(fundEntityMock().getFundraiserId(), donateCreateDTOMock());
    }

    public UserEntity userMock() {
        UserEntity user = new UserEntity();
        user.setUserId(1L);
        user.setName("exemplo");
        user.setEmail("exemplo@gmail.com");
        user.setPassword("123");
        return user;
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
        return fundraiserEntityMock;
    }

    public DonateCreateDTO donateCreateDTOMock() {
        DonateCreateDTO donateCreateDTO = new DonateCreateDTO();
        donateCreateDTO.setValue(new BigDecimal("50.0"));
        return donateCreateDTO;
    }

    public DonationEntity donationEntityMock() {
        DonationEntity donationEntity = new DonationEntity();
        donationEntity.setDonator(userMock());
        donationEntity.setFundraiser(fundEntityMock());
        return donationEntity;
    }

}
