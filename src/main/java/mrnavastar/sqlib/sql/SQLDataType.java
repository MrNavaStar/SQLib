package mrnavastar.sqlib.sql;

public enum SQLDataType {
    STRING("LONGTEXT"),
    INT("INT", 255),
    FLOAT("FLOAT", 24),
    DOUBLE("DOUBLE", 53),
    BOOL("BOOL"),
    DATE("DATE"),
    TIME("TIME"),
    TIMESTAMP("TIMESTAMP"),
    YEAR("YEAR"),
    UUID("CHAR", 36),

    //Minecraft Data types
    BLOCKPOS("BIGINT", 255),
    CHUNKPOS("BIGINT", 255),
    JSON("LONGTEXT"),
    NBT("LONGTEXT"),
    MUTABLE_TEXT("LONGTEXT");

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