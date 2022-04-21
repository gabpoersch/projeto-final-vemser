package br.com.dbc.devser.colabore.service;

import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserCreateDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserDetailsDTO;
import br.com.dbc.devser.colabore.entity.FundraiserEntity;
import br.com.dbc.devser.colabore.exception.BusinessRuleException;
import br.com.dbc.devser.colabore.repository.FundraiserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserService userService;

    //TODO: Adicionar logs

    public void saveFundraiser(FundraiserCreateDTO fundraiserCreate) {
        String authUserId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        UserEntity userEntity = userService.fin

        FundraiserEntity fundEntity = objectMapper.convertValue(fundraiserCreate
                , FundraiserEntity.class);

        long milliseconds = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();

        fundEntity.setCreationDate(milliseconds);
        fundEntity.setCurrentValue(new BigDecimal("0.0"));
        fundEntity.setStatusActive(true);
//        fundEntity.setFundraiserCreator();

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

    public Page<FundraiserDetailsDTO> findUserFundraisers(Integer numberPage) {

        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return fundraiserRepository
                .findFundraisersOfUser(Long.getLong(userId), getPageable(numberPage, 30))
                .map(fEntity -> objectMapper.convertValue(fEntity, FundraiserDetailsDTO.class));
    }

    private FundraiserEntity findById(Long fundraiserId) throws BusinessRuleException {

        return fundraiserRepository.findById(fundraiserId)
                .orElseThrow(() -> new BusinessRuleException("Fundraiser not found."));

    }

    public Page<FundraiserDetailsDTO> findAllFundraisers(Integer numberPage) {

        return fundraiserRepository
                .findAllFundraisersActive(getPageable(numberPage, 20))
                .map(fEntity -> objectMapper.convertValue(fEntity, FundraiserDetailsDTO.class));

    }

    public void deleteFundraiser(Long fundraiserId) {

        fundraiserRepository.deleteById(fundraiserId);

    }

    private Pageable getPageable(Integer numberPage, Integer numberItems) {

        return PageRequest
                .of(numberPage, numberItems, Sort.by("creationDate").ascending());

    }

}
