package me.mrnavastar.sqlib.sql;

public enum SQLDataType {
    //Java Datatypes
    STRING("LONGTEXT"),
    INT("INT", 255),
    DOUBLE("FLOAT", 53),
    LONG("BIGINT", 255),
    BOOL("INT", 1),
    DATE("BIGINT", 255),
    COLOR("INT", 255),
    UUID("CHAR", 36),

    // Minecraft Datatypes
    BLOCKPOS("BIGINT", 255),
    CHUNKPOS("BIGINT", 255),
    JSON("LONGTEXT"),
    NBT("LONGTEXT"),
    TEXT("LONGTEXT"),
    MUTABLE_TEXT("LONGTEXT"),
    IDENTIFIER("LONGTEXT");

    // Adventure Datatypes
    //KEY("LONGTEXT"),
    //COMPONENT("LONGTEXT");

    private final String sqlDataType;
    private int size = 0;

    SQLDataType(String sqlDatatype) {
        this.sqlDataType = sqlDatatype;
    }

    SQLDataType(String sqlDatatype, int size) {
        this.sqlDataType = sqlDatatype;
        this.size = size;
    }

    @Override
    public String toString() {
        String string = sqlDataType;
        if (size != 0) string += "(" + size + ")";
        return string;
    }
}