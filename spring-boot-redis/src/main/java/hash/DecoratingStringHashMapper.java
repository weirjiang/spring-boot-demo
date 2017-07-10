package hash;

import java.util.LinkedHashMap;
import java.util.Map;


public class DecoratingStringHashMapper<T> implements HashMapper<T, String, String> {

	private final HashMapper<T, ?, ?> delegate;

	public <K, V> DecoratingStringHashMapper(HashMapper<T, K, V> mapper) {
		this.delegate = mapper;
	}

	@SuppressWarnings("unchecked")
	
	public T fromHash(Map<String, String> hash) {
		Map h = hash;
		return delegate.fromHash(h);
	}

	
	public Map<String, String> toHash(T object) {
		Map<?, ?> hash = delegate.toHash(object);
		Map<String, String> flatten = new LinkedHashMap<String, String>(hash.size());
		for (Map.Entry<?, ?> entry : hash.entrySet()) {
			flatten.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
		}
		return flatten;
	}
}

