package ru.pavbatol.myplace.dto.cart;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.pavbatol.myplace.dto.AbstractSearchFilter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCartItemSearchFilter extends AbstractSearchFilter<UserCartItemSearchFilter> {
    List<Long> userIds;
    Long lastUserId;
    Long lastItemCount;

    @Override
    public UserCartItemSearchFilter setNullFieldsToDefault() {
        setBaseNullFieldsToDefault();
        setUserIds(getUserIds() != null ? getUserIds() : List.of());
        setLastUserId(getLastUserId() != null ? getLastUserId() : null);
        setLastItemCount(getLastItemCount() != null ? getLastItemCount() : null);
        return this;
    }

    @Override
    public String toQuery(DateTimeFormatter formatter) {
        return joinQueryParams(
                baseFilterToQuery(formatter),
                toParamFromLongs("userIds", getUserIds()),
                toParam("lastUserId", String.valueOf(getLastUserId())),
                toParam("lastItemCount", String.valueOf(getLastItemCount()))
        );
    }
}
