package com.xantrix.webapp.repository;

import com.xantrix.webapp.entities.Articoli;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@RefreshScope
public interface ArticoliRepository extends PagingAndSortingRepository<Articoli, String> {

    Articoli findByCodArt(String codArt);

    List<Articoli> findByDescrizioneLike(String descrizione, Pageable pageable);

    @Query(value = "SELECT * FROM Articoli WHERE descrizione LIKE :desArt", nativeQuery = true)
    List<Articoli> selByDescrizioneLike(@Param("desArt") String descrizione);

    @Query(value = "SELECT * FROM articoli a JOIN barcode b ON a.codart = b.codart WHERE b.barcode = :ean",
            nativeQuery = true )
    Articoli selByEan(@Param("ean") String ean);

    @Query(value = "SELECT COUNT(*) FROM ARTICOLI", nativeQuery = true)
    int countArts();

}
