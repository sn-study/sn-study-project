package sn.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.example.demo.model.Ppurigi;

import java.util.Date;
import java.util.Optional;

@Repository
public interface PpurigiRepository extends JpaRepository <Ppurigi, Long> {
    Optional<Ppurigi> findByTokenAndExpDtsGreaterThan(String token, Date expDts);
}
