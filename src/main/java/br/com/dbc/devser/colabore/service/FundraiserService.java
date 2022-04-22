package br.com.dbc.devser.colabore.service;

import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserCreateDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserDetailsDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserGenericDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserUserContributionsDTO;
import br.com.dbc.devser.colabore.entity.CategoryEntity;
import br.com.dbc.devser.colabore.entity.DonationEntity;
import br.com.dbc.devser.colabore.entity.FundraiserEntity;
import br.com.dbc.devser.colabore.exception.BusinessRuleException;
import br.com.dbc.devser.colabore.exception.FundraiserException;
import br.com.dbc.devser.colabore.exception.UserColaboreException;
import br.com.dbc.devser.colabore.repository.DonationRepository;
import br.com.dbc.devser.colabore.repository.FundraiserRepository;
import br.com.dbc.devser.colabore.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FundraiserService {

    private final ObjectMapper objectMapper;
    private final FundraiserRepository fundraiserRepository;
    private final DonationRepository donationRepository;
    private final UserRepository userRepository;

    public void saveFundraiser(FundraiserCreateDTO fundraiserCreate) throws UserColaboreException {

        String authUserId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        FundraiserEntity fundEntity = objectMapper.convertValue(fundraiserCreate, FundraiserEntity.class);

        fundEntity.setFundraiserCreator(userRepository.findById(Integer.parseInt(authUserId))
                .orElseThrow(() -> new UserColaboreException("User not found.")));

        try {
            fundEntity.setCoverPhoto(FileUtils.readFileToByteArray(fundraiserCreate.getCoverPhoto()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        fundEntity.setCreationDate(LocalDateTime.now());
        fundEntity.setCurrentValue(new BigDecimal("0.0"));
        fundEntity.setStatusActive(true);
        fundEntity.setCategories(convertCategoriesEntity(fundraiserCreate.getCategories()));

        fundraiserRepository.save(fundEntity);
    }

    public void updateFundraiser(Long fundraiserId, FundraiserCreateDTO fundraiserUpdate) throws FundraiserException {

        FundraiserEntity fundraiserEntity = findById(fundraiserId);

        if (fundraiserEntity.getDonations().size() != 0) {
            throw new FundraiserException("Fundraiser already has donations.");
        }

        fundraiserEntity.setTitle(fundraiserUpdate.getTitle());
        fundraiserEntity.setGoal(fundraiserUpdate.getGoal());
        fundraiserEntity.setDescription(fundraiserUpdate.getDescription());
        fundraiserEntity.setCategories(convertCategoriesEntity(fundraiserUpdate.getCategories()));
        fundraiserEntity.setAutomaticClose(fundraiserUpdate.getAutomaticClose());
        try {
            fundraiserEntity.setCoverPhoto(FileUtils.readFileToByteArray(fundraiserUpdate.getCoverPhoto()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        fundraiserEntity.setLastUpdate(LocalDateTime.now());

        fundraiserRepository.save(fundraiserEntity);
    }


    public FundraiserDetailsDTO fundraiserDetails(Long fundraiserId) throws FundraiserException {

        FundraiserEntity fundraiserEntity = findById(fundraiserId);

        FundraiserDetailsDTO details = objectMapper.convertValue(fundraiserEntity, FundraiserDetailsDTO.class);

        details.setCategories(convertCategories(fundraiserEntity.getCategories()));

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

    public Page<FundraiserUserContributionsDTO> userContributions(Integer numberPage) {
        String authId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return donationRepository.findMyDonations(Long.getLong(authId), getPageable(numberPage, 20))
                .map(dEntity -> objectMapper.convertValue(dEntity, FundraiserUserContributionsDTO.class));
    }

//    public Page<FundraiserGenericDTO> filterByCategories(List<String> categories, Integer numberPage) {
//        return fundraiserRepository
//                .findByCategoriesContainsIgnoreCaseAndStatusActive(convertListToString(categories), true
//                        , getPageable(numberPage, 20))
//                .map(fEntity -> {
//                    FundraiserGenericDTO generic = objectMapper.convertValue(fEntity, FundraiserGenericDTO.class);
//                    return completeFundraiser(generic, fEntity);
//                });
//    }

    public Page<FundraiserGenericDTO> filterByFundraiserComplete(Integer numberPage) {
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

    private FundraiserEntity findById(Long fundraiserId) throws FundraiserException {
        return fundraiserRepository.findById(fundraiserId)
                .orElseThrow(() -> new FundraiserException("Fundraiser not found."));
    }

    private Pageable getPageable(Integer numberPage, Integer numberItems) {
        return PageRequest
                .of(numberPage, numberItems, Sort.by("creationDate").ascending());
    }


    private FundraiserGenericDTO completeFundraiser(FundraiserGenericDTO generic, FundraiserEntity fEntity) {
        generic.setCategories(convertCategories(fEntity.getCategories()));
        generic.setCurrentValue(calculateTotal(fEntity));
        generic.setCreationDate(fEntity.getCreationDate());
        generic.setLastUpdate(fEntity.getLastUpdate());
        generic.setFundraiserCreator(fEntity.getFundraiserCreator().getName());
        return generic;
    }

    private Set<String> convertCategories(Set<CategoryEntity> categories) {
        return categories.stream()
                .map(CategoryEntity::getName)
                .collect(Collectors.toSet());
    }

    private Set<CategoryEntity> convertCategoriesEntity(Set<String> listCategories) {
        return listCategories.stream()
                .map(str -> CategoryEntity.builder().name(str).build())
                .collect(Collectors.toSet());
    }

    private String convertListToString(List<String> listCategories) {
        StringBuilder categoriesFormed = new StringBuilder();
        for (String s : listCategories) {
            categoriesFormed.append(s.replace(",", "")).append(",");
        }
        return categoriesFormed.deleteCharAt(categoriesFormed.length() - 1)
                .toString().replace(" ", "");
    }

    private BigDecimal calculateTotal(FundraiserEntity fEntity) {
        return fEntity.getDonations().stream().map(DonationEntity::getValue)
                .reduce(BigDecimal::add).orElse(null);
    }

    public void checkClosed (Long idRequest) throws BusinessRuleException {
        FundraiserEntity fundraiserEntity = fundraiserRepository.findById(idRequest)
                .orElseThrow(()-> new BusinessRuleException("Fundraiser not found."));

        fundraiserEntity.setAutomaticClose(checkClosedValue(fundraiserEntity.getCurrentValue(), fundraiserEntity.getGoal()));

        fundraiserRepository.save(fundraiserEntity);
    }

    public Boolean checkClosedValue(BigDecimal currentValue, BigDecimal goal){
        if (currentValue.compareTo(goal) <= 0) {
            return false;
        }
        else {return true;}
    }

}
