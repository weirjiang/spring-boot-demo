package hash;

import java.util.Map;

public interface HashMapper<T, K, V> {

	Map<K, V> toHash(T object);

	T fromHash(Map<K, V> hash);
}
