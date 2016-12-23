package com.runesource.util;

import java.io.File;
import java.io.FileReader;
import java.util.Collection;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class GsonParser<T> {

	private final Collection<T> collection;
	
	private final String path;

	public GsonParser(Collection<T> collection, String path) {
		this.collection = collection;
		this.path = path;
	}

	public final void load() {
		try (FileReader reader = new FileReader(new File(path))) {
			JsonParser parser = new JsonParser();
			JsonArray array = (JsonArray) parser.parse(reader);
			for (int index = 0; index < array.size(); index++) {
				JsonObject data = (JsonObject) array.get(index);
				parse(data, collection);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public abstract void parse(JsonObject object, Collection<T> collection);
}
