/*
 * This file is generated by jOOQ.
 */
package com.ssafy.jooq.tables.records;


import com.ssafy.jooq.tables.FoodType;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;
import org.jooq.types.UInteger;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FoodTypeRecord extends UpdatableRecordImpl<FoodTypeRecord> implements Record8<UInteger, String, String, Integer, Integer, Integer, Integer, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>fluffit_flupet.food_type.id</code>.
     */
    public void setId(UInteger value) {
        set(0, value);
    }

    /**
     * Getter for <code>fluffit_flupet.food_type.id</code>.
     */
    public UInteger getId() {
        return (UInteger) get(0);
    }

    /**
     * Setter for <code>fluffit_flupet.food_type.name</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>fluffit_flupet.food_type.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>fluffit_flupet.food_type.img_url</code>.
     */
    public void setImgUrl(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>fluffit_flupet.food_type.img_url</code>.
     */
    public String getImgUrl() {
        return (String) get(2);
    }

    /**
     * Setter for <code>fluffit_flupet.food_type.fullness_effect</code>.
     */
    public void setFullnessEffect(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>fluffit_flupet.food_type.fullness_effect</code>.
     */
    public Integer getFullnessEffect() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>fluffit_flupet.food_type.health_effect</code>.
     */
    public void setHealthEffect(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>fluffit_flupet.food_type.health_effect</code>.
     */
    public Integer getHealthEffect() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>fluffit_flupet.food_type.price</code>.
     */
    public void setPrice(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>fluffit_flupet.food_type.price</code>.
     */
    public Integer getPrice() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>fluffit_flupet.food_type.stock</code>.
     */
    public void setStock(Integer value) {
        set(6, value);
    }

    /**
     * Getter for <code>fluffit_flupet.food_type.stock</code>.
     */
    public Integer getStock() {
        return (Integer) get(6);
    }

    /**
     * Setter for <code>fluffit_flupet.food_type.description</code>.
     */
    public void setDescription(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>fluffit_flupet.food_type.description</code>.
     */
    public String getDescription() {
        return (String) get(7);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UInteger> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row8<UInteger, String, String, Integer, Integer, Integer, Integer, String> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    @Override
    public Row8<UInteger, String, String, Integer, Integer, Integer, Integer, String> valuesRow() {
        return (Row8) super.valuesRow();
    }

    @Override
    public Field<UInteger> field1() {
        return FoodType.FOOD_TYPE.ID;
    }

    @Override
    public Field<String> field2() {
        return FoodType.FOOD_TYPE.NAME;
    }

    @Override
    public Field<String> field3() {
        return FoodType.FOOD_TYPE.IMG_URL;
    }

    @Override
    public Field<Integer> field4() {
        return FoodType.FOOD_TYPE.FULLNESS_EFFECT;
    }

    @Override
    public Field<Integer> field5() {
        return FoodType.FOOD_TYPE.HEALTH_EFFECT;
    }

    @Override
    public Field<Integer> field6() {
        return FoodType.FOOD_TYPE.PRICE;
    }

    @Override
    public Field<Integer> field7() {
        return FoodType.FOOD_TYPE.STOCK;
    }

    @Override
    public Field<String> field8() {
        return FoodType.FOOD_TYPE.DESCRIPTION;
    }

    @Override
    public UInteger component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public String component3() {
        return getImgUrl();
    }

    @Override
    public Integer component4() {
        return getFullnessEffect();
    }

    @Override
    public Integer component5() {
        return getHealthEffect();
    }

    @Override
    public Integer component6() {
        return getPrice();
    }

    @Override
    public Integer component7() {
        return getStock();
    }

    @Override
    public String component8() {
        return getDescription();
    }

    @Override
    public UInteger value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public String value3() {
        return getImgUrl();
    }

    @Override
    public Integer value4() {
        return getFullnessEffect();
    }

    @Override
    public Integer value5() {
        return getHealthEffect();
    }

    @Override
    public Integer value6() {
        return getPrice();
    }

    @Override
    public Integer value7() {
        return getStock();
    }

    @Override
    public String value8() {
        return getDescription();
    }

    @Override
    public FoodTypeRecord value1(UInteger value) {
        setId(value);
        return this;
    }

    @Override
    public FoodTypeRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public FoodTypeRecord value3(String value) {
        setImgUrl(value);
        return this;
    }

    @Override
    public FoodTypeRecord value4(Integer value) {
        setFullnessEffect(value);
        return this;
    }

    @Override
    public FoodTypeRecord value5(Integer value) {
        setHealthEffect(value);
        return this;
    }

    @Override
    public FoodTypeRecord value6(Integer value) {
        setPrice(value);
        return this;
    }

    @Override
    public FoodTypeRecord value7(Integer value) {
        setStock(value);
        return this;
    }

    @Override
    public FoodTypeRecord value8(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public FoodTypeRecord values(UInteger value1, String value2, String value3, Integer value4, Integer value5, Integer value6, Integer value7, String value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached FoodTypeRecord
     */
    public FoodTypeRecord() {
        super(FoodType.FOOD_TYPE);
    }

    /**
     * Create a detached, initialised FoodTypeRecord
     */
    public FoodTypeRecord(UInteger id, String name, String imgUrl, Integer fullnessEffect, Integer healthEffect, Integer price, Integer stock, String description) {
        super(FoodType.FOOD_TYPE);

        setId(id);
        setName(name);
        setImgUrl(imgUrl);
        setFullnessEffect(fullnessEffect);
        setHealthEffect(healthEffect);
        setPrice(price);
        setStock(stock);
        setDescription(description);
    }
}
