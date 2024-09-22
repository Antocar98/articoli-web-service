package com.xantrix.webapp.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.xantrix.webapp.dtos.PrezzoDto;
import com.xantrix.webapp.exceptions.ErrorResponse;
import com.xantrix.webapp.feign.PriceClient;
import feign.FeignException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xantrix.webapp.dtos.ArticoliDto;
import com.xantrix.webapp.dtos.InfoMsg;
import com.xantrix.webapp.entities.Articoli;
import com.xantrix.webapp.exceptions.BindingException;
import com.xantrix.webapp.exceptions.DuplicateException;
import com.xantrix.webapp.exceptions.NotFoundException;
import com.xantrix.webapp.services.ArticoliService;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

@RestController
@RequestMapping("api/articoli")
@Log
@Tag(name = "ArticoliController", description = "Controller Operazioni di Gestione Dati Articoli")
public class ArticoliController {

	@Autowired
	private ArticoliService articoliService;

	@Autowired
	private ResourceBundleMessageSource errMessage;

	@Autowired
	private PriceClient priceClient;

	private double getPriceArt(String codArt, String idList, String header)
	{
		double prezzo = 0;
		try{

			PrezzoDto prezzoDto = (!idList.isEmpty() ? priceClient.getPriceArt2(header,codArt,idList):
					priceClient.getDefPriceArt2(header,codArt));

			log.info("Prezzo articolo "+ codArt + ": " + prezzoDto.getPrezzo());

			if(prezzoDto.getSconto()>0) {
				prezzo = prezzoDto.getPrezzo() * (1 - (prezzoDto.getPrezzo() /100));
				prezzo*= 100;
				prezzo = Math.round(prezzo);
				prezzo/= 100;
			} else {
				prezzo = prezzoDto.getPrezzo();
			}

		} catch (FeignException ex) {
			log.warning(String.format("Errore: %s",ex.getLocalizedMessage()));
		}

		return prezzo;
	}


