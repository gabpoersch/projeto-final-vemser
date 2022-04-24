package br.com.dbc.devser.colabore.controller;

import br.com.dbc.devser.colabore.dto.donate.DonateCreateDTO;
import br.com.dbc.devser.colabore.exception.BusinessRuleException;
import br.com.dbc.devser.colabore.exception.FundraiserException;
import br.com.dbc.devser.colabore.exception.UserColaboreException;
import br.com.dbc.devser.colabore.service.DonationService;
import br.com.dbc.devser.colabore.service.FundraiserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/donation")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;
    private final FundraiserService fundraiserService;

    @ApiOperation(value = "Registra uma doação para uma determinada campanha.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A doação foi persistida com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @PostMapping("/{fundraiserId}")
    public void makeDonation(@PathVariable("fundraiserId") Long fundraiserId, @RequestBody DonateCreateDTO donate)
            throws UserColaboreException, FundraiserException, BusinessRuleException {
        donationService.makeDonation(fundraiserId, donate);
        fundraiserService.checkClosed(fundraiserId);
    }

}
