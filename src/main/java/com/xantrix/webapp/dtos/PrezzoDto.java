package com.xantrix.webapp.dtos;

import lombok.Data;

@Data
public class PrezzoDto {
    private String codArt;
    private double prezzo = 0.00;
    private double sconto = 0.00;
    private int tipo;
}
