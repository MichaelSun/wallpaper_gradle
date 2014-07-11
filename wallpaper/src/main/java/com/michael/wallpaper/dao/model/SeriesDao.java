package com.michael.wallpaper.dao.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.michael.wallpaper.dao.model.Series;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SERIES.
*/
public class SeriesDao extends AbstractDao<Series, Void> {

    public static final String TABLENAME = "SERIES";

    /**
     * Properties of entity Series.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Type = new Property(0, int.class, "type", false, "TYPE");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Category = new Property(2, String.class, "category", false, "CATEGORY");
        public final static Property Tag3 = new Property(3, String.class, "tag3", false, "TAG3");
        public final static Property Property = new Property(4, Integer.class, "property", false, "PROPERTY");
    };


    public SeriesDao(DaoConfig config) {
        super(config);
    }
    
    public SeriesDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SERIES' (" + //
                "'TYPE' INTEGER NOT NULL ," + // 0: type
                "'TITLE' TEXT NOT NULL ," + // 1: title
                "'CATEGORY' TEXT," + // 2: category
                "'TAG3' TEXT," + // 3: tag3
                "'PROPERTY' INTEGER);"); // 4: property
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SERIES'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Series entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getType());
        stmt.bindString(2, entity.getTitle());
 
        String category = entity.getCategory();
        if (category != null) {
            stmt.bindString(3, category);
        }
 
        String tag3 = entity.getTag3();
        if (tag3 != null) {
            stmt.bindString(4, tag3);
        }
 
        Integer property = entity.getProperty();
        if (property != null) {
            stmt.bindLong(5, property);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public Series readEntity(Cursor cursor, int offset) {
        Series entity = new Series( //
            cursor.getInt(offset + 0), // type
            cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // category
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // tag3
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4) // property
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Series entity, int offset) {
        entity.setType(cursor.getInt(offset + 0));
        entity.setTitle(cursor.getString(offset + 1));
        entity.setCategory(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTag3(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setProperty(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(Series entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(Series entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
