package com.xantrix.webapp.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.xantrix.webapp.validation.CodArt;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ARTICOLI")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Articoli implements Serializable {
    private static final long serialVersionUID = 291353626011036772L;

    @Id
    @Column(name = "CODART")
    @Size(min = 5, max = 20, message = "{Size.Articoli.codArt.Validation}")
    @NotNull(message = "{NotNull.Articoli.codArt.Validation}")
    @CodArt
    @Schema(description = "Il Codice Interno Univoco dell'Articolo")
    private String codArt;

    @Column(name = "DESCRIZIONE")
    @Size(min = 6, max = 80, message = "{Size.Articoli.descrizione.Validation}")
    private String descrizione;

    @Column(name = "UM")
    private String um;

    @Column(name = "CODSTAT")
    @NotBlank(message = "{NotBlank.Articoli.codStat.Validation}")
    private String codStat;

    @Column(name = "PZCART")
    @Max(value = 99, message = "{Max.Articoli.pzCart.Validation}")
    private Integer pzCart;

    @Column(name = "PESONETTO")
    @Min(value = (long) 0.01, message = "{Min.Articoli.pesoNetto.Validation}")
    private double pesoNetto;

    @Column(name = "IDSTATOART")
    private String idStatoArt;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATACREAZIONE")
    private Date dataCreaz;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "articolo", orphanRemoval = true)
    @JsonManagedReference
    private Set<Barcode> barcode = new HashSet<>();

    @OneToOne(mappedBy = "articolo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Ingredienti ingredienti;

    @ManyToOne
    @JoinColumn(name = "IDIVA", referencedColumnName = "idIva")
    @NotNull(message = "{NotNull.Iva.idIva.Validation}")
    private Iva iva;

    @ManyToOne
    @JoinColumn(name = "IDFAMASS", referencedColumnName = "ID")
    @NotNull(message = "{NotNull.Articoli.famAssort.Validation}")
    private FamAssort famAssort;
}