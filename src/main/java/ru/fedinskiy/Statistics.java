package ru.fedinskiy;

import io.vertx.core.json.JsonObject;

public class Statistics {
	final int totalCount;
	final int letterCount;
	final String source;

	public Statistics(int totalCount, int letterCount, String source) {
		this.totalCount = totalCount;
		this.letterCount = letterCount;
		this.source = source;
	}

	public JsonObject putToJson(JsonObject json) {
		return json
				.put("was", source)
				.put("letters", Integer.toString(letterCount))
				.put("length", Integer.toString(totalCount));
	}

	public static Statistics empty() {
		return new Statistics(0, 0, "");
	}

	public static Statistics fromString(String source) {
		int totalCount=0;
		int letterCount=0;
		boolean hadCarriageReturn=false;
		for (char c : source.toCharArray()) {
			if(!(hadCarriageReturn&&c=='\n')) {
				totalCount+=1;
			}
			if(!Character.isWhitespace(c)){
				letterCount+=1;
			}
			hadCarriageReturn=c=='\r';
		}
		return new Statistics(totalCount, letterCount, source);
	}
}
