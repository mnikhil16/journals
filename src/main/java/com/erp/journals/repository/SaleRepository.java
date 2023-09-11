package com.erp.journals.repository;

import com.erp.journals.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Month;
import java.time.Year;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale,Integer> {
    List<Sale> findSaleBySaleMonthAndYear(Month month, Year year);

}
