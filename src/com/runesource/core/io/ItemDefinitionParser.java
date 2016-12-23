package com.runesource.core.io;

import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.runesource.core.world.model.entity.item.ItemDefinition;
import com.runesource.util.GsonParser;

public final class ItemDefinitionParser extends GsonParser<ItemDefinition> {

	public ItemDefinitionParser(Collection<ItemDefinition> collection) {
		super(collection, "./data/definitions/item_definitions.json");
	}

	@Override
	public void parse(JsonObject object, Collection<ItemDefinition> collection) {
		Gson gson = new GsonBuilder().create();
        int id = object.get("id").getAsInt();
        String name = object.get("name").getAsString();
        String examine = object.get("examine").getAsString();
        boolean stackable = object.get("stackable").getAsBoolean();
        int baseValue = object.get("base_value").getAsInt();
        //int specialValue = object.get("special_value").getAsInt();
        int notedId = object.get("noted_id").getAsInt();
        int unnotedId = object.get("unnoted_id").getAsInt();
        boolean membersOnly = object.get("members_only").getAsBoolean();
        double weight = object.get("weight").getAsDouble();
        boolean tradable = object.get("tradeable").getAsBoolean();
        String[] inventoryActions = gson.fromJson(object.get("inventory_actions"), String[].class);
        String[] groundActions = gson.fromJson(object.get("ground_actions"), String[].class);
        collection.add(new ItemDefinition(id, name, examine, stackable, baseValue, notedId, unnotedId, membersOnly, weight, tradable, inventoryActions, groundActions));
	}

}
