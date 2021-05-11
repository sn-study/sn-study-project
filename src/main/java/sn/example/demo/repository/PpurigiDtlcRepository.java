package sn.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sn.example.demo.model.PpurigiDtlc;
import sn.example.demo.model.PpurigiDtlcId;

import java.util.Optional;

import java.util.List;

public interface PpurigiDtlcRepository extends JpaRepository <PpurigiDtlc, PpurigiDtlcId> {

    Optional<PpurigiDtlc> findFirstByIdAndReceiveUserIdIsNull(Long id);

    Optional<PpurigiDtlc> findByIdAndReceiveUserId(Long id, Long receiveUserId);

    List<PpurigiDtlc> findByIdAndReceiveUserIdIsNotNull(Long id);
}