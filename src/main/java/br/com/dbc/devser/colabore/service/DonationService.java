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

    public void makeDonation(Long fundraiserId, DonateCreateDTO donate) throws UserColaboreException, FundraiserException, BusinessRuleException {
        String authId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserEntity userEntity = userRepository.findById(Integer.parseInt(authId))
                .orElseThrow(() -> new UserColaboreException("User not found in database."));

        FundraiserEntity fundraiserEntity = fundraiserRepository.findById(fundraiserId)
                .orElseThrow(() -> new FundraiserException("Fundraiser not found in database."));

        DonationEntity donationEntity = objectMapper.convertValue(donate, DonationEntity.class);

        donationEntity.setDonator(userEntity);
        donationEntity.setFundraiser(fundraiserEntity);
        DonationEntity donationSaved = donationRepository.save(donationEntity);

        if (fundraiserEntity.getAutomaticClose()) {
            fundraiserService.checkClosed(fundraiserId);
        }

        log.info("Donation {} registered with success.", donationSaved.getDonationId());
    }
}
