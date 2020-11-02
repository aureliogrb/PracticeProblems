/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practice;

import static practice.Utilities.*;
import java.util.Comparator;

/**
 *
 * @author Aurelio
 */
public class Fraction implements Comparator<Fraction>, Comparable<Fraction> {

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (int) (this.numerator ^ (this.numerator >>> 32));
        hash = 19 * hash + (int) (this.denominator ^ (this.denominator >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final Fraction other = (Fraction) obj;

        if (this.numerator * other.denominator != other.numerator * this.denominator) {
            return false;
        }

        return true;
    }

    /**
     *
     * @param numerator
     * @param denominator
     */
    public Fraction(long numerator, long denominator) {
        if (denominator < 1) {
            throw new IllegalArgumentException("Denominator is expected to be above zero");
        }
        this.numerator = numerator;
        this.denominator = denominator;
    }

    private long numerator;
    private long denominator;

    /**
     * Converts fraction to their simplest form. e.g. converts 2/4 to 1/2;
     */
    public void reduce() {
        long gcd = greatestCommonDivisor(numerator < 0 ? -1 * numerator : numerator, denominator);
        if (gcd > 1) {
            numerator /= gcd;
            denominator /= gcd;
        }
    }

    /**
     * @return the numerator
     */
    public long getNumerator() {
        return numerator;
    }

    /**
     * @param numerator the numerator to set
     */
    public void setNumerator(long numerator) {
        this.numerator = numerator;
    }

    /**
     * @return the denominator
     */
    public long getDenominator() {
        return denominator;
    }

    /**
     * @param denominator the denominator to set
     */
    public void setDenominator(long denominator) {
        if (denominator < 1) {
            throw new IllegalArgumentException("Denominator is expected to be above zero");
        }
        this.denominator = denominator;
    }

    @Override
    public int compare(Fraction o1, Fraction o2) {
        if (o1.getNumerator() * o2.getDenominator() < o2.getNumerator() * o1.getDenominator()) return -1;
        if (o1.getNumerator() * o2.getDenominator() == o2.getNumerator() * o1.getDenominator()) return 0;
        else return 1;
        
    }

    @Override
    public int compareTo(Fraction o) {
        return compare(this, o);
    }

    /**
     *
     * @return
     */
    public boolean isProperFraction() {
        return Math.abs(numerator) < denominator;
    }
    
    /**
     *
     * @param a The fraction that will be added to this one.
     * @return A new fraction that represents the sum of the given fraction and this fraction
     */
    public Fraction add(Fraction a) {
        Fraction retVal = new Fraction (this.numerator * a.denominator + a.numerator * this.denominator, this.denominator * a.denominator);
        retVal.reduce();
        return retVal;
    }  
    
}
