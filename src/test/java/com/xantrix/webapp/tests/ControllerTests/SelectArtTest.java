package com.xantrix.webapp.tests.ControllerTests;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import com.xantrix.webapp.ArticoliWebService;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@ContextConfiguration(classes = ArticoliWebService.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class SelectArtTest
{
	private MockMvc mockMvc;
		
	@Autowired
	private WebApplicationContext wac;
	
	@BeforeEach
	public void setup() throws JSONException, IOException
	{
		mockMvc = MockMvcBuilders
				.webAppContextSetup(wac)
				.build();	
	}

	String JsonData =
			"{\n" +
					"    \"codArt\": \"5008485002\",\n" +
					"    \"descrizione\": \"MELANZANE CAT II IT NC - PROD LOCALE\",\n" +
					"    \"um\": \"PZ\",\n" +
					"    \"codStat\": \"\",\n" +
					"    \"pzCart\": 0,\n" +
					"    \"pesoNetto\": 0,\n" +
					"    \"idStatoArt\": \"1\",\n" +
					"    \"dataCreazione\": \"2016-08-07\",\n" +
					"    \"prezzo\": 0,\n" +
					"    \"barcode\": [\n" +
					"        {\n" +
					"            \"barcode\": \"20909344\",\n" +
					"            \"idTipoArt\": \"PI\"\n" +
					"        }\n" +
					"    ],\n" +
					"    \"ingredienti\": null,\n" +
					"    \"famAssort\": {\n" +
					"        \"id\": 50,\n" +
					"        \"descrizione\": \"ORTOFRUTTA\"\n" +
					"    },\n" +
					"    \"iva\": {\n" +
					"        \"idIva\": 4,\n" +
					"        \"descrizione\": \"IVA RIVENDITA 4%\",\n" +
					"        \"aliquota\": 4\n" +
					"    }\n" +
					"}";

	@Test
	@Order(1)
	public void listArtByEan() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/barcode/20909344")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				 //articoli
				.andExpect(jsonPath("$.codArt").exists())
				.andExpect(jsonPath("$.codArt").value("5008485002"))
				.andExpect(jsonPath("$.descrizione").exists())
				.andExpect(jsonPath("$.descrizione").value("MELANZANE CAT II IT NC - PROD LOCALE"))
				.andExpect(jsonPath("$.um").exists())
				.andExpect(jsonPath("$.um").value("PZ"))
				.andExpect(jsonPath("$.codStat").exists())
				.andExpect(jsonPath("$.codStat").value(""))
				.andExpect(jsonPath("$.pzCart").exists())
				.andExpect(jsonPath("$.pzCart").value("0"))
				.andExpect(jsonPath("$.pesoNetto").exists())
				.andExpect(jsonPath("$.pesoNetto").value("0.0"))
				.andExpect(jsonPath("$.idStatoArt").exists())
				.andExpect(jsonPath("$.idStatoArt").value("1"))
				.andExpect(jsonPath("$.dataCreazione").exists())
				.andExpect(jsonPath("$.dataCreazione").value("2016-08-07"))
				 //barcode
				.andExpect(jsonPath("$.barcode[0].barcode").exists())
				.andExpect(jsonPath("$.barcode[0].barcode").value("20909344"))
				.andExpect(jsonPath("$.barcode[0].idTipoArt").exists())
				.andExpect(jsonPath("$.barcode[0].idTipoArt").value("PI"))
				 //famAssort
				.andExpect(jsonPath("$.famAssort.id").exists())
				.andExpect(jsonPath("$.famAssort.id").value("50"))
				.andExpect(jsonPath("$.famAssort.descrizione").exists())
				.andExpect(jsonPath("$.famAssort.descrizione").value("ORTOFRUTTA"))
				 //ingredienti
				.andExpect(jsonPath("$.ingredienti").isEmpty())
				 //Iva
				.andExpect(jsonPath("$.iva.idIva").exists())
				.andExpect(jsonPath("$.iva.idIva").value("4"))
				.andExpect(jsonPath("$.iva.descrizione").exists())
				.andExpect(jsonPath("$.iva.descrizione").value("IVA RIVENDITA 4%"))
				.andExpect(jsonPath("$.iva.aliquota").exists())
				.andExpect(jsonPath("$.iva.aliquota").value("4"))
				
				.andDo(print());
	}
	
	private String Barcode = "8008490002138";

	@Test
	@Order(2)
	public void ErrlistArtByEan() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/barcode/" + Barcode)
						.contentType(MediaType.APPLICATION_JSON)
						.content(JsonData)
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isNotFound())
						.andExpect(jsonPath("$.code", is(404)))
						.andExpect(jsonPath("$.message").value("Il barcode " + Barcode + " non e' stato trovato!"))
						.andDo(print());
	}
	
	@Test
	@Order(3)
	public void listArtByCodArt() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/codice/5008485002")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(JsonData)) 
				.andReturn();
	}
	
	private String CodArt = "500002000302";
	
	@Test
	@Order(4)
	public void errlistArtByCodArt() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/codice/" + CodArt)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code", is(404)))
				.andExpect(jsonPath("$.message").value("L'articolo con codice " + CodArt + " non e' stato trovato!"))
				.andDo(print());
	}
	
	private String JsonData2 = "[" + JsonData + "]";

	@Test
	@Order(5)
	public void listArtByDesc() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/descrizione/MELANZANE CAT II IT NC - PROD LOCALE")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(JsonData2)) 
				.andReturn();
	}

	@Test
	@Order(6)
	public void errlistArtByDesc()  throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/descrizione/ABC1234")
						.contentType(MediaType.APPLICATION_JSON)
						.content(JsonData)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code", is(404)))
				.andExpect(jsonPath("$.message").value("Non e' stato trovato alcun articolo avente descrizione ABC1234"))
				.andDo(print());
	}
}
