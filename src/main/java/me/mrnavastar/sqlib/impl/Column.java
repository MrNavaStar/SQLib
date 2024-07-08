package me.mrnavastar.sqlib.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.mrnavastar.sqlib.api.types.SQLibType;

@Getter
@RequiredArgsConstructor
public class Column {
    private final String name;
    private final SQLibType<?> type;
    private final boolean array;
    private final boolean unique;
}
