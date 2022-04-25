package br.com.dbc.devser.colabore.controller;

import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserCreateDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserDetailsDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserGenericDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserUserContributionsDTO;
import br.com.dbc.devser.colabore.exception.BusinessRuleException;
import br.com.dbc.devser.colabore.exception.FundraiserException;
import br.com.dbc.devser.colabore.exception.UserColaboreException;
import br.com.dbc.devser.colabore.service.FundraiserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fundraiser")
@RequiredArgsConstructor
public class FundraiserController {

    private final FundraiserService fundraiserService;

    @ApiOperation(value = "Salva uma campanha no banco de dados.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A campanha foi persistida com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @PostMapping(value = "/save", consumes = {"multipart/form-data"})
    public void saveFundraiser(@ModelAttribute FundraiserCreateDTO fundraiser) throws UserColaboreException, BusinessRuleException {
        fundraiserService.saveFundraiser(fundraiser);
    }

    @ApiOperation(value = "Atualiza as informações de uma campanha no banco de dados.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A campanha foi atualizada com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @PutMapping(value = "/{fundraiserId}", consumes = {"multipart/form-data"})
    public void updateFundraiser(@PathVariable("fundraiserId") Long fundraiserId, @ModelAttribute FundraiserCreateDTO fundUpdate) throws FundraiserException {
        fundraiserService.updateFundraiser(fundraiserId, fundUpdate);
    }

//    @ApiOperation(value = "Atualiza o status de uma campanha no banco de dados.")
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "O status da campanha foi atualizado com sucesso."),
//            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
//            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
//    @PutMapping("/status/{fundraiserId}")
//    public void updateFundraiserStatus(@PathVariable("fundraiserId") Long fundraiserId) throws FundraiserException {
//        fundraiserService.updateFundraiserStatus(fundraiserId);
//    }

    @ApiOperation(value = "Lista as campanhas do usuário. Passar número da página como parâmetro (Resultado paginado).")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "As campanhas foram listadas com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @GetMapping("/userFundraisers/{numberPage}")
    public Page<FundraiserGenericDTO> findUserFundraisers(@PathVariable("numberPage") Integer numberPage) throws BusinessRuleException, UserColaboreException {
        return fundraiserService.findUserFundraisers(numberPage);
    }

    @ApiOperation(value = "Apresenta as informações da campanha de uma maneira mais detalhada. Apresentar o Id da " +
            "campanha como parâmetro na url.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A campanha foi apresentada com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @GetMapping("/fundraiserDetails/{fundraiserId}")
    public FundraiserDetailsDTO fundraiserDetails(@PathVariable("fundraiserId") Long fundraiserId) throws FundraiserException {
        return fundraiserService.fundraiserDetails(fundraiserId);
    }

    @ApiOperation(value = "Apresenta a lista de contribuições(campanhas) que o usuário contribuiu. Passar número da página como parâmetro " +
            "(Resultado paginado)")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "As campanhas foram listadas com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @GetMapping("/userContributions/{numberPage}")
    public Page<FundraiserUserContributionsDTO> userContributions(@PathVariable("numberPage") Integer numberPage) throws BusinessRuleException, UserColaboreException {
        return fundraiserService.userContributions(numberPage);
    }

    @ApiOperation(value = "Filtra as campanhas a partir de categorias listadas. Passar número da página como parâmetro (Resultado paginado)")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "As campanha foram listadas com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @GetMapping("/byCategories/{numberPage}")
    public Page<FundraiserGenericDTO> filterByCategories(@RequestParam List<String> categories, @PathVariable("numberPage") Integer numberPage) {
        return fundraiserService.filterByCategories(categories, numberPage);
    }

    @ApiOperation(value = "Filtra as campanhas que atingiram a meta. Passar número da página como parâmetro (Resultado paginado)")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "As campanhas foram listadas com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @GetMapping("/fundraiserCompleted/{numberPage}")
    public Page<FundraiserGenericDTO> filterByFundraiserComplete(@PathVariable("numberPage") Integer numberPage) {
        return fundraiserService.filterByFundraiserComplete(numberPage);
    }

    @ApiOperation(value = "Filtra as campanhas que ainda não atingiram a meta. Passar número da página como parâmetro (Resultado paginado)")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "As campanhas foram listadas com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @GetMapping("/fundraiserIncomplete/{numberPage}")
    public Page<FundraiserGenericDTO> filterByFundraiserIncomplete(@PathVariable("numberPage") Integer numberPage) {
        return fundraiserService.filterByFundraiserIncomplete(numberPage);
    }

    @ApiOperation(value = "Apresenta todas as campanhas registradas no site. Passar número da página como parâmetro (Resultado paginado)")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "As campanhas foram listadas com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @GetMapping("/findAll/{numberPage}")
    public Page<FundraiserGenericDTO> findAllFundraisers(@PathVariable("numberPage") Integer numberPage) {
        return fundraiserService.findAllFundraisers(numberPage);
    }

    @ApiOperation(value = "Deleta os registros da campanha (campanha e doações) do banco de dados.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Os registros da campanha foram deletados com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @DeleteMapping("/{fundraiserId}")
    public void deleteFundraiser(@PathVariable("fundraiserId") Long fundraiserId) {
        fundraiserService.deleteFundraiser(fundraiserId);
    }

}
