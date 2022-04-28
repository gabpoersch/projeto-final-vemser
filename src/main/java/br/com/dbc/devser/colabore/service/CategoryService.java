package br.com.dbc.devser.colabore.service;

import br.com.dbc.devser.colabore.dto.category.CategoryDTO;
import br.com.dbc.devser.colabore.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    public List<CategoryDTO> findAllCategories() {
        return categoryRepository.findAll().stream()
                .map(cEntity -> objectMapper.convertValue(cEntity, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    public CategoryDTO findByName(String name) {
        return objectMapper.convertValue(categoryRepository.findByName(name), CategoryDTO.class);
    }



}
