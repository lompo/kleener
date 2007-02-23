package com.sixlegs.kleener;

import java.util.*;

class Generics
{
    public static <T> List<T> newArrayList() { return new ArrayList<T>(); }
    public static <T> List<T> newLinkedList() { return new LinkedList<T>(); }
    public static <T> Set<T> newHashSet() { return new HashSet<T>(); }
    public static <T,S> Map<T,S> newHashMap() { return new HashMap<T,S>(); }
}

