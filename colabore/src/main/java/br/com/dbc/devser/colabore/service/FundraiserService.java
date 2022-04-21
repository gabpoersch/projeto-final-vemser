package br.com.dbc.devser.colabore.service;

import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserCreateDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserDetailsDTO;
import br.com.dbc.devser.colabore.dto.user.UserDTO;
import br.com.dbc.devser.colabore.entity.FundraiserEntity;
import br.com.dbc.devser.colabore.exception.BusinessRuleException;
import br.com.dbc.devser.colabore.repository.FundraiserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FundraiserService {

    private final ObjectMapper objectMapper;
    private final FundraiserRepository fundraiserRepository;

    //TODO: Adicionar logs

    public void saveFundraiser(String token, FundraiserCreateDTO fundraiserCreate) {

        FundraiserEntity fundEntity = objectMapper.convertValue(fundraiserCreate
                , FundraiserEntity.class);

        //TODO: Capturar token e pegar id do usuário para inserir no fundraiser;

        long milliseconds = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();

        fundEntity.setCreationDate(milliseconds);
        fundEntity.setCurrentValue(new BigDecimal("0.0"));
        fundEntity.setStatus(true);

        fundraiserRepository.save(fundEntity);
    }

    public void updateFundraiser(Long fundraiserId, FundraiserCreateDTO fundraiserUpdate) throws BusinessRuleException {

        FundraiserEntity fundraiserEntity = findById(fundraiserId);

        if (fundraiserEntity.getDonations().size() == 0) {
            throw new BusinessRuleException("Fundraiser already have donations.");
        }

        fundraiserRepository.save(objectMapper.convertValue(fundraiserUpdate
                , FundraiserEntity.class));
    }

    public FundraiserDetailsDTO getFundraiserDetails(Long fundraiserId) throws BusinessRuleException {

        FundraiserEntity fundraiserEntity = findById(fundraiserId);
        FundraiserDetailsDTO fundraiserDetails = objectMapper.convertValue(fundraiserEntity
                , FundraiserDetailsDTO.class);

        fundraiserDetails.setFundraiserUsers(fundraiserEntity.getDonations()
                .stream().map(donation -> objectMapper.convertValue(donation.getDonator(), UserDTO.class))
                .collect(Collectors.toSet()));

        return fundraiserDetails;
    }

    private FundraiserEntity findById(Long fundraiserId) throws BusinessRuleException {
        return fundraiserRepository.findById(fundraiserId)
                .orElseThrow(() -> new BusinessRuleException("Fundraiser not found."));
    }

//    public FundraiserDetailsDTO findByTitle (String title){
//
//    }


}
