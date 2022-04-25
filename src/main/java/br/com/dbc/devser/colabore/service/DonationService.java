package br.com.dbc.devser.colabore.service;

import br.com.dbc.devser.colabore.dto.donate.DonateCreateDTO;
import br.com.dbc.devser.colabore.entity.DonationEntity;
import br.com.dbc.devser.colabore.entity.FundraiserEntity;
import br.com.dbc.devser.colabore.entity.UserEntity;
import br.com.dbc.devser.colabore.exception.BusinessRuleException;
import br.com.dbc.devser.colabore.exception.FundraiserException;
import br.com.dbc.devser.colabore.exception.UserColaboreException;
import br.com.dbc.devser.colabore.repository.DonationRepository;
import br.com.dbc.devser.colabore.repository.FundraiserRepository;
import br.com.dbc.devser.colabore.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DonationService {

    private final ObjectMapper objectMapper;
    private final DonationRepository donationRepository;
    private final FundraiserRepository fundraiserRepository;
    private final FundraiserService fundraiserService;
    private final UserRepository userRepository;
    private final UserService userService;

    public void makeDonation(Long fundraiserId, DonateCreateDTO donate) throws UserColaboreException, FundraiserException, BusinessRuleException {
                UserEntity userEntity = userRepository.findById(userService.getLoggedUserId())
                .orElseThrow(() -> new UserColaboreException("User not found in database."));

        FundraiserEntity fundraiserEntity = fundraiserRepository.findById(fundraiserId)
                .orElseThrow(() -> new FundraiserException("Fundraiser not found in database."));

        if (!fundraiserEntity.getStatusActive()) {
            throw new FundraiserException("You can not donate to a closed fundraiser.");
        }

        DonationEntity donationEntity = objectMapper.convertValue(donate, DonationEntity.class);

        /*Setando o donate no banco*/
        donationEntity.setDonator(userEntity);
        donationEntity.setFundraiser(fundraiserEntity);
        DonationEntity donationSaved = donationRepository.save(donationEntity);

        log.info("Donation {} registered with success.", donationSaved.getDonationId());

        /*Atualizando o currentValue no banco*/
        fundraiserEntity.setCurrentValue(fundraiserEntity.getCurrentValue().add(donate.getValue()));
        fundraiserRepository.save(fundraiserEntity);

        if (fundraiserEntity.getAutomaticClose()) {
            fundraiserService.checkClosed(fundraiserId);
        }

    }

}
