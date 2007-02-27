package com.sixlegs.kleener;

// immutable
abstract public class CharSet
{
    public static final int UNKNOWN_CARDINALITY = Character.MAX_CODE_POINT + 1;
    
    abstract public boolean contains(int c);

    // TODO: use iterator instead? (better for charset builder)
    abstract public int nextChar(int c);

    abstract public CharSet intersect(CharSet cset);
    abstract public CharSet subtract(CharSet cset);
    abstract public int cardinality(); // upper bound
    abstract public boolean isEmpty();

    @Override public String toString() {
        int count = cardinality();
        if (count > 20) {
            // TODO: use ranges or something
            return count + " chars";
        }
        StringBuilder sb = new StringBuilder();
        for (int c = nextChar(0); c >= 0; c = nextChar(c + 1))
            sb.appendCodePoint(c);
        return Misc.escapeStringLiteral(sb.toString());
    }

    @Override public boolean equals(Object o) {
        if (!(o instanceof CharSet))
            return false;
        CharSet cset = (CharSet)o;
        if (isEmpty())
            return cset.isEmpty();
        if (cset.isEmpty())
            return false;
        if (cardinality() != cset.cardinality())
            return false;
        int c1 = nextChar(0);
        int c2 = cset.nextChar(0);
        while (c1 >= 0 && c1 == c2) {
            c1 = nextChar(c1 + 1);
            c2 = nextChar(c2 + 1);
        }
        return c1 != c2;
    }

    @Override public int hashCode() {
        // TODO: improve this
        return nextChar(0);
    }
}
