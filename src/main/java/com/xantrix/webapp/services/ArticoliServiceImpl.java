package com.xantrix.webapp.services;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.netflix.discovery.converters.Auto;
import com.xantrix.webapp.dtos.PrezzoDto;
import com.xantrix.webapp.entities.Barcode;
import com.xantrix.webapp.feign.PriceClient;
import feign.FeignException;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.xantrix.webapp.dtos.ArticoliDto;
import com.xantrix.webapp.entities.Articoli;
import com.xantrix.webapp.repository.ArticoliRepository;

import org.springframework.transaction.annotation.Transactional;
@Component
@Transactional(readOnly = true)
@Log
public class ArticoliServiceImpl implements ArticoliService
{
	@Autowired
	ArticoliRepository articoliRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CircuitBreakerFactory<?,?> circuitBreakerFactory;

	@Autowired
	private PriceClient priceClient;

	@Override
	public List<ArticoliDto> SelByDescrizione(String descrizione)
	{
		String filter = "%" + descrizione.toUpperCase() + "%";

		List<Articoli> articoli = articoliRepository.selByDescrizioneLike(filter);

		return ConvertToDto(articoli);
	}

	@Override
	public List<ArticoliDto> SelByDescrizione(String descrizione, Pageable pageable)
	{
		String filter = "%" + descrizione.toUpperCase() + "%";

		List<Articoli> articoli = articoliRepository.findByDescrizioneLike(filter, pageable);

		return ConvertToDto(articoli);


	}

	@Override
	public double getPriceArt(String CodArt, String IdList, String Header)
	{
		Double Prezzo = 0.00;

		CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");

		try
		{
			String listino = (IdList == null) ? "" : IdList;

			Prezzo = (!listino.isEmpty()) ?
					circuitBreaker.run(() -> priceClient.getPriceArt(Header, CodArt, listino),
							throwable -> SelPrezzoFallBack(Header, CodArt)) :
					circuitBreaker.run(() -> priceClient.getDefPriceArt(Header, CodArt),
							throwable -> SelPrezzoFallBack(Header, CodArt));

			log.info("Prezzo Articolo " + CodArt + ": " + Prezzo);

		}
		catch(FeignException ex)
		{
			log.warning(String.format("Errore: %s", ex.getLocalizedMessage()));
		}

		return Prezzo;
	}

	public double SelPrezzoFallBack(String Header, String CodArt)
	{
		log.warning("****** SelPrezzoFallBack in esecuzione *******");

		return priceClient.getDefPriceArt(Header, CodArt);
	}

	@Override
	public Articoli SelByCodArt2(String codart)
	{
		return  articoliRepository.findByCodArt(codart);
	}

	@Override
	public ArticoliDto SelByCodArt(String codart)
	{
		Articoli articoli = this.SelByCodArt2(codart);

		return this.ConvertToDto(articoli);
	}

	@Override
	public ArticoliDto SelByBarcode(String barcode)
	{
		Articoli articoli = articoliRepository.selByEan(barcode);

		return this.ConvertToDto(articoli);
	}

	private ArticoliDto ConvertToDto(Articoli articoli)
	{
		ArticoliDto articoliDto = null;


		if (articoli != null)
		{
			articoliDto =  modelMapper.map(articoli, ArticoliDto.class);
		}

		return articoliDto;
	}

	private List<ArticoliDto> ConvertToDto(List<Articoli> articoli)
	{
		List<ArticoliDto> retVal = articoli
				.stream()
				.map(source -> modelMapper.map(source, ArticoliDto.class))
				.collect(Collectors.toList());

		return retVal;
	}

	public void DelArticolo(Articoli articolo)
	{
		articoliRepository.delete(articolo);
	}


	public void InsArticolo(Articoli articolo)
	{
		articolo.setDescrizione(articolo.getDescrizione().toUpperCase());

		articoliRepository.save(articolo);
	}






}