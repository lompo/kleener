package com.sixlegs.kleener;

abstract class GenericCharSet extends CharSet
{
    private Boolean empty; 
    
    public int cardinality() {
        // TODO: count and cache?
        return UNKNOWN_CARDINALITY;
    }
    
    public boolean isEmpty() {
        if (empty == null)
            empty = Boolean.valueOf(nextChar(0) >= 0);
        return empty.booleanValue();
    }

    public CharSet intersect(CharSet cset) {
        if (cset.isEmpty())
            return cset;
        return new IntersectCharSet(this, cset);
    }
    
    public CharSet subtract(CharSet cset) {
        if (cset.isEmpty())
            return this;
        return new SubtractCharSet(this, cset);
    }

    private static class IntersectCharSet extends GenericCharSet
    {
        private final CharSet cset1;
        private final CharSet cset2;

        public IntersectCharSet(CharSet cset1, CharSet cset2) {
            this.cset1 = cset1;
            this.cset2 = cset2;
        }
        
        public boolean contains(int c) {
            return cset1.contains(c) && cset2.contains(c);
        }
        
        public int nextChar(int c) {
            for (;;) {
                int n1 = cset1.nextChar(c);
                int n2 = cset2.nextChar(c);
                if (n1 == n2)
                    return n1;
                c = Math.max(n1, n2) + 1;
            }
        }
    }

    private static class SubtractCharSet extends GenericCharSet
    {
        private final CharSet cset1;
        private final CharSet cset2;

        public SubtractCharSet(CharSet cset1, CharSet cset2) {
            this.cset1 = cset1;
            this.cset2 = cset2;
        }
        
        public boolean contains(int c) {
            return cset1.contains(c) && !cset2.contains(c);
        }
        
        public int nextChar(int c) {
            for (;;) {
                c = cset1.nextChar(c);
                if (!cset2.contains(c))
                    return c;
                c++;
            }
        }
    }
}
    
