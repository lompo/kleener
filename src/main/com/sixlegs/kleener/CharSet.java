package com.sixlegs.kleener;

import java.util.*;

// TODO: re-implement using intervals?
final public class CharSet
{
    private final BitSet toc = new BitSet();
	private final BitSet[] root = new BitSet[256];
    private int hashCode;
    private boolean dirty;

    public CharSet() {
        dirty = true;
    }

    public CharSet(char c) {
        add(c);
    }

    public CharSet(CharSet copy) {
        add(copy);
    }

    public CharSet(CharSequence chars) {
        add(chars);
    }

    private static BitSet copy(BitSet set) {
        BitSet copy = new BitSet();
        copy.or(set);
        return copy;
    }

    public boolean isEmpty() {
        return toc.isEmpty();
    }

    public int cardinality() {
        int total = 0;
        for (int i = toc.nextSetBit(0); i >= 0; i = toc.nextSetBit(i + 1))
            total += root[i].cardinality();
        return total;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CharSet))
            return false;
        CharSet other = (CharSet)o;
        if (!toc.equals(other.toc))
            return false;
        for (int i = toc.nextSetBit(0); i >= 0; i = toc.nextSetBit(i + 1))
            if (!root[i].equals(other.root[i]))
                return false;
        return true;
    }

    public int hashCode() {
        if (dirty) {
            hashCode = computeHashCode();
            dirty = false;
        }
        return hashCode;
    }

    private int computeHashCode() {
        int value = 0;
        for (int i = toc.nextSetBit(0); i >= 0; i = toc.nextSetBit(i + 1))
            value ^= root[i].hashCode();
        return value;
    }
    
    public CharSet intersect(CharSet other) {
        // System.err.println("test intersect " + this + " and " + other);
        CharSet result = new CharSet();
        if (!other.isEmpty()) {
            for (int i = toc.nextSetBit(0); i >= 0; i = toc.nextSetBit(i + 1)) {
                BitSet a = root[i];
                BitSet b = other.root[i];
                if (b != null && b.intersects(a)) {
                    BitSet intersection = copy(a);
                    intersection.and(b);
                    a.andNot(intersection);
                    b.andNot(intersection);
                    result.root[i] = intersection;

                    updateToc(i);
                    other.updateToc(i);
                    result.updateToc(i);
                }
            }
        }
        return result;
    }

    public void add(CharSet other) {
        for (int i = other.toc.nextSetBit(0); i >= 0; i = other.toc.nextSetBit(i + 1)) {
            create(i).or(other.root[i]);
            updateToc(i);
        }
    }
        
	public void add(CharSequence chars) {
        for (int i = 0, len = chars.length(); i < len; i++)
            add(chars.charAt(i));
	}

	public void add(char ch) {
        int hibyte = ch >>> 8;
        create(hibyte).set(ch & 0xFF);
        updateToc(hibyte);
	}
    
	public void remove(char ch) {
        int hibyte = ch >>> 8;
        BitSet set = root[hibyte];
        if (set != null) {
            set.clear(ch & 0xFF);
            updateToc(hibyte);
        }
	}

    private void updateToc(int hibyte) {
        BitSet set = root[hibyte];
        toc.set(hibyte, set != null && !set.isEmpty());
        dirty = true;
    }

	public boolean contains(char ch) {
        BitSet set = root[ch >>> 8];
        return (set != null) ? set.get(ch & 0xFF) : false;
	}

    public boolean containsAny(CharSet other) {
        for (int i = toc.nextSetBit(0); i >= 0; i = toc.nextSetBit(i + 1)) {
            if (root[i].intersects(other.root[i]))
                return true;
        }
        return false;
    }

	private BitSet create(int hibyte) {
		BitSet set = root[hibyte];
		if (set == null)
            set = root[hibyte] = new BitSet(256);
		return set;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
        for (int i = toc.nextSetBit(0); i >= 0; i = toc.nextSetBit(i + 1)) {
			BitSet set = root[i];
            for (int j = set.nextSetBit(0); j >= 0; j = set.nextSetBit(j + 1))
                sb.append((char)(i << 8 | j));
		}
        return sb.toString();
	}
}
