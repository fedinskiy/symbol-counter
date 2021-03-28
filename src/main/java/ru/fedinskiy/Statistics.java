package ru.fedinskiy;

import io.vertx.core.json.JsonObject;

public class Statistics {
	private final int totalCount;
	private final String source;

	public Statistics(int totalCount, String source) {
		this.totalCount = totalCount;
		this.source = source;
	}

	public JsonObject putToJson(JsonObject json) {
		return json
				.put("was", source)
				.put("length", Integer.toString(totalCount));
	}

	public static Statistics empty() {
		return new Statistics(0,"");
	}


	public static Statistics fromString(String source) {
		final int length = source.length();
		return new Statistics(length, source);
	}
}
