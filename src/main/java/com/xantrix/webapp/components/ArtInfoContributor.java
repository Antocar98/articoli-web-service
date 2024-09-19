package com.xantrix.webapp.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import com.xantrix.webapp.repository.ArticoliRepository;

@Component
public class ArtInfoContributor implements InfoContributor 
{
	@Autowired
	ArticoliRepository articoliRepository;

	@Override
	public void contribute(Builder builder) 
	{
		int QtaArticoli = articoliRepository.countArts();
		
		Map<String, Object> ArtMap = new HashMap<String, Object>();
		ArtMap.put("Numero Articoli", QtaArticoli);
	        
	    builder.withDetail("articoli-info", ArtMap);
	}

}
