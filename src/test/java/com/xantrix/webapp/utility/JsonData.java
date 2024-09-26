package com.xantrix.webapp.utility;


public class JsonData 
{
	public static String GetArtData()
	{
		String JsonData =  
				"{\n" + 
				"    \"codArt\": \"002000301\",\n" + 
				"    \"descrizione\": \"ACQUA ULIVETO 15 LT\",\n" + 
				"    \"um\": \"PZ\",\n" + 
				"    \"codStat\": \"\",\n" + 
				"    \"pzCart\": 6,\n" + 
				"    \"pesoNetto\": 1.5,\n" + 
				"    \"idStatoArt\": \"1\",\n" + 
				"    \"dataCreazione\": \"2010-06-14\",\n" + 
				"    \"barcode\": [\n" + 
				"        {\n" + 
				"            \"barcode\": \"8008490000021\",\n" + 
				"            \"idTipoArt\": \"CP\"\n" + 
				"        }\n" + 
				"    ],\n" + 
				"    \"famAssort\": {\n" + 
				"        \"id\": 1,\n" + 
				"        \"descrizione\": \"DROGHERIA ALIMENTARE\"\n" + 
				"    },\n" + 
				"    \"ingredienti\": null,\n" + 
				"    \"iva\": {\n" + 
				"        \"idIva\": 22,\n" + 
				"        \"descrizione\": \"IVA RIVENDITA 22%\",\n" + 
				"        \"aliquota\": 22\n" + 
				"    }\n" + 
				"}";
		
		return JsonData;
	}
	
	public static String GetTestArtData()
	{
		String JsonData =  
				"{\r\n"
				+ "    \"codArt\": \"123Test\",\r\n"
				+ "    \"descrizione\": \"Articolo Unit Test Inserimento\",\r\n"
				+ "    \"um\": \"PZ\",\r\n"
				+ "    \"codStat\": \"TESTART\",\r\n"
				+ "    \"pzCart\": 6,\r\n"
				+ "    \"pesoNetto\": 1.75,\r\n"
				+ "    \"idStatoArt\": \"1\",\r\n"
				+ "    \"dataCreaz\": \"2019-05-14\",\r\n"
				+ "    \"barcode\": [\r\n"
				+ "        {\r\n"
				+ "            \"barcode\": \"12345678\",\r\n"
				+ "            \"idTipoArt\": \"CP\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"ingredienti\": {\r\n"
				+ "		\"codArt\" : \"123Test\",\r\n"
				+ "		\"info\" : \"TEST INGREDIENTI\"\r\n"
				+ "	},\r\n"
				+ "    \"iva\": {\r\n"
				+ "        \"idIva\": 22\r\n"
				+ "    },\r\n"
				+ "    \"famAssort\": {\r\n"
				+ "        \"id\": 1\r\n"
				+ "    }\r\n"
				+ "}";
		
		return JsonData;
	}
}
