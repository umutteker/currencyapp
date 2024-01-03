package com.ozanapp.exchangeapplication.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
public class Conversion {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conversation_seq")
    @SequenceGenerator(name = "conversation_seq", sequenceName = "conversation_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "source")
    private String source;

    @Column(name = "target")
    private String target;

    @Column(name = "transactionId")
    private String transactionId= UUID.randomUUID().toString();

    @Column(name = "exchange_rate")
    private BigDecimal exchangeRate;

    @Column(name = "source_amount")
    private BigDecimal sourceAmount;

    @Column(name = "target_amount")
    private BigDecimal targetAmount;

    @Column(name = "create_date")
    private LocalDate createdDate=LocalDate.now();

}
