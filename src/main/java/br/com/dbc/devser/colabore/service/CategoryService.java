package br.com.dbc.devser.colabore.service;

import br.com.dbc.devser.colabore.dto.CategorieDTO;
import br.com.dbc.devser.colabore.repository.CategorieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategorieService {

    private final CategorieRepository categorieRepository;
    private final ObjectMapper objectMapper;

    public List<CategorieDTO> findAllCategories() {
        return categorieRepository.findAll().stream()
                .map(cEntity -> objectMapper.convertValue(cEntity, CategorieDTO.class))
                .collect(Collectors.toList());
    }
}
