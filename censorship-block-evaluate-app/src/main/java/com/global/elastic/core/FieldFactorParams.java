package com.global.elastic.core;

import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction.Modifier;

/**
 * description goes here.
 * <P>
 *
 * @author Sopra Steria.
 * @copyright � Airbus 2017. All rights reserved.
 */
public class FieldFactorParams {

    /**
     * The field factory
     */
    private String factorField;

    /**
     * Factor impact on field weight
     */
    private Integer factor;

    /**
     * Modifier value "none", "log", "log1p", "log2p", "ln", "ln1p", "square", "sqrt", "reciprocal"
     */
    private Modifier modifier;

    /**
     * Default constructor.
     * 
     * @param factorField the field factory
     * @param factor the factor field weight
     * @param modifier the modifier value
     * 
     */
    public FieldFactorParams(String factorField, Integer factor, Modifier modifier) {
        this.factorField = factorField;
        this.factor = factor;
        this.modifier = modifier;
    }

    /**
     * Getter for property factorField.
     * 
     * @return Value of property factorField.
     */
    public String getFactorField() {
        return factorField;
    }

    /**
     * Setter for property factorField.
     * 
     * @param factorField New value of property factorField.
     */
    public void setFactorField(String factorField) {
        this.factorField = factorField;
    }

    /**
     * Getter for property factor.
     * 
     * @return Value of property factor.
     */
    public Integer getFactor() {
        return factor;
    }

    /**
     * Setter for property factor.
     * 
     * @param factor New value of property factor.
     */
    public void setFactor(Integer factor) {
        this.factor = factor;
    }

    /**
     * Getter for property modifier.
     * 
     * @return Value of property modifier.
     */
    public Modifier getModifier() {
        return modifier;
    }

    /**
     * Setter for property modifier.
     * 
     * @param modifier New value of property modifier.
     */
    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

}
