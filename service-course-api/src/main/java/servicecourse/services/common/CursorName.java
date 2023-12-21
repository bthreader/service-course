package servicecourse.services.common;

import lombok.Getter;

@Getter
public enum CursorName {
    AFTER("after"),
    BEFORE("before");

    private final String name;

    private CursorName(String name) {
        this.name = name;
    }
}
