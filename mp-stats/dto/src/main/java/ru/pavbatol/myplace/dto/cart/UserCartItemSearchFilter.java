package ru.pavbatol.myplace.dto.cart;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.pavbatol.myplace.dto.AbstractSearchFilter;

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

    @Override
    public UserCartItemSearchFilter populateNullFields() {
        ensureNonNullFields();
        setUserIds(getUserIds() != null ? getUserIds() : List.of());
        return this;
    }
}
