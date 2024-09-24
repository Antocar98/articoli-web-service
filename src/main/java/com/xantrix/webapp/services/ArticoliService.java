package com.xantrix.webapp.services;

import java.util.List;

import com.xantrix.webapp.dtos.ArticoliDto;
import com.xantrix.webapp.entities.Articoli;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ArticoliService
{
	public List<ArticoliDto> SelByDescrizione(String descrizione);

	public List<ArticoliDto> SelByDescrizione(String descrizione, Pageable pageable);

	public ArticoliDto SelByCodArt(String codart);

	public Articoli SelByCodArt2(String codart);

	public ArticoliDto SelByBarcode(String barcode);

	public void DelArticolo(Articoli articolo);

	public void InsArticolo(Articoli articolo);

	public void CleanCache();

	public double getPriceArt(String codArt, String idList, String header);
}