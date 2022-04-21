package br.com.dbc.devser.colabore.dto.fundraiser;

import br.com.dbc.devser.colabore.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundraiserDetailsDTO extends FundraiserCreateDTO {

    private Long fundraiserId;

    private String fundraiserCreator;

    private BigDecimal currentValue;

    private LocalDateTime lastUpdate;

    private Set<UserDTO> fundraiserUsers;
}
