/*
 * Items provided to user
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.servers.apps.games.gods.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Brito
 * @date: 2024-08-21
 * @location: Germany
 */
public abstract class GODS_item {

    @Expose
    final String id;
    
    @Expose
    final String name;
    
    @Expose
    final String descriptionOneLine;

    @Expose
    final GODS_ItemType itemType;

    @Expose
    public boolean active = true;
    
    @Expose
    final int changeLevel;
    
    @Expose
    final int boostPercentageDefense;
    
    @Expose
    final int boostPercentageAttack;
    
    @Expose
    final String art;

    
    

    public GODS_item(String id, String name, String descriptionOneLine, 
            int changeLevel, int boostPercentageDefense, 
            int boostPercentageAttack, GODS_ItemType itemType, String art) {
        this.id = id;
        this.name = name;
        this.art = art;
        this.itemType = itemType;
        this.descriptionOneLine = descriptionOneLine;
        this.changeLevel = changeLevel;
        this.boostPercentageDefense = boostPercentageDefense;
        this.boostPercentageAttack = boostPercentageAttack;
    }
    
    /**
     * Export this object as a JSON string.
     *
     * @return JSON string or null if something went wrong
     */
    public String jsonExport() {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .setPrettyPrinting();
            Gson gson = gsonBuilder.create();
            return gson.toJson(this);
        } catch (Exception e) {
            Logger.getLogger(GODS_item.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
    
    

    public GODS_ItemType getItemType() {
        return itemType;
    }

    
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescriptionOneLine() {
        return descriptionOneLine;
    }

    public boolean isActive() {
        return active;
    }

    public int getChangeLevel() {
        return changeLevel;
    }

    public int getBoostPercentageDefense() {
        return boostPercentageDefense;
    }

    public int getBoostPercentageAttack() {
        return boostPercentageAttack;
    }

    public String getArt() {
        return art;
    }
    
    
}
