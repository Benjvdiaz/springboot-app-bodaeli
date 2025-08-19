package com.bodaeli.spring.eshop.springboot_app_bodaeli.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Forzar la serialización de Regalo y evitar problemas con lazy loading
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "regalo_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Regalo regalo;

    @NotBlank(message = "El nombre del invitado es obligatorio")
    private String nombreInvitado;

    @Email(message = "El correo no es válido")
    private String emailInvitado;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal montoPagado;

    private LocalDateTime fecha;

    private String estadoPago;

    private String metodoPago;

    private String referenciaTransferencia;

    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Regalo getRegalo() { return regalo; }
    public void setRegalo(Regalo regalo) { this.regalo = regalo; }

    public String getNombreInvitado() { return nombreInvitado; }
    public void setNombreInvitado(String nombreInvitado) { this.nombreInvitado = nombreInvitado; }

    public String getEmailInvitado() { return emailInvitado; }
    public void setEmailInvitado(String emailInvitado) { this.emailInvitado = emailInvitado; }

    public BigDecimal getMontoPagado() { return montoPagado; }
    public void setMontoPagado(BigDecimal montoPagado) { this.montoPagado = montoPagado; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getReferenciaTransferencia() { return referenciaTransferencia; }
    public void setReferenciaTransferencia(String referenciaTransferencia) { this.referenciaTransferencia = referenciaTransferencia; }
}