package com.frcalderon.stock.repository;

import com.frcalderon.stock.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("select s from Stock s where s.expirationDate <= :expirationDate")
    List<Stock> findAllWithExpirationDateTimeBefore(@Param("expirationDate") LocalDate expirationDate);
}
