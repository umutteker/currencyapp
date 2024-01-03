package com.ozanapp.exchangeapplication.repository;

import com.ozanapp.exchangeapplication.entity.Conversion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;

public interface ConversionRepository extends PagingAndSortingRepository<Conversion, Long> {
    Page<Conversion> findAll(Pageable pageable);

    Page<Conversion> findByTransactionId(String transactionId, Pageable pageable);
    void save(Conversion conversion);

    @Query("SELECT c FROM Conversion c WHERE (:transactionId is null or c.transactionId = :transactionId) and " +
            "(:transactionDate is null or (c.createdDate >= :transactionDate and c.createdDate <= :transactionDate ) )")
    Page<Conversion> findByTransactionIdAndTransactionDate(String transactionId, LocalDate transactionDate, Pageable pageable);
}
