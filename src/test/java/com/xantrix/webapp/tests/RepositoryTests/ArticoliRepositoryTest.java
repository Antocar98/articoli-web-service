package com.xantrix.webapp.tests.RepositoryTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
 
import com.xantrix.webapp.entities.Articoli;
import com.xantrix.webapp.entities.Barcode;
import com.xantrix.webapp.entities.FamAssort;
import com.xantrix.webapp.entities.Ingredienti;
import com.xantrix.webapp.entities.Iva;
import com.xantrix.webapp.repository.ArticoliRepository;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest()
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(properties = {"profile = test", "seq = 1", "ramo = main"})
public class ArticoliRepositoryTest
{
	
	@Autowired
	private ArticoliRepository articoliRepository;

	@Test
	@Order(1)
	public void TestInsArticolo() {
		Date date = new Date();

		// Crea un nuovo articolo
		Articoli articolo = new Articoli("500123453", "Articolo di Test", "PZ", "validStat", 6, 1.75, "1", date, null, null, null, null);

		// Associa la famiglia di assortimento
		FamAssort famAssort = new FamAssort();
		famAssort.setId(1);
		articolo.setFamAssort(famAssort);

		// Aggiungi barcode
		Set<Barcode> EAN = new HashSet<>();
		EAN.add(new Barcode("12345678", "CP", articolo));
		articolo.setBarcode(EAN);

		// Aggiungi IVA
		Iva iva = new Iva();
		iva.setIdIva(22);
		articolo.setIva(iva);

		// Associa ingredienti
		Ingredienti ingredienti = new Ingredienti();
		ingredienti.setCodArt("500123453");
		ingredienti.setInfo("Test inserimento ingredienti");

		// Salva l'articolo
		articoliRepository.save(articolo);

		// Recupera l'articolo dal repository per verificare l'inserimento
		Articoli articoloSalvato = articoliRepository.findByCodArt("500123453");

		// Verifica i campi principali
		assertThat(articoloSalvato).isNotNull();
		assertThat(articoloSalvato.getDescrizione()).isEqualTo("Articolo di Test");
		assertThat(articoloSalvato.getCodArt()).isEqualTo("500123453");

		assertThat(articoliRepository.findByCodArt("500123453"))
				.extracting(Articoli::getDescrizione)
				.isEqualTo("Articolo di Test");
	}


	@Test
	@Order(2)
	public void TestfindByDescrizioneLike()
	{
		List<Articoli> items = articoliRepository.selByDescrizioneLike("ACQUA ULIVETO%");
		assertEquals(1, items.size());
	}
	
	@Test
	@Order(3)
	public void TestfindByDescrizioneLikePage()
	{
		List<Articoli> items = articoliRepository.findByDescrizioneLike("ACQUA%",PageRequest.of(0, 10));
		assertEquals(10, items.size());
	}

	
	@Test
	@Order(4)
	public void TestfindByCodArt() throws Exception
	{
		assertThat(articoliRepository.findByCodArt("500002000301"))
				.extracting(Articoli::getDescrizione)
				.isEqualTo("ARTICOLO UNIT TEST MODIFICA");
				
	}

	@Test
	@Order(5)
	public void TestfindByBarCode()
	{
		assertThat(articoliRepository.selByEan("12345678"))
				.extracting(Articoli::getDescrizione)
				.isEqualTo("Articolo di Test");
	}
	

	@Test
	@Order(6)
	//@Disabled
	public void TestDelArt() throws Exception
	{
		
		articoliRepository.delete(articoliRepository.findByCodArt("500123453"));
		
		assertThat(articoliRepository.findByCodArt("500123453`")).isNull();
				
	}
	
	

}
