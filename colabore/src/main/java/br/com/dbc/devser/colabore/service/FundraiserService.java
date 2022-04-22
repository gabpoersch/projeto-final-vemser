package br.com.dbc.devser.colabore.service;

import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserCreateDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserDetailsDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserGenericDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserUserContributionsDTO;
import br.com.dbc.devser.colabore.entity.FundraiserEntity;
import br.com.dbc.devser.colabore.exception.BusinessRuleException;
import br.com.dbc.devser.colabore.repository.DonationRepository;
import br.com.dbc.devser.colabore.repository.FundraiserRepository;
import br.com.dbc.devser.colabore.repository.UserRepository;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FundraiserService {

    private final ObjectMapper objectMapper;
    private final FundraiserRepository fundraiserRepository;
    private final DonationRepository donationRepository;
    private final UserRepository userRepository;

    //TODO: Adicionar logs

    public void saveFundraiser(FundraiserCreateDTO fundraiserCreate) throws BusinessRuleException {

        String authUserId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        FundraiserEntity fundEntity = objectMapper.convertValue(fundraiserCreate, FundraiserEntity.class);

        long milliseconds = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();

        fundEntity.setFundraiserCreator(userRepository.findById(Integer.parseInt(authUserId))
                .orElseThrow(()-> new BusinessRuleException("User not found.")));
        fundEntity.setCreationDate(milliseconds);
        fundEntity.setCurrentValue(new BigDecimal("0.0"));
        fundEntity.setStatusActive(true);
        fundEntity.setCategories(convertListToString(fundraiserCreate.getCategories()));

        fundraiserRepository.save(fundEntity);
    }

    public void updateFundraiser(Long fundraiserId, FundraiserCreateDTO fundraiserUpdate) throws BusinessRuleException {

        FundraiserEntity fundraiserEntity = findById(fundraiserId);

        if (fundraiserEntity.getDonations().size() != 0) {
            throw new BusinessRuleException("Fundraiser already have donations.");
        }

        fundraiserRepository.save(objectMapper.convertValue(fundraiserUpdate, FundraiserEntity.class));
    }


    public FundraiserDetailsDTO fundraiserDetails(Long fundraiserId) throws BusinessRuleException {

        FundraiserEntity fundraiserEntity = findById(fundraiserId);

        FundraiserDetailsDTO details = objectMapper.convertValue(fundraiserEntity, FundraiserDetailsDTO.class);

        details.setCategories(convertStringToList(fundraiserEntity.getCategories()));

        return details;

    }

    public Page<FundraiserGenericDTO> findAllFundraisers(Integer numberPage) {
        return fundraiserRepository
                .findAllFundraisersActive(getPageable(numberPage, 20))
                .map(fEntity -> {

                    FundraiserGenericDTO generic = objectMapper.convertValue(fEntity, FundraiserGenericDTO.class);
                    return completeFundraiser(generic, fEntity);

                });
    }


    public Page<FundraiserGenericDTO> findUserFundraisers(Integer numberPage) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return fundraiserRepository
                .findFundraisersOfUser(Long.getLong(userId), getPageable(numberPage, 30))
                .map(fEntity -> {
                    FundraiserGenericDTO generic = objectMapper.convertValue(fEntity, FundraiserGenericDTO.class);
                    return completeFundraiser(generic, fEntity);
                });
    }

    public List<FundraiserUserContributionsDTO> findUserContributions() {
        String authId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return donationRepository.findMyDonations(Long.getLong(authId))
                .stream()
                .map(dEntity -> objectMapper.convertValue(dEntity, FundraiserUserContributionsDTO.class))
                .collect(Collectors.toList());
    }

    public Page<FundraiserGenericDTO> filterByCategories(List<String> categories, Integer numberPage) {
        return fundraiserRepository
                .findByCategoriesContainsIgnoreCaseAndStatusActive(convertListToString(categories), true
                        , getPageable(numberPage, 20))
                .map(fEntity -> {
                    FundraiserGenericDTO generic = objectMapper.convertValue(fEntity, FundraiserGenericDTO.class);
                    return completeFundraiser(generic, fEntity);
                });
    }

    public Page<FundraiserGenericDTO> filterByFundraiserCompleted(Integer numberPage) {
        return fundraiserRepository.findFundraiserCompleted(getPageable(numberPage, 20))
                .map(fEntity -> {
                    FundraiserGenericDTO generic = objectMapper.convertValue(fEntity, FundraiserGenericDTO.class);
                    return completeFundraiser(generic, fEntity);
                });
    }

    public Page<FundraiserGenericDTO> filterByFundraiserIncomplete(Integer numberPage) {
        return fundraiserRepository.findFundraiserIncomplete(getPageable(numberPage, 20))
                .map(fEntity -> {
                    FundraiserGenericDTO generic = objectMapper.convertValue(fEntity, FundraiserGenericDTO.class);
                    return completeFundraiser(generic, fEntity);
                });
    }

    public void deleteFundraiser(Long fundraiserId) {
        fundraiserRepository.deleteById(fundraiserId);
    }

    private FundraiserEntity findById(Long fundraiserId) throws BusinessRuleException {
        return fundraiserRepository.findById(fundraiserId)
                .orElseThrow(() -> new BusinessRuleException("Fundraiser not found."));
    }

    private Pageable getPageable(Integer numberPage, Integer numberItems) {
        return PageRequest
                .of(numberPage, numberItems, Sort.by("creationDate").ascending());
    }

    private LocalDateTime convertLongToLocalDate(Long milliseconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds),
                TimeZone.getDefault().toZoneId());
    }

    private FundraiserGenericDTO completeFundraiser(FundraiserGenericDTO generic, FundraiserEntity fEntity) {
        generic.setCategories(convertStringToList(fEntity.getCategories()));
        generic.setCreationDate(convertLongToLocalDate(fEntity.getCreationDate()));
        generic.setLastUpdate(convertLongToLocalDate(fEntity.getLastUpdate()));
        generic.setFundraiserCreator(fEntity.getFundraiserCreator().getName());
        return generic;
    }

    private List<String> convertStringToList(String categories) {
        return Arrays.asList(categories.split(","));
    }

    private String convertListToString(List<String> listCategories) {
        StringBuilder categoriesFormed = new StringBuilder();
        for (String s : listCategories) {
            categoriesFormed.append(s.replace(",", "")).append(",");
        }
        return categoriesFormed.deleteCharAt(categoriesFormed.length() - 1)
                .toString().replace(" ", "");
    }

}
