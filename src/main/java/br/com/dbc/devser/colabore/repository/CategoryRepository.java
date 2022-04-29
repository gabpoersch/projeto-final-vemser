package br.com.dbc.devser.colabore.repository;

import br.com.dbc.devser.colabore.entity.CategoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    CategoryEntity findByName(String name);

    @Query("select c from category c where c.name in (:categories)")
    List<CategoryEntity> filterByCategories(List<String> categories);
}
