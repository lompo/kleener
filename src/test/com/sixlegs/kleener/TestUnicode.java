package com.sixlegs.kleener;

import java.lang.reflect.*;
import java.util.*;
import org.testng.annotations.*;
import static java.lang.Character.UnicodeBlock.*;

public class TestUnicode
{
    private static final int MAX_CODEPOINT = 0x10ffff;

    private static final List<Character.UnicodeBlock> blocks =
      Arrays.asList(AEGEAN_NUMBERS,
                    ALPHABETIC_PRESENTATION_FORMS,
                    ARABIC,
                    ARABIC_PRESENTATION_FORMS_A,
                    ARABIC_PRESENTATION_FORMS_B,
                    ARMENIAN,
                    ARROWS,
                    BASIC_LATIN,
                    BENGALI,
                    BLOCK_ELEMENTS,
                    BOPOMOFO,
                    BOPOMOFO_EXTENDED,
                    BOX_DRAWING,
                    BRAILLE_PATTERNS,
                    BUHID,
                    BYZANTINE_MUSICAL_SYMBOLS,
                    CHEROKEE,
                    CJK_COMPATIBILITY,
                    CJK_COMPATIBILITY_FORMS,
                    CJK_COMPATIBILITY_IDEOGRAPHS,
                    CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT,
                    CJK_RADICALS_SUPPLEMENT,
                    CJK_SYMBOLS_AND_PUNCTUATION,
                    CJK_UNIFIED_IDEOGRAPHS,
                    CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A,
                    CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B,
                    COMBINING_DIACRITICAL_MARKS,
                    COMBINING_HALF_MARKS,
                    COMBINING_MARKS_FOR_SYMBOLS,
                    CONTROL_PICTURES,
                    CURRENCY_SYMBOLS,
                    CYPRIOT_SYLLABARY,
                    CYRILLIC,
                    CYRILLIC_SUPPLEMENTARY,
                    DESERET,
                    DEVANAGARI,
                    DINGBATS,
                    ENCLOSED_ALPHANUMERICS,
                    ENCLOSED_CJK_LETTERS_AND_MONTHS,
                    ETHIOPIC,
                    GENERAL_PUNCTUATION,
                    GEOMETRIC_SHAPES,
                    GEORGIAN,
                    GOTHIC,
                    GREEK,
                    GREEK_EXTENDED,
                    GUJARATI,
                    GURMUKHI,
                    HALFWIDTH_AND_FULLWIDTH_FORMS,
                    HANGUL_COMPATIBILITY_JAMO,
                    HANGUL_JAMO,
                    HANGUL_SYLLABLES,
                    HANUNOO,
                    HEBREW,
                    HIGH_PRIVATE_USE_SURROGATES,
                    HIGH_SURROGATES,
                    HIRAGANA,
                    IDEOGRAPHIC_DESCRIPTION_CHARACTERS,
                    IPA_EXTENSIONS,
                    KANBUN,
                    KANGXI_RADICALS,
                    KANNADA,
                    KATAKANA,
                    KATAKANA_PHONETIC_EXTENSIONS,
                    KHMER,
                    KHMER_SYMBOLS,
                    LAO,
                    LATIN_1_SUPPLEMENT,
                    LATIN_EXTENDED_A,
                    LATIN_EXTENDED_ADDITIONAL,
                    LATIN_EXTENDED_B,
                    LETTERLIKE_SYMBOLS,
                    LIMBU,
                    LINEAR_B_IDEOGRAMS,
                    LINEAR_B_SYLLABARY,
                    LOW_SURROGATES,
                    MALAYALAM,
                    MATHEMATICAL_ALPHANUMERIC_SYMBOLS,
                    MATHEMATICAL_OPERATORS,
                    MISCELLANEOUS_MATHEMATICAL_SYMBOLS_A,
                    MISCELLANEOUS_MATHEMATICAL_SYMBOLS_B,
                    MISCELLANEOUS_SYMBOLS,
                    MISCELLANEOUS_SYMBOLS_AND_ARROWS,
                    MISCELLANEOUS_TECHNICAL,
                    MONGOLIAN,
                    MUSICAL_SYMBOLS,
                    MYANMAR,
                    NUMBER_FORMS,
                    OGHAM,
                    OLD_ITALIC,
                    OPTICAL_CHARACTER_RECOGNITION,
                    ORIYA,
                    OSMANYA,
                    PHONETIC_EXTENSIONS,
                    PRIVATE_USE_AREA,
                    RUNIC,
                    SHAVIAN,
                    SINHALA,
                    SMALL_FORM_VARIANTS,
                    SPACING_MODIFIER_LETTERS,
                    SPECIALS,
                    SUPERSCRIPTS_AND_SUBSCRIPTS,
                    SUPPLEMENTAL_ARROWS_A,
                    SUPPLEMENTAL_ARROWS_B,
                    SUPPLEMENTAL_MATHEMATICAL_OPERATORS,
                    SUPPLEMENTARY_PRIVATE_USE_AREA_A,
                    SUPPLEMENTARY_PRIVATE_USE_AREA_B,
                    SURROGATES_AREA,
                    SYRIAC,
                    TAGALOG,
                    TAGBANWA,
                    TAGS,
                    TAI_LE,
                    TAI_XUAN_JING_SYMBOLS,
                    TAMIL,
                    TELUGU,
                    THAANA,
                    THAI,
                    TIBETAN,
                    UGARITIC,
                    UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS,
                    VARIATION_SELECTORS,
                    VARIATION_SELECTORS_SUPPLEMENT,
                    YI_RADICALS,
                    YI_SYLLABLES,
                    YIJING_HEXAGRAM_SYMBOLS);

    @Test(groups = { "unicode" })
    public void generateBlockRanges() throws Exception {
        Map<Character.UnicodeBlock,BitSet> blockMap = Generics.newHashMap();
        for (Character.UnicodeBlock block : blocks)
            blockMap.put(block, new BitSet());
        for (int i = 0; i < MAX_CODEPOINT; i++) {
            Character.UnicodeBlock block = Character.UnicodeBlock.of(i);
            if (block != null)
                blockMap.get(block).set(i);
        }
        for (Character.UnicodeBlock block : blocks) {
            StringBuilder sb = new StringBuilder();
            BitSet bits = blockMap.get(block);
            int prev = -2;
            int start = -1;
            sb.append("blocks.put(").append(block.toString()).append(", new CharSetBuilder()");
            for (int i = bits.nextSetBit(0); i >= 0; i = bits.nextSetBit(i + 1)) {
                if (i != prev + 1) {
                    rangeHelper(sb, start, prev);
                    start = i;
                }
                prev = i;
            }
            rangeHelper(sb, start, prev);
            sb.append(".build());");
            System.err.println(sb);
        }
    }

    private static void rangeHelper(StringBuilder sb, int start, int end) {
        if (start >= 0)
            sb.append(".add(").append(start).append(", ").append(end).append(")");
    }

}
