package suftware.tuitui.common.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN("ADMIN"),
    MANAGER("MANAGER"),
    USER("USER");

    private final String value;
}
