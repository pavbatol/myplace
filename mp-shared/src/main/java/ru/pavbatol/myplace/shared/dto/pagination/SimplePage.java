package ru.pavbatol.myplace.shared.dto.pagination;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"content", "page", "size", "totalElements", "totalPage"})
public interface SimplePage<T> {
    @JsonProperty("content")
    List<T> getContent();

    @JsonProperty("page")
    int getPage();

    @JsonProperty("size")
    int getSize();

    @JsonProperty("totalElements")
    long getTotalElements();

    @JsonProperty("totalPage")
    int getTotalPage();
}
