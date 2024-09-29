/*
 * Status associated to user
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.games.gods;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import online.nostrium.apps.games.gods.items.GODS_item;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.user.User;
import online.nostrium.utils.GsonUtils;
import online.nostrium.utils.JsonTextFile;
import org.apache.commons.io.FileUtils;

/**
 * @author Brito
 * @date: 2024-08-21
 * @location: Germany
 */
public class GODS_Status {
    
    final TerminalGODS app;
    
    @Expose
    int level = 1;
    
    @Expose
    int defense = 1;
    
    @Expose
    int attack = 1;
    
    @Expose
    int luck = 1;
    
    @Expose
    int armour = 1;
    
    @Expose
    int weapon = 1;
    
    
    @SuppressWarnings("unchecked")
    @Expose
    ArrayList<GODS_item> items = new ArrayList();
    

    public GODS_Status(TerminalGODS app) {
        this.app = app;
    }
    
    public static GODS_Status loadFromFile(TerminalGODS app) {
        User user = app.session.getUser();
        File folder = user.getFolder(false);
        File file = new File(folder, GODS_definitions.jsonStatusFilename);
        
        if (!file.exists() || file.isDirectory() || file.length() == 0) {
            return new GODS_Status(app);
        }
        try {
            String text = FileUtils.readFileToString(file, "UTF-8");
            Gson gson = GsonUtils.createGson(); // Use the custom Gson instance
            return gson.fromJson(text, GODS_Status.class);
        } catch (JsonSyntaxException | IOException e) {
            Logger.getLogger(JsonTextFile.class.getName()).log(Level.SEVERE, null, e);
            return new GODS_Status(app);
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getLuck() {
        return luck;
    }

    public void setLuck(int luck) {
        this.luck = luck;
    }

    public int getArmour() {
        return armour;
    }

    public void setArmour(int armour) {
        this.armour = armour;
    }

    public int getWeapon() {
        return weapon;
    }

    public void setWeapon(int weapon) {
        this.weapon = weapon;
    }

    public ArrayList<GODS_item> getItems() {
        return items;
    }


}
