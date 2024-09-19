package com.xantrix.webapp.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BARCODE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Barcode implements Serializable
{
    private static final long serialVersionUID = 1853763261962860635L;

    @Id
    @Column(name = "BARCODE")
    @NotNull(message = "{NotNull.Barcode.barcode.Validation}")
    @Size(min = 8, max = 13, message = "{Size.Barcode.barcode.Validation}")
    private String barcode;

    @Column(name = "IDTIPOART")
    @NotNull(message = "{NotNull.Barcode.idTipoArt.Validation}")
    private String idTipoArt;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "CODART", referencedColumnName = "codArt")
    @JsonBackReference
    private Articoli articolo;

}