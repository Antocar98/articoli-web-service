package com.xantrix.webapp.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class InfoMsg
{
    public LocalDate data;

    public String message;
}
