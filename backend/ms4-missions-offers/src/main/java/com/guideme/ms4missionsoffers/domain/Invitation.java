package com.guideme.ms4missionsoffers.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "invitations")
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Column(nullable = false)
    private Long prestataireId;

    @Column(length = 2000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutInvitation statut = StatutInvitation.EN_ATTENTE;

    @Version
    private Long version;

    @Column(precision = 12, scale = 3)
    private BigDecimal tarifPropose;

    private Instant notifieeLe;

    protected Invitation() {
    }

    public Invitation(Mission mission, Long prestataireId, String message) {
        this.mission = mission;
        this.prestataireId = prestataireId;
        this.message = message;
        this.notifieeLe = Instant.now();
    }

    public void modifier(String message) {
        this.message = message;
    }

    public void repondre(StatutInvitation reponse, BigDecimal tarifPropose) {
        this.statut = reponse;
        this.tarifPropose = tarifPropose;
    }

    public BigDecimal getTarifPropose() {
        return tarifPropose;
    }

    public Instant getNotifieeLe() {
        return notifieeLe;
    }

    public Long getId() {
        return id;
    }

    public Mission getMission() {
        return mission;
    }

    public Long getPrestataireId() {
        return prestataireId;
    }

    public String getMessage() {
        return message;
    }

    public StatutInvitation getStatut() {
        return statut;
    }
}
