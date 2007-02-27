package com.sixlegs.kleener;

import java.util.*;

class Generics
{
    public static <T> List<T> newArrayList() { return new ArrayList<T>(); }
    public static <T> List<T> newArrayList(Collection<? extends T> c) { return new ArrayList<T>(c); }
    public static <T> List<T> newLinkedList() { return new LinkedList<T>(); }
    public static <T> Set<T> newHashSet() { return new HashSet<T>(); }
    public static <T,S> Map<T,S> newHashMap() { return new HashMap<T,S>(); }
    public static <T,S> Map<T,S> newTreeMap() { return new TreeMap<T,S>(); }
    public static <T,S> Map<T,S> newLinkedHashMap() { return new LinkedHashMap<T,S>(); }

    public static <T,S> Map<S,List<T>> bucket(Collection<? extends T> c, Function<T,S> f) {
        Map<S,List<T>> buckets = Generics.newHashMap();
        for (T value : c) {
            S key = f.eval(value);
            List<T> bucket = buckets.get(key);
            if (bucket == null)
                buckets.put(key, bucket = Generics.newArrayList());
            bucket.add(value);
        }
        return buckets;
    }

    public static <T> Collection filter(Collection<? extends T> c, Function<T,Boolean> p) {
        Iterator<? extends T> it = c.iterator();
        while (it.hasNext()) {
            if (!p.eval(it.next()))
                it.remove();
        }
        return c;
    }
}

