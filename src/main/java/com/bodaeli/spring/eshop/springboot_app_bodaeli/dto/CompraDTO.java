package com.bodaeli.spring.eshop.springboot_app_bodaeli.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

public class CompraDTO {

    @NotNull
    private Long regaloId;

    @NotBlank
    private String nombreInvitado;

    @Email
    private String emailInvitado;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal montoPagado;

    // Nuevo: referencia de transferencia (opcional)
    private String referenciaTransferencia;

    // Getters y Setters
    public Long getRegaloId() { return regaloId; }
    public void setRegaloId(Long regaloId) { this.regaloId = regaloId; }

    public String getNombreInvitado() { return nombreInvitado; }
    public void setNombreInvitado(String nombreInvitado) { this.nombreInvitado = nombreInvitado; }

    public String getEmailInvitado() { return emailInvitado; }
    public void setEmailInvitado(String emailInvitado) { this.emailInvitado = emailInvitado; }

    public BigDecimal getMontoPagado() { return montoPagado; }
    public void setMontoPagado(BigDecimal montoPagado) { this.montoPagado = montoPagado; }

    public String getReferenciaTransferencia() { return referenciaTransferencia; }
    public void setReferenciaTransferencia(String referenciaTransferencia) { this.referenciaTransferencia = referenciaTransferencia; }
}