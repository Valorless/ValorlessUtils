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
        switch (type) {
            case BYTE:
                return PersistentDataType.BYTE;
            case BYTE_ARRAY:
                return PersistentDataType.BYTE_ARRAY;
            case DOUBLE:
                return PersistentDataType.DOUBLE;
            case FLOAT:
                return PersistentDataType.FLOAT;
            case INTEGER:
                return PersistentDataType.INTEGER;
            case INTEGER_ARRAY:
                return PersistentDataType.INTEGER_ARRAY;
            case LONG:
                return PersistentDataType.LONG;
            case LONG_ARRAY:
                return PersistentDataType.LONG_ARRAY;
            case SHORT:
                return PersistentDataType.SHORT;
            case STRING:
                return PersistentDataType.STRING;
            case TAG_CONTAINER:
                return PersistentDataType.TAG_CONTAINER;
            default:
                throw new IllegalArgumentException("Cannot resolve type: " + type);
        }
    }
}
