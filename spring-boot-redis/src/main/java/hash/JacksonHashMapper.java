package hash;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

public class JacksonHashMapper<T> implements HashMapper<T, String, Object> {

	private final ObjectMapper mapper;
	private final JavaType userType;
	private final JavaType mapType = TypeFactory.mapType(Map.class, String.class, Object.class);

	public JacksonHashMapper(Class<T> type) {
		this(type, new ObjectMapper());
	}

	public JacksonHashMapper(Class<T> type, ObjectMapper mapper) {
		this.mapper = mapper;
		this.userType = TypeFactory.type(type);
	}

	@SuppressWarnings("unchecked")
	
	public T fromHash(Map<String, Object> hash) {
		return (T) mapper.convertValue(hash, userType);
	}

	
	public Map<String, Object> toHash(T object) {
		return mapper.convertValue(object, mapType);
	}
}