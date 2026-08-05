package valorless.valorlessutils.tags;

import org.bukkit.persistence.PersistentDataType;

public enum TagType {
    BYTE,
    BYTE_ARRAY,
    DOUBLE,
    FLOAT,
    INTEGER,
    INTEGER_ARRAY,
    LONG,
    LONG_ARRAY,
    SHORT,
    STRING,
    TAG_CONTAINER;

	public static PersistentDataType<?, ?> GetPersistentDataType(TagType type) {
        return switch (type) {
            case BYTE -> PersistentDataType.BYTE;
            case BYTE_ARRAY -> PersistentDataType.BYTE_ARRAY;
            case DOUBLE -> PersistentDataType.DOUBLE;
            case FLOAT -> PersistentDataType.FLOAT;
            case INTEGER -> PersistentDataType.INTEGER;
            case INTEGER_ARRAY -> PersistentDataType.INTEGER_ARRAY;
            case LONG -> PersistentDataType.LONG;
            case LONG_ARRAY -> PersistentDataType.LONG_ARRAY;
            case SHORT -> PersistentDataType.SHORT;
            case STRING -> PersistentDataType.STRING;
            case TAG_CONTAINER -> PersistentDataType.TAG_CONTAINER;
            default -> throw new IllegalArgumentException("Cannot resolve type: " + type);
        };
    }
}
