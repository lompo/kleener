package com.sixlegs.kleener;

import java.util.*;

abstract public class Expression
{
    abstract protected NFA getNFA();

//     public static Expression parse(String pattern) {
//         // TODO
//         return null;
//     }

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

    protected Collection<CharSet> getCharSets() {
        return Collections.emptySet();
    }

    protected String wrap(Expression e) {
        return "(" + e + ")";
    }
}
