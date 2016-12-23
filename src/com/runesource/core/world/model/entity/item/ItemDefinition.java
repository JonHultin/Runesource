package com.runesource.core.world.model.entity.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;

import com.runesource.core.io.ItemDefinitionParser;

public final class ItemDefinition {

    private static final List<ItemDefinition> DEFINITIONS = new ArrayList<>();

    public static ItemDefinition get(int id) {
        ItemDefinition def = DEFINITIONS.get(id);
        return def;
    }

    public static Iterable<ItemDefinition> all() {
        return DEFINITIONS;
    }

    public static String computeNameForId(int id) {
        return get(id).getName();
    }

    static { 
        ItemDefinitionParser parser = new ItemDefinitionParser(DEFINITIONS);
        parser.load();
    }

    private final int id;

    private final String name;

    private final String examine;

    private final boolean stackable;

    private final int value;

    private final OptionalInt notedId;

    private final OptionalInt unnotedId;

    private final boolean membersOnly;

    private final double weight;

    private final boolean tradeable;

    private final List<String> inventoryActions;

    private final List<String> groundActions;

    public ItemDefinition(int id, String name, String examine, boolean stackable, int value, int notedId,
        int unnotedId, boolean membersOnly, double weight, boolean tradeable, String[] inventoryActions,
        String[] groundActions) {
        this.id = id;
        this.name = name;
        this.examine = examine;
        this.stackable = stackable;
        this.value = value;
        this.notedId = notedId == -1 ? OptionalInt.empty() : OptionalInt.of(notedId);
        this.unnotedId = unnotedId == -1 ? OptionalInt.empty() : OptionalInt.of(unnotedId);
        this.membersOnly = membersOnly;
        this.weight = weight;
        this.tradeable = tradeable;
        this.inventoryActions = Collections.unmodifiableList(Arrays.asList(inventoryActions));
        this.groundActions = Collections.unmodifiableList(Arrays.asList(groundActions));
    }

    public boolean hasInventoryAction(String action) {
        return inventoryActions.contains(action);
    }

    public boolean hasGroundAction(String action) {
        return groundActions.contains(action);
    }

    public boolean isNoteable() {
        return notedId.isPresent();
    }

    public boolean isNoted() {
        return unnotedId.isPresent();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExamine() {
        return examine;
    }

    public boolean isStackable() {
        return stackable;
    }

    public int getValue() {
        return value;
    }

    public OptionalInt getNotedId() {
        return notedId;
    }

    public OptionalInt getUnnotedId() {
        return unnotedId;
    }

    public boolean isMembersOnly() {
        return membersOnly;
    }

    public double getWeight() {
        return weight;
    }

    public boolean isTradeable() {
        return tradeable;
    }

    public List<String> getInventoryActions() {
        return inventoryActions;
    }

    public Collection<String> getGroundActions() {
        return groundActions;
    }
    
}
