package sn.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.example.demo.model.Ppurigi;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PpurigiRepository extends JpaRepository <Ppurigi, Long> {

    Ppurigi findBySendUserIdAndTokenAndRegDtsGreaterThan(Long sendUserId, String token, Date regDts);

    Optional<Ppurigi> findByTokenAndExpDtsGreaterThan(String token, Date expDts);
}