	@Operation(summary = "Ricerca l'articolo per BARCODE", description = "Restituisce i dati dell'articolo in formato JSON",
			tags = { "ArticoliDto" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "L'articolo cercato è stato trovato!"),
			@ApiResponse(responseCode = "404", description = "L'articolo cercato NON è stato trovato!"),
			@ApiResponse(responseCode = "403", description = "Non sei AUTORIZZATO ad accedere alle informazioni"),
			@ApiResponse(responseCode = "401", description = "Non sei AUTENTICATO")
	})
	@GetMapping(value = "/cerca/barcode/{ean}", produces = "application/json")
	@SneakyThrows
	public ResponseEntity<ArticoliDto> listArtByEan(
			@Parameter(description = "Barcode univo dell'articolo") @PathVariable("ean") String Ean, HttpServletRequest httpRequest) {
		log.info(String.format("****** Otteniamo l'articolo con barcode %s *******", Ean));

		String authHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

		ArticoliDto articolo = articoliService.SelByBarcode(Ean);

		if (articolo == null) {
			String ErrMsg = String.format("Il barcode %s non e' stato trovato!", Ean);
			log.warning(ErrMsg);
			throw new NotFoundException(ErrMsg);
		}

		articolo.setPrezzo(getPriceArt(articolo.getCodArt(),"",authHeader));

		return new ResponseEntity<>(articolo, HttpStatus.OK);
	}

	@Operation(summary = "Ricerca l'articolo per BARCODE", description = "Restituisce i dati dell'articolo in formato JSON",
			tags = { "ArticoliDto" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "L'articolo cercato è stato trovato!",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArticoliDto.class))),
			@ApiResponse(responseCode = "401", description = "Utente non AUTENTICATO", content = @Content),
			@ApiResponse(responseCode = "403", description = "Utente Non AUTORIZZATO ad accedere alle informazioni", content = @Content),
			@ApiResponse(responseCode = "404", description = "L'articolo cercato NON è stato trovato!",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
	@GetMapping(value = {"/cerca/codice/{codart}","/cerca/codice/{codart}/{idlist}"}, produces = "application/json")
	@SneakyThrows
	public ResponseEntity<ArticoliDto> listArtByCodArt(@PathVariable("codart") String CodArt, @PathVariable("idlist") Optional<String> optidlist, HttpServletRequest httpRequest) {
		log.info("****** Otteniamo l'articolo con codice " + CodArt + " *******");

		String authHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

		String idList = (optidlist.orElse(""));

		ArticoliDto articolo = articoliService.SelByCodArt(CodArt);

		if (articolo == null) {
			String ErrMsg = String.format("L'articolo con codice %s non e' stato trovato!", CodArt);
			log.warning(ErrMsg);
			throw new NotFoundException(ErrMsg);
		} else{
			articolo.setPrezzo(this.getPriceArt(articolo.getCodArt(),idList,authHeader));
		}

		return new ResponseEntity<>(articolo, HttpStatus.OK);
	}

	@Operation(summary = "Ricerca l'articolo per CODICE", description = "Restituisce i dati dell'articolo in formato JSON", tags = { "ArticoliDto" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "L'articolo cercato è stato trovato!", content = @Content(schema = @Schema(implementation = ArticoliDto.class))),
			@ApiResponse(responseCode = "401", description = "Utente non AUTENTICATO", content = @Content),
			@ApiResponse(responseCode = "403", description = "Utente Non AUTORIZZATO ad accedere alle informazioni", content = @Content),
			@ApiResponse(responseCode = "404", description = "L'articolo cercato NON è stato trovato!", content = @Content) })
	@GetMapping(value = {"/cerca/descrizione/{filter}","/cerca/descrizione/{filter}/{idlist}"}, produces = "application/json")
	@SneakyThrows
	public ResponseEntity<List<ArticoliDto>> listArtByDesc(@PathVariable("filter") String Filter, @PathVariable("idlist") Optional<String> optidlist, HttpServletRequest httpRequest) {
		log.info("****** Otteniamo gli articoli con Descrizione: " + Filter + " *******");

		String authHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

		String idList = (optidlist.orElse(""));

		List<ArticoliDto> articoli = articoliService.SelByDescrizione(Filter);


		if (articoli.isEmpty()) {
			String ErrMsg = String.format("Non e' stato trovato alcun articolo avente descrizione %s", Filter);
			log.warning(ErrMsg);
			throw new NotFoundException(ErrMsg);
		} else {
			articoli.forEach(articoliDto -> {
				articoliDto.setPrezzo(getPriceArt(articoliDto.getCodArt(),idList, authHeader));
			});
		}

		return new ResponseEntity<>(articoli, HttpStatus.OK);
	}

	@Operation(summary = "Ricerca uno o più articoli per descrizione o parte", description = "Restituisce i dati dell'articolo in formato JSON", tags = { "ArticoliDto" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "L'articolo/i cercato/i sono stati trovati!", content = @Content(schema = @Schema(implementation = ArticoliDto.class))),
			@ApiResponse(responseCode = "401", description = "Utente non AUTENTICATO", content = @Content),
			@ApiResponse(responseCode = "403", description = "Utente Non AUTORIZZATO ad accedere alle informazioni", content = @Content),
			@ApiResponse(responseCode = "404", description = "L'articolo/i cercato/i NON sono stati trovati!", content = @Content) })
	@PostMapping(value = "/inserisci", produces = "application/json")
	@SneakyThrows
	public ResponseEntity<InfoMsg> createArt(@Valid @RequestBody Articoli articolo, BindingResult bindingResult) {
		log.info("Salviamo l'articolo con codice " + articolo.getCodArt());

		if (bindingResult.hasErrors()) {
			String MsgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
			log.warning(MsgErr);
			throw new BindingException(MsgErr);
		}

		ArticoliDto checkArt = articoliService.SelByCodArt(articolo.getCodArt());
		if (checkArt != null) {
			String MsgErr = String.format("Articolo %s presente in anagrafica! Impossibile utilizzare il metodo POST", articolo.getCodArt());
			log.warning(MsgErr);
			throw new DuplicateException(MsgErr);
		}

		articoliService.InsArticolo(articolo);

		return new ResponseEntity<>(new InfoMsg(LocalDate.now(), "Inserimento Articolo Eseguita con successo!"), HttpStatus.CREATED);
	}

	@Operation(summary = "MODIFICA dati articolo in anagrafica")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Dati articolo salvati con successo"),
			@ApiResponse(responseCode = "400", description = "Uno o più dati articolo non validi"),
			@ApiResponse(responseCode = "404", description = "Articolo non presente in anagrafica"),
			@ApiResponse(responseCode = "403", description = "Non sei AUTORIZZATO ad inserire dati"),
			@ApiResponse(responseCode = "401", description = "Non sei AUTENTICATO")
	})
	@RequestMapping(value = "/modifica", method = RequestMethod.PUT)
	@SneakyThrows
	public ResponseEntity<InfoMsg> updateArt(@Valid @RequestBody Articoli articolo, BindingResult bindingResult) {
		log.info("Modifichiamo l'articolo con codice " + articolo.getCodArt());

		if (bindingResult.hasErrors()) {
			String MsgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
			log.warning(MsgErr);
			throw new BindingException(MsgErr);
		}

		ArticoliDto checkArt = articoliService.SelByCodArt(articolo.getCodArt());
		if (checkArt == null) {
			String MsgErr = String.format("Articolo %s non presente in anagrafica! Impossibile utilizzare il metodo PUT", articolo.getCodArt());
			log.warning(MsgErr);
			throw new NotFoundException(MsgErr);
		}

		articoliService.InsArticolo(articolo);

		return new ResponseEntity<>(new InfoMsg(LocalDate.now(), "Modifica Articolo Eseguita con successo!"), HttpStatus.CREATED);
	}

	@Operation(summary = "ELIMINAZIONE dati articolo in anagrafica")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Dati articolo eliminati con successo"),
			@ApiResponse(responseCode = "404", description = "Articolo non presente in anagrafica"),
			@ApiResponse(responseCode = "403", description = "Non sei AUTORIZZATO ad inserire dati"),
			@ApiResponse(responseCode = "401", description = "Non sei AUTENTICATO")
	})
	@DeleteMapping(value = "/elimina/{codart}", produces = "application/json")
	@SneakyThrows
	public ResponseEntity<?> deleteArt(@PathVariable("codart") String CodArt) {
		log.info("Eliminiamo l'articolo con codice " + CodArt);

		Articoli articolo = articoliService.SelByCodArt2(CodArt);
		if (articolo == null) {
			String MsgErr = String.format("Articolo %s non presente in anagrafica!", CodArt);
			log.warning(MsgErr);
			throw new NotFoundException(MsgErr);
		}

		articoliService.DelArticolo(articolo);

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode responseNode = mapper.createObjectNode();

		responseNode.put("code", HttpStatus.OK.toString());
		responseNode.put("message", "Eliminazione Articolo " + CodArt + " Eseguita Con Successo");

		return new ResponseEntity<>(responseNode, new HttpHeaders(), HttpStatus.OK);
	}
}