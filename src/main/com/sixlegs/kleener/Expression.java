package com.sixlegs.kleener;

import java.util.*;

abstract public class Expression
{
    abstract protected void split(List<CharSet> csets);
    abstract protected NFA getNFA(List<CharSet> csets);

//     public static Expression parse(String pattern) {
//         // TODO
//         return null;
//     }

    public NFA getNFA() {
        List<CharSet> csets = new ArrayList<CharSet>();
        split(csets);
        System.err.println(Misc.format(csets));
        return getNFA(csets);
    }

    /*
      0, 0 -> opt(loop(e))
      0, 1 -> loop(e)
      n, n  *build*
      n, 0 -> cat(rep(e, n-1, n-1), loop(e))
      0, n -> rep(opt(e, n, n))
      m, n -> cat(rep(e, m, m), rep(e, 0, n-m))
    */
    static Expression repeat(Expression e1, int min, int max) {
        if (min < 0 || max < 0 || (max < min && max != 0))
            throw new IllegalArgumentException("min=" + min + " max=" + max);
        if (max == 0) {
            if (min == 0) {
                return new OptionalExp(new LoopExp(e1));
            } else if (min == 1) {
                return new LoopExp(e1);
            } else {
                return new CatExp(repeat(e1, min - 1, min - 1), new LoopExp(e1));
            }
        } else if (max == min) {
            Expression temp = e1;
            for (int i = 1; i < min; i++) 
                temp = new CatExp(temp, e1);
            return temp;
        } else if (min > 0) {
            return new CatExp(repeat(e1, min, min), repeat(e1, 0, max - min));
        } else {
            return repeat(new OptionalExp(e1), max, max);
        }
    }

    protected static void split(List<CharSet> csets, CharSet cset) {
        CharSet copy = new CharSet(cset);
        if (csets.isEmpty()) {
            csets.add(copy);
        } else {
            List<CharSet> temp = new ArrayList<CharSet>();
            for (Iterator<CharSet> it = csets.iterator(); it.hasNext();) {
                CharSet oldSet = it.next();
                CharSet newSet = oldSet.intersect(copy);
                if (oldSet.isEmpty())
                    it.remove();
                if (!newSet.isEmpty())
                    temp.add(newSet);
            }
            if (!copy.isEmpty())
                temp.add(copy);
            csets.addAll(temp);
        }
    }

    protected String wrap(Expression e) {
        return "(" + e + ")";
    }

//     protected boolean isFixedLength() {
//         return false;
//     }

//     protected int getLength() {
//         return 0;
//     }

//     NFA getNFA() throws DriverLexer.DriverException {
//         return getNFA(new EquivMap(this));
//     }
}
