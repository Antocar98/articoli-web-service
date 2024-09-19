package com.xantrix.webapp.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

import com.xantrix.webapp.repository.ArticoliRepository;

import lombok.Data;

@Component
@Endpoint(id="customInfo")
@Data
public class CustomInfoEndpoint 
{
	@Autowired
	ArticoliRepository articoliRepository;
	
	@ReadOperation
    public Map<String, Object> customInfo() 
	{
		long QtaArticoli = articoliRepository.countArts();
		
		Map<String, Object> ArtInfo = new HashMap<String, Object>();
		ArtInfo.put("Qta Articoli", QtaArticoli);
		
        return ArtInfo;
    }
	
	@ReadOperation
    public String customEndPointByName(@Selector String name) 
    {
    	 return String.format("Eseguito Metodo GET con parametro %s", name);
    }

    @WriteOperation
    public String writeOperation(@Selector String name) 
    {
        return String.format("Eseguito Metodo POST con parametro %s", name);
    }
    
    @DeleteOperation
    public String deleteOperation(@Selector String name)
    {
    	return String.format("Eseguito Metodo DELETE con parametro %s", name);
    }
}
