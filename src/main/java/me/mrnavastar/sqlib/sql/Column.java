package me.mrnavastar.sqlib.sql;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Column {
    private final String name;
    private final ColumnType<?> type;
    private final boolean array;
    private final boolean unique;
}
