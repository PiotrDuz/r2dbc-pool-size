package com.example;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@MappedEntity
public record Item(@GeneratedValue @Id Long id, String valA, String valB) {
}
