package br.com.dbc.devser.colabore.dto.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    @ApiModelProperty(value = "Id da categoria/tag")
    private Long categoryId;

    @ApiModelProperty(value = "Nome da categoria/tag")
    private String name;

}
