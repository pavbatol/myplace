package ru.pavbatol.myplace.shared.dto.pagination;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"content", "size", "numberOfElements", "hasNext"})
public interface SimpleSlice<T> {
    @JsonProperty("content")
    List<T> getContent();

    @JsonProperty("size")
    int getSize();

    @JsonProperty("numberOfElements")
    int getNumberOfElements();

    @JsonProperty("hasNext")
    boolean hasNext();
}
