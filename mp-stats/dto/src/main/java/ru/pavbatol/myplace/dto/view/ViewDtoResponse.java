package ru.pavbatol.myplace.dto.view;

import lombok.*;
import ru.pavbatol.myplace.dto.annotation.ExcludeJacocoGenerated;

import java.util.Objects;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ViewDtoResponse {
    String app;
    String uri;
    Long views;

    @ExcludeJacocoGenerated
    public ViewDtoResponse withApp(String newApp) {
        return Objects.equals(this.app, newApp) ? this : new ViewDtoResponse(newApp, uri, views);
    }

    @ExcludeJacocoGenerated
    public ViewDtoResponse withUri(String newUri) {
        return Objects.equals(this.uri, newUri) ? this : new ViewDtoResponse(app, newUri, views);
    }

    @ExcludeJacocoGenerated
    public ViewDtoResponse withViews(Long newViews) {
        return Objects.equals(this.views, newViews) ? this : new ViewDtoResponse(app, uri, newViews);
    }
}
