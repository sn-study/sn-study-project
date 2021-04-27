package sn.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.example.demo.model.Ppurigi;

import java.util.Optional;

@Repository
public interface PpurigiRepository extends JpaRepository <Ppurigi, Long> {

    @Query("select p from Ppurigi p where p.token = :token and p.expDts > sysdate")
    Optional<Ppurigi> findByTokenLessThanExpDts(@Param("token") String token);
}
