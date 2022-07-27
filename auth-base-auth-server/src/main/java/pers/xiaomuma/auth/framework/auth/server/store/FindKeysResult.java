package pers.xiaomuma.auth.framework.auth.server.store;

import lombok.Getter;

import java.util.List;

public class FindKeysResult {
	@Getter
	private List<String> accessKeys;
	@Getter
	private int count;

	public FindKeysResult (List<String> accessKeys, int count) {
		this.accessKeys = accessKeys;
		this.count =  count;
	}
}
