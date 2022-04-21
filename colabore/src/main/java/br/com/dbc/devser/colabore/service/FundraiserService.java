package br.com.dbc.devser.colabore.service;

import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserCreateDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserUpdateDTO;
import br.com.dbc.devser.colabore.entity.FundraiserEntity;
import br.com.dbc.devser.colabore.repository.FundraiserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class FundraiserService {

    private final ObjectMapper objectMapper;
    private final FundraiserRepository fundraiserRepository;

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

    public void updateFundraiser(FundraiserUpdateDTO fundraiserUpdate) {
        fundraiserRepository.save(objectMapper.convertValue(fundraiserUpdate
                , FundraiserEntity.class));
    }


}
