package com.jonjauhari.catalog.model;

import java.math.BigDecimal;

/**
 * An entity that can be valued
 */
public interface Appraisable {

    /**
     * Estimate a price for this object
     * @return a monetary valuation for this object
     */
    BigDecimal appraise();
}
