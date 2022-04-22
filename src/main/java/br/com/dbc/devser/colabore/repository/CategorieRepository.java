package br.com.dbc.devser.colabore.repository;

import br.com.dbc.devser.colabore.entity.CategorieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieRepository extends JpaRepository<CategorieEntity,Long> {


}
