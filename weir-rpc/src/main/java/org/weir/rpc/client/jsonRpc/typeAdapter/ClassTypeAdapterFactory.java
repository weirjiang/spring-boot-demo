package org.weir.rpc.client.jsonRpc.typeAdapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

public class ClassTypeAdapterFactory implements TypeAdapterFactory {

	@SuppressWarnings("unchecked")
	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
	      if(!Class.class.isAssignableFrom(type.getRawType())) {
	            return null;
	        }
	        return (TypeAdapter<T>) new ClassTypeAdapter();
	}
}