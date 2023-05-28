package com.frcalderon.stock.repository;

import com.frcalderon.stock.model.UpdateStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpdateStockRepository extends JpaRepository<UpdateStock, Long> {

    List<UpdateStock> findAllBySent(boolean sent);
}
