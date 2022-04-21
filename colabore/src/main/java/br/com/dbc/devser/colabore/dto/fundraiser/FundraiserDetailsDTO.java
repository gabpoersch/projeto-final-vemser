package br.com.dbc.devser.colabore.dto.fundraiser;

import br.com.dbc.devser.colabore.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundraiserDetailsDTO {

    private Long fundraiserId;

    private String title;

    private BigDecimal goal;

    private String description;

    private String coverPhoto;

    private List<String> categories;

    private Set<UserDTO> contributors;
}
