/*
 * This file is generated by jOOQ.
 */
package com.ssafy.jooq.tables;


import com.ssafy.jooq.FluffitFlupet;
import com.ssafy.jooq.Keys;
import com.ssafy.jooq.tables.records.FoodTypeRecord;

import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function8;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row8;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.jooq.types.UInteger;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FoodType extends TableImpl<FoodTypeRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>fluffit_flupet.food_type</code>
     */
    public static final FoodType FOOD_TYPE = new FoodType();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<FoodTypeRecord> getRecordType() {
        return FoodTypeRecord.class;
    }

    /**
     * The column <code>fluffit_flupet.food_type.id</code>.
     */
    public final TableField<FoodTypeRecord, UInteger> ID = createField(DSL.name("id"), SQLDataType.INTEGERUNSIGNED.nullable(false).identity(true), this, "");

    /**
     * The column <code>fluffit_flupet.food_type.name</code>.
     */
    public final TableField<FoodTypeRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(10).nullable(false), this, "");

    /**
     * The column <code>fluffit_flupet.food_type.img_url</code>.
     */
    public final TableField<FoodTypeRecord, String> IMG_URL = createField(DSL.name("img_url"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>fluffit_flupet.food_type.fullness_effect</code>.
     */
    public final TableField<FoodTypeRecord, Integer> FULLNESS_EFFECT = createField(DSL.name("fullness_effect"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>fluffit_flupet.food_type.health_effect</code>.
     */
    public final TableField<FoodTypeRecord, Integer> HEALTH_EFFECT = createField(DSL.name("health_effect"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>fluffit_flupet.food_type.price</code>.
     */
    public final TableField<FoodTypeRecord, Integer> PRICE = createField(DSL.name("price"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>fluffit_flupet.food_type.stock</code>.
     */
    public final TableField<FoodTypeRecord, Integer> STOCK = createField(DSL.name("stock"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field("30", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>fluffit_flupet.food_type.description</code>.
     */
    public final TableField<FoodTypeRecord, String> DESCRIPTION = createField(DSL.name("description"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    private FoodType(Name alias, Table<FoodTypeRecord> aliased) {
        this(alias, aliased, null);
    }

    private FoodType(Name alias, Table<FoodTypeRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>fluffit_flupet.food_type</code> table reference
     */
    public FoodType(String alias) {
        this(DSL.name(alias), FOOD_TYPE);
    }

    /**
     * Create an aliased <code>fluffit_flupet.food_type</code> table reference
     */
    public FoodType(Name alias) {
        this(alias, FOOD_TYPE);
    }

    /**
     * Create a <code>fluffit_flupet.food_type</code> table reference
     */
    public FoodType() {
        this(DSL.name("food_type"), null);
    }

    public <O extends Record> FoodType(Table<O> child, ForeignKey<O, FoodTypeRecord> key) {
        super(child, key, FOOD_TYPE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : FluffitFlupet.FLUFFIT_FLUPET;
    }

    @Override
    public Identity<FoodTypeRecord, UInteger> getIdentity() {
        return (Identity<FoodTypeRecord, UInteger>) super.getIdentity();
    }

    @Override
    public UniqueKey<FoodTypeRecord> getPrimaryKey() {
        return Keys.KEY_FOOD_TYPE_PRIMARY;
    }

    @Override
    public FoodType as(String alias) {
        return new FoodType(DSL.name(alias), this);
    }

    @Override
    public FoodType as(Name alias) {
        return new FoodType(alias, this);
    }

    @Override
    public FoodType as(Table<?> alias) {
        return new FoodType(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public FoodType rename(String name) {
        return new FoodType(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public FoodType rename(Name name) {
        return new FoodType(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public FoodType rename(Table<?> name) {
        return new FoodType(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row8 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row8<UInteger, String, String, Integer, Integer, Integer, Integer, String> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function8<? super UInteger, ? super String, ? super String, ? super Integer, ? super Integer, ? super Integer, ? super Integer, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function8<? super UInteger, ? super String, ? super String, ? super Integer, ? super Integer, ? super Integer, ? super Integer, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
