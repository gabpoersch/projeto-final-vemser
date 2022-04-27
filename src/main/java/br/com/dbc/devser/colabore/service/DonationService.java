package br.com.dbc.devser.colabore.service;

import br.com.dbc.devser.colabore.dto.donate.DonateCreateDTO;
import br.com.dbc.devser.colabore.email.service.MailService;
import br.com.dbc.devser.colabore.entity.DonationEntity;
import br.com.dbc.devser.colabore.entity.FundraiserEntity;
import br.com.dbc.devser.colabore.entity.UserEntity;
import br.com.dbc.devser.colabore.exception.FundraiserException;
import br.com.dbc.devser.colabore.repository.DonationRepository;
import br.com.dbc.devser.colabore.repository.FundraiserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DonationService {

    private final ObjectMapper objectMapper;
    private final DonationRepository donationRepository;
    private final FundraiserRepository fundraiserRepository;
    private final FundraiserService fundraiserService;
    private final UserService userService;
    private final MailService mailService;

    public void makeDonation(Long fundraiserId, DonateCreateDTO donate) throws Exception {

        UserEntity userEntity = userService.getLoggedUser();

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
        FundraiserEntity fundraiserUpdated = fundraiserRepository.save(fundraiserEntity);

        log.info("Current value up to date, value add = {}.", donate.getValue());

        /*Enviando email para doador*/
        mailService.donatorMailService(donationSaved, fundraiserEntity);

        /*Agora está se passando a referência ao invés do próprio ID*/
        if (fundraiserEntity.getAutomaticClose()) {
            fundraiserService.checkClosed(fundraiserUpdated);
        }

    }

}
