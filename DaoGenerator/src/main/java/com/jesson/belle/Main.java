package com.jesson.belle;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by zhangdi on 14-3-7.
 */
public class Main {
    private static final String PACKAGE_NAME = "com.michael.wallpaper.dao.model";
    private static final int VERSION = 2;

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(VERSION, PACKAGE_NAME);

        generateBellesDao(schema);
        generateCollectedBelle(schema);
        generateSeries(schema);

        new DaoGenerator().generateAll(schema, "wallpaper/src/main/java/");
    }

    private static void generateBellesDao(Schema schema) {
        Entity entity = schema.addEntity("LocalBelle");
        entity.addLongProperty("id").notNull();
        entity.addLongProperty("time").notNull();
        entity.addIntProperty("type").notNull();
        entity.addStringProperty("desc");
        entity.addStringProperty("url").notNull();
        entity.addStringProperty("rawUrl");
        entity.setHasKeepSections(true);
    }

    private static void generateCollectedBelle(Schema schema) {
        Entity entity = schema.addEntity("CollectedBelle");
        entity.addStringProperty("url").notNull().primaryKey();
        entity.addLongProperty("time").notNull();
        entity.setHasKeepSections(true);
    }

    private static void generateSeries(Schema schema) {
        Entity entity = schema.addEntity("Series");
        entity.addIntProperty("type").notNull();
        entity.addStringProperty("title").notNull();
        entity.addStringProperty("category");
        entity.addIntProperty("property");
        entity.setHasKeepSections(true);
        entity.implementsSerializable();
    }
}
