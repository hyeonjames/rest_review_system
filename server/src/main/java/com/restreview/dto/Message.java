package com.restreview.dto;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import org.springframework.data.domain.Page;

import com.restreview.etc.Mappable;

public class Message extends HashMap<String, Object> {
	private static final long serialVersionUID = 2279478139682501809L;
	public Message(boolean success, Object data) {
		put("error", !success);
		put("data",data);
	}
	public Message(boolean success) {
		put("error", !success);
	}
	
	public static Message success() {
		return new Message(true);
	}
	public static Message success(Object data) {
		return new Message(true, data);
	}
	public static Message dto(Mappable m) {
		Object obj = m.mapping();
		return new Message(true, obj);
	}
	public static Message failure(Object message) {
		return new Message(false,message);
	}
	public static Message listRaw(Page<?> pg) {

		HashMap<String,Object> data = new HashMap<String,Object>();
		
		data.put("totalElements", pg.getTotalElements());
		data.put("content", pg.getContent());
		data.put("currentPage",pg.getPageable().getPageNumber());
		data.put("totalPage",Math.max(1, pg.getTotalPages()));
		data.put("countPerPage",pg.getPageable().getPageSize());
		return new Message(true, data);
	}
	public static Message list(Page<? extends Mappable> pg) {
		return Message.listRaw(pg.map( obj -> obj.mapping() ));
	}
	public static Message list(Collection<?> obj) {
		return new Message(true, obj);
	}
	public static Message dto(Collection<? extends Mappable> li) {
		Collection<Object> col = new LinkedList<Object>();
		for(Mappable d : li) {
			col.add(d.mapping());
		}
		return new Message(true, col);
	}
}
