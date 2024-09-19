package com.xantrix.webapp;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import com.xantrix.webapp.dtos.ArticoliDto;
import com.xantrix.webapp.dtos.BarcodeDto;
import com.xantrix.webapp.entities.Articoli;
import com.xantrix.webapp.entities.Barcode;

@Configuration
public class ModelMapperConfig
{
	@Bean
	public ModelMapper modelMapper()
	{
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		modelMapper.addMappings(articoliMapping);

		modelMapper.addMappings(new PropertyMap<Barcode, BarcodeDto>()
		{
			@Override
			protected void configure()
			{
				map().setIdTipoArt(source.getIdTipoArt());
			}
		});

		modelMapper.addConverter(articoliConverter);

		return modelMapper;
	}

	PropertyMap<Articoli, ArticoliDto> articoliMapping = new PropertyMap<Articoli,ArticoliDto>()
	{
		protected void configure()
		{
			map().setDataCreazione(source.getDataCreaz());
		}
	};

	Converter<String, String> articoliConverter = new Converter<String, String>()
	{
		@Override
		public String convert(MappingContext<String, String> context)
		{
			return context.getSource() == null ? "" : context.getSource().trim();
		}
	};
}