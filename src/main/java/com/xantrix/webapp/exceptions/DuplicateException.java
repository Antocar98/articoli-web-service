package com.xantrix.webapp.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuplicateException  extends Exception
{
	private static final long serialVersionUID = -5870522280695461980L;

	private String messaggio;

	public DuplicateException()
	{
		super();
	}

	public DuplicateException(String messaggio)
	{
		super(messaggio);
		this.messaggio = messaggio;
	}

}