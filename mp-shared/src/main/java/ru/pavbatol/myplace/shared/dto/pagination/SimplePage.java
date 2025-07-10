package ru.pavbatol.myplace.shared.dto.pagination;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"content", "number", "size", "totalElements", "totalPages"})
public interface SimplePage<T> {
    @JsonProperty("content")
    List<T> getContent();

    @JsonProperty("number")
    Integer getNumber();

    @JsonProperty("size")
    Integer getSize();

    @JsonProperty("totalElements")
    Long getTotalElements();

    @JsonProperty("totalPages")
    Integer getTotalPages();
}
