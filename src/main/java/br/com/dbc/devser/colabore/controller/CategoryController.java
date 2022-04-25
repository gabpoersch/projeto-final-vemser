package br.com.dbc.devser.colabore.controller;

import br.com.dbc.devser.colabore.dto.category.CategoryDTO;
import br.com.dbc.devser.colabore.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation(value = "Lista todas as categorias registradas no site.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "As categorias foram listadas com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @GetMapping("/findAll")
    public List<CategoryDTO> findAllCategories() {
        return categoryService.findAllCategories();
    }

    @ApiOperation(value = "Encontra uma categoria específica pelo nome.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A categoria foi retornada com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @GetMapping("/findByName")
    public CategoryDTO findByName(@RequestParam String name) {
        return categoryService.findByName(name);
    }
}
