package com.sixlegs.kleener;

// TODO: reduce number of anonymous classes
abstract class JavaCharSet extends GenericCharSet
{
    public static final CharSet ISOControl =
      new CharSetBuilder().add('\u0000', '\u001F').add('\u007F', '\u009F').build();

    public static final CharSet Digit = new JavaCharSet() {
        public boolean contains(int c) { return Character.isDigit(c); }
    };

    public static final CharSet JavaIdentifierPart = new JavaCharSet() {
        public boolean contains(int c) { return Character.isJavaIdentifierPart(c); }
    };
    
    public static final CharSet JavaIdentifierStart = new JavaCharSet() {
        public boolean contains(int c) { return Character.isJavaIdentifierStart(c); }
    };

    public static final CharSet IdentifierIgnorable = new JavaCharSet() {
        public boolean contains(int c) { return Character.isIdentifierIgnorable(c); }
    };
    
    public static final CharSet Letter = new JavaCharSet() {
        public boolean contains(int c) { return Character.isLetter(c); }
    };
    
    public static final CharSet LetterOrDigit = new JavaCharSet() {
        public boolean contains(int c) { return Character.isLetterOrDigit(c); }
    };
    
    public static final CharSet LowerCase = new JavaCharSet() {
        public boolean contains(int c) { return Character.isLowerCase(c); }
    };
    
    public static final CharSet Mirrored = new JavaCharSet() {
        public boolean contains(int c) { return Character.isMirrored(c); }
    };
    
    public static final CharSet SpaceChar = new JavaCharSet() {
        public boolean contains(int c) { return Character.isSpaceChar(c); }
    };
    
    public static final CharSet TitleCase = new JavaCharSet() {
        public boolean contains(int c) { return Character.isTitleCase(c); }
    };
    
    public static final CharSet UnicodeIdentifierPart = new JavaCharSet() {
        public boolean contains(int c) { return Character.isUnicodeIdentifierPart(c); }
    };
    
    public static final CharSet UnicodeIdentifierStart = new JavaCharSet() {
        public boolean contains(int c) { return Character.isUnicodeIdentifierStart(c); }
    };
    
    public static final CharSet UpperCase = new JavaCharSet() {
        public boolean contains(int c) { return Character.isUpperCase(c); }
    };
    
    public static final CharSet Whitespace = new JavaCharSet() {
        public boolean contains(int c) { return Character.isWhitespace(c); }
    };
    
    private final int last;

    private JavaCharSet() {
        int test = 65535;
        while (!contains(test) && test >= 0)
            test--;
        last = test;
    }

    public int nextChar(int c) {
        for (int i = c; i <= last; i++)
            if (contains(i))
                return i;
        return -1;
    }
}
