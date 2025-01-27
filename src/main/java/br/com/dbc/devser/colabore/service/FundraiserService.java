package br.com.dbc.devser.colabore.service;

import br.com.dbc.devser.colabore.dto.category.CategoryDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserCreateDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserDetailsDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserGenericDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserUserContributionsDTO;
import br.com.dbc.devser.colabore.dto.user.UserDTO;
import br.com.dbc.devser.colabore.email.service.MailService;
import br.com.dbc.devser.colabore.entity.CategoryEntity;
import br.com.dbc.devser.colabore.entity.FundraiserEntity;
import br.com.dbc.devser.colabore.exception.FundraiserException;
import br.com.dbc.devser.colabore.exception.UserColaboreException;
import br.com.dbc.devser.colabore.repository.CategoryRepository;
import br.com.dbc.devser.colabore.repository.DonationRepository;
import br.com.dbc.devser.colabore.repository.FundraiserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class FundraiserService {

    private final ObjectMapper objectMapper;
    private final FundraiserRepository fundraiserRepository;
    private final DonationRepository donationRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final MailService mailService;

    public void saveFundraiser(FundraiserCreateDTO fundraiserCreate) throws UserColaboreException {
        FundraiserEntity fundraiserEntity = objectMapper.convertValue(fundraiserCreate, FundraiserEntity.class);

        fundraiserEntity.setCreationDate(LocalDateTime.now());
        fundraiserEntity.setCurrentValue(new BigDecimal("0.0"));
        fundraiserEntity.setStatusActive(true);
        fundraiserEntity.setLastUpdate(LocalDateTime.now());
        fundraiserEntity.setFundraiserCreator(userService.getLoggedUser());
        fundraiserEntity.setCategoriesFundraiser(buildCategories(fundraiserCreate.getCategories()));
        /*Seta a foto e grava no banco*/
        FundraiserEntity fundSaved = fundraiserRepository.save(setPhotoEntity(fundraiserEntity, fundraiserCreate));

        log.info("Fundraiser with id number {} registered with success.", fundSaved.getFundraiserId());
    }

    public void updateFundraiser(Long fundraiserId, FundraiserCreateDTO fundraiserUpdate) throws FundraiserException, UserColaboreException {
        FundraiserEntity fundraiserEntity = findById(fundraiserId);

        verifyIfFundraiserIsYours(fundraiserEntity);

        if (fundraiserEntity.getDonations().size() != 0) {
            throw new FundraiserException("Fundraiser already has donations.");
        }

        fundraiserEntity.setTitle(fundraiserUpdate.getTitle());
        fundraiserEntity.setGoal(fundraiserUpdate.getGoal());
        fundraiserEntity.setAutomaticClose(fundraiserUpdate.getAutomaticClose());
        fundraiserEntity.setDescription(fundraiserUpdate.getDescription());
        fundraiserEntity.setEndingDate(fundraiserUpdate.getEndingDate());
        fundraiserEntity.setCategoriesFundraiser(buildCategories(fundraiserUpdate.getCategories()));

        fundraiserEntity.setLastUpdate(LocalDateTime.now());

        fundraiserRepository.save(setPhotoEntity(fundraiserEntity, fundraiserUpdate));

        log.info("Fundraiser with id number {} updated with success.", fundraiserEntity.getFundraiserId());
    }

    private Set<CategoryEntity> buildCategories(Set<String> categories) {
        return categories.stream().map(category -> {
            //***Retirando espaços do final e do começo da string***
            String categoryFormated = category.trim().toLowerCase();
            CategoryEntity categoryReference = categoryRepository.findByName(categoryFormated);

            //***Testando se existe***
            if (categoryReference != null) {
                return categoryReference;
            }
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setName(categoryFormated);

            return categoryRepository.save(categoryEntity);
        }).collect(Collectors.toSet());
    }

    public FundraiserDetailsDTO fundraiserDetails(Long fundraiserId) throws FundraiserException {
        FundraiserEntity fundraiserEntity = findById(fundraiserId);

        FundraiserDetailsDTO details = objectMapper.convertValue(fundraiserEntity, FundraiserDetailsDTO.class);

        if (fundraiserEntity.getCover() != null) {
            details.setCoverPhoto(Base64.getEncoder().encodeToString(fundraiserEntity.getCover()));
        }
        details.setCategories(convertCategories(fundraiserEntity.getCategoriesFundraiser()));
        details.setEndingDate(fundraiserEntity.getEndingDate());
        //Usando o método de build da UserService para construir o user que será exposto.
        details.setFundraiserCreator(userService.buildExposedDTO(fundraiserEntity.getFundraiserCreator()));
        details.setAutomaticClose(fundraiserEntity.getAutomaticClose());
        details.setContributors(fundraiserEntity.getDonations().stream()
                .map(donationEntity -> userService.buildExposedDTO(donationEntity.getDonator())).collect(Collectors.toSet()));

        return details;
    }

    public Page<FundraiserGenericDTO> findFundraisersActiveNotAcchieved(Integer numberPage) {
        return fundraiserRepository
                .findFundraisersActiveNotAcchieved(getPageable(numberPage))
                .map(this::buildFundraiserGeneric);
    }

    public Page<FundraiserGenericDTO> findFundraisersActiveAcchieved(Integer numberPage) {
        return fundraiserRepository
                .findFundraisersActiveAcchieved(getPageable(numberPage))
                .map(this::buildFundraiserGeneric);
    }

    public Page<FundraiserGenericDTO> findAllFundraisersActive(Integer numberPage) {
        return fundraiserRepository
                .findAllFundraisersActive(getPageable(numberPage))
                .map(this::buildFundraiserGeneric);
    }

    public Page<FundraiserGenericDTO> findUserFundraisers(Integer numberPage) throws UserColaboreException {
        return fundraiserRepository
                .findFundraisersOfUser(userService.getLoggedUser().getUserId(), getPageable(numberPage))
                .map(this::buildFundraiserGeneric);
    }

    public Page<FundraiserUserContributionsDTO> userContributions(Integer numberPage) throws UserColaboreException {
        return donationRepository.findMyDonations(userService.getLoggedUser().getUserId(), PageRequest.of(numberPage, 9))
                .map(userContribution -> {
                    FundraiserEntity fEntity = userContribution.getFundraiserEntity();
                    FundraiserUserContributionsDTO userContributions = objectMapper
                            .convertValue(buildFundraiserGeneric(fEntity)
                                    , FundraiserUserContributionsDTO.class);
                    userContributions.setStatus(fEntity.getStatusActive());
                    userContributions.setTotalContribution(userContribution.getValue());
                    return userContributions;
                });
    }

    public Page<FundraiserGenericDTO> filterByCategories(List<String> categories, Integer numberPage) {
        List<String> categoriesLower = new ArrayList<>();
        /*Passa a entrada para lower case (Comparação)*/
        for (String str : categories) {
            categoriesLower.add(str.toLowerCase().trim());
        }
        return fundraiserRepository.filterByCategories(categoriesLower, getPageable(numberPage))
                .map(this::buildFundraiserGeneric);
    }

    public void deleteFundraiser(Long fundraiserId) throws FundraiserException, UserColaboreException {
        FundraiserEntity fundraiserEntity = findById(fundraiserId);

        verifyIfFundraiserIsYours(fundraiserEntity);

        Set<CategoryEntity> categories = fundraiserEntity.getCategoriesFundraiser();

        //Deletando categorias que não estão mais relacionadas a nenhum outro fundraiser;
        categories.stream().filter(categoryEntity -> categoryEntity.getFundraisers().size() == 1)
                .forEach(categoryRepository::delete);

        fundraiserRepository.delete(fundraiserEntity);

        log.info("Fundraiser with id number {} deleted.", fundraiserId);
    }

    public FundraiserEntity findById(Long fundraiserId) throws FundraiserException {
        return fundraiserRepository.findById(fundraiserId)
                .orElseThrow(() -> new FundraiserException("Fundraiser not found."));
    }

    private Pageable getPageable(Integer numberPage) {
        return PageRequest
                .of(numberPage, 9, Sort.by("endingDate").ascending());
    }

    @Scheduled(cron = "0 0 23 * * *")
    public void setStatusFundraiser() {
        log.info("Scheduled method running on {}", LocalDate.now());

        fundraiserRepository.finishedFundraisers(LocalDate.now())
                .forEach(fEntity -> {
                    fEntity.setStatusActive(false);
                    fundraiserRepository.save(fEntity);
                    mailService.fundraiserMailService(fEntity, String.format("Olá, %s! %n%nSua campanha %s foi encerrada " +
                                    "pois chegou à data de encerramento.%nO valor total arrecadado foi de R$ %.2f." +
                                    "%nObrigado por usar a nossa plataforma!\nColabore - VemSerDBC", fEntity.getFundraiserCreator().getName()
                            , fEntity.getTitle(), fEntity.getCurrentValue()));
                });

    }

    public void checkClosed(FundraiserEntity fundraiserEntity) {

        fundraiserEntity.setStatusActive(checkClosedValue(fundraiserEntity.getCurrentValue(), fundraiserEntity.getGoal()));

        fundraiserRepository.save(fundraiserEntity);

        if (!fundraiserEntity.getStatusActive()) {
            mailService.fundraiserMailService(fundraiserEntity, String.format("Olá, %s!%n%nSua meta de R$ %.2f da sua " +
                            "campanha \"%s\" foi atingida com sucesso!" +
                            "%nPara resgatar o valor total da campanha, responda a este e-mail e iremos lhe auxiliar durante o " +
                            "processo :)%n%nObrigado por utilizar a nossa plataforma!%nColabore - VemSerDBC"
                    , fundraiserEntity.getFundraiserCreator().getName(), fundraiserEntity.getGoal()
                    , fundraiserEntity.getTitle()));
        }

    }

    public Boolean checkClosedValue(BigDecimal currentValue, BigDecimal goal) {
        return currentValue.compareTo(goal) < 0;
    }

    private FundraiserGenericDTO completeFundraiser(FundraiserGenericDTO generic, FundraiserEntity fEntity) {
        generic.setCategories(convertCategories(fEntity.getCategoriesFundraiser()));
        generic.setFundraiserCreator(objectMapper.convertValue(fEntity.getFundraiserCreator(), UserDTO.class));
        if (fEntity.getCover() != null) {
            generic.setCoverPhoto(Base64.getEncoder().encodeToString(fEntity.getCover()));
        }
        return generic;
    }

    private Set<CategoryDTO> convertCategories(Set<CategoryEntity> categories) {
        return categories.stream()
                .map(cEntity -> objectMapper.convertValue(cEntity, CategoryDTO.class))
                .collect(Collectors.toSet());
    }

    private FundraiserEntity setPhotoEntity(FundraiserEntity ent, FundraiserCreateDTO fundCreate) {
        try {
            MultipartFile coverPhoto = fundCreate.getCoverPhoto();
            if (coverPhoto != null) {
                ent.setCover(coverPhoto.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ent;
    }

    private void verifyIfFundraiserIsYours(FundraiserEntity fEntity) throws FundraiserException, UserColaboreException {
        /*Não permite a atualização de um fundraiser que não é do usuário.*/
        if (!Objects.equals(userService.getLoggedUser().getUserId(), fEntity.getFundraiserCreator().getUserId())) {
            throw new FundraiserException("You are not the owner.");
        }
    }

    private FundraiserGenericDTO buildFundraiserGeneric(FundraiserEntity fEntity) {
        return completeFundraiser(objectMapper.convertValue(fEntity, FundraiserGenericDTO.class), fEntity);
    }

}
