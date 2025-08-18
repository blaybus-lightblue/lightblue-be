package com.example.lightblue.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "partnership")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Partnership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "initiator_account_id", nullable = false)
    private Account initiatorAccount;

    @ManyToOne
    @JoinColumn(name = "recipient_account_id", nullable = false)
    private Account recipientAccount;

    @Column(nullable = false)
    private String type; // e.g., "COLLABORATION", "COMPANY_APPLICATION"
}