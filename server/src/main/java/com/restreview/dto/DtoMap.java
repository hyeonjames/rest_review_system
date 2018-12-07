package com.restreview.dto;

import java.util.HashMap;

public class DtoMap extends HashMap<String,Object> {
	
	public DtoMap() {}
	
	public DtoMap set(String key, Object obj) {
		put(key, obj);
		return this;
	}
}
