/*
 * Implication.java
 *
 * Copyright (C) 2008  Pei Wang
 *
 * This file is part of Open-NARS.
 *
 * Open-NARS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Open-NARS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Open-NARS.  If not, see <http://www.gnu.org/licenses/>.
 */
package nars.language;

import java.util.*;

import nars.io.Symbols;
import nars.storage.Memory;

/**
 * A Statement about an Inheritance copula.
 */
public class Implication extends Statement {

    private int temporalOrder = CompoundTerm.ORDER_NONE;

    /**
     * Constructor with partial values, called by make
     * @param arg The component list of the term
     */
//    protected Implication(ArrayList<Term> arg) {
//        super(arg);
//    }

    private Implication(ArrayList<Term> arg, int order) {
        super(arg);
        temporalOrder = order;
    }

    /**
     * Constructor with full values, called by clone
     * @param n The name of the term
     * @param cs Component list
     * @param con Whether it is a constant term
     * @param i Syntactic complexity of the compound
     */
//    protected Implication(String n, ArrayList<Term> cs, boolean con, short i) {
//        super(n, cs, con, i);
//    }

    private Implication(String n, ArrayList<Term> cs, boolean con, short i, int order) {
        super(n, cs, con, i);
        temporalOrder = order;
    }

    /**
     * Clone an object
     * @return A new object
     */
    @Override
    public Object clone() {
        return new Implication(name, cloneList(components), isConstant(), complexity, temporalOrder);
    }

    /**
     * Try to make a new compound from two components. Called by the inference rules.
     * @param subject The first component
     * @param predicate The second component
     * @param memory Reference to the memory
     * @return A compound generated or a term it reduced to
     */
    public static Implication make(final Term subject, final Term predicate, final Memory memory) {
        return make(subject, predicate, CompoundTerm.ORDER_NONE, memory);
    }
    
    public static Implication make(final Term subject, final Term predicate, int temporalOrder, final Memory memory) {
        if ((subject == null) || (predicate == null)) {
            return null;
        }
        if ((subject instanceof Implication) || (subject instanceof Equivalence) || (predicate instanceof Equivalence)) {
            return null;
        }
        if (invalidStatement(subject, predicate)) {
            return null;
        }
        String copula;
        switch (temporalOrder) {
            case CompoundTerm.ORDER_FORWARD:
                copula = Symbols.Relation.IMPLICATION_AFTER.toString();
                break;
            case CompoundTerm.ORDER_CONCURRENT:
                copula = Symbols.Relation.IMPLICATION_WHEN.toString();
                break;
            case CompoundTerm.ORDER_BACKWARD:
                copula = Symbols.Relation.IMPLICATION_BEFORE.toString();
                break;
            default:
                copula = Symbols.Relation.IMPLICATION.toString();
        }                
        final String name = makeStatementName(subject, copula, predicate);
        final Term t = memory.nameToListedTerm(name);
        if (t != null) {            
            if (t.getClass()!=Implication.class) {                
                throw new RuntimeException("Implication.make"  + ": "+ name + " is not Implication; it is " + t.getClass().getSimpleName() + " = " + t.toString() );
            }
            return (Implication) t;
        }
        if (predicate instanceof Implication) {
            final Term oldCondition = ((Implication) predicate).getSubject();
            if ((oldCondition instanceof Conjunction) && ((Conjunction) oldCondition).containComponent(subject)) {
                return null;
            }
            final Term newCondition = Conjunction.make(subject, oldCondition, memory);
            return make(newCondition, ((Implication) predicate).getPredicate(), memory);
        } else {
            final ArrayList<Term> argument = argumentsToList(subject, predicate);
            return new Implication(argument, temporalOrder);
        }
    }

    /**
     * Get the operator of the term.
     * @return the operator of the term
     */
    @Override
    public String operator() {
        switch (temporalOrder) {
            case CompoundTerm.ORDER_FORWARD:
                return Symbols.Relation.IMPLICATION_AFTER.toString();
            case CompoundTerm.ORDER_CONCURRENT:
                return Symbols.Relation.IMPLICATION_WHEN.toString();
            case CompoundTerm.ORDER_BACKWARD:
                return Symbols.Relation.IMPLICATION_BEFORE.toString();
            default:
                return Symbols.Relation.IMPLICATION.toString();
        }
    }
    
    public int getTemporalOrder() {
        return temporalOrder;
    }
}
