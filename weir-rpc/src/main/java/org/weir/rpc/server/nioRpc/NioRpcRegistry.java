package org.weir.rpc.server.nioRpc;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class NioRpcRegistry {
	private Map<String, NioRpcApi> registry = new ConcurrentHashMap<String, NioRpcApi>();

	/**
	 * Register a rpc api.
	 * 
	 * @param api
	 */
	void register(NioRpcApi api) {
		registry.put(api.getKey(), api);
	}

	/**
	 * Unregister a rpc api.
	 * 
	 * @param api
	 */
	void unregister(NioRpcApi api) {
		registry.remove(api.getKey());
	}

	/**
	 * Lookup a rpc api.
	 * 
	 * @param api
	 *            query api object
	 * @return result rpc api.
	 */
	NioRpcApi lookup(NioRpcApi api) {
		return registry.get(api.getKey());
	}

	/**
	 * @return all registered rpc apis.
	 */
	Set<NioRpcApi> apis() {
		Set<NioRpcApi> apis = new TreeSet<NioRpcApi>(registry.values());
		return Collections.unmodifiableSet(apis);
	}
}
