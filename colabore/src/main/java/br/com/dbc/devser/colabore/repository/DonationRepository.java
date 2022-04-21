package br.com.dbc.devser.colabore.repository;

import br.com.dbc.devser.colabore.entity.DonationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<DonationEntity, Long> {

    @Query("select d from donation d where d.donator.userId = :userId")
    List<DonationEntity> findMyDonations (Long userId);
}
