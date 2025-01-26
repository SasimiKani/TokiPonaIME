package com.example.tokiponaime;

import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class Common {

    public static double AREA_HEIGHT = 0.32;
    public static double GRID_HEIGHT_50 = AREA_HEIGHT * 0.187;
    public static double GRID_HEIGHT_55 = GRID_HEIGHT_50 * 1.1;
    public static double KEY_WIDTH = 0.146;
    public static double KEY_WIDTH_BS = 0.159;
    public static double KEY_WIDTH_SPACE = 0.341;
    public static double KEY_WIDTH_LR = 0.085;
    public static double KEY_WIDTH_UD = 0.097;
    public static double KEY_WIDTH_ENTER = KEY_WIDTH;
    public static double KEY_WIDTH_SYMBOL = 0.122;
    public static double SPACE_20 = 0.048;
    public static double SPACE_35 = 0.085;

    public static double MARGIN_1 = GRID_HEIGHT_50;
    public static double MARGIN_2 = GRID_HEIGHT_50 * 2;
    public static double MARGIN_3 = GRID_HEIGHT_50 * 3.1;

    /**
     * TokiPonaの単語リスト
     */
    public static final String[] tokiPonaWords = {
            "a", "akesi", "ala", "alasa", "ale", "anpa", "ante", "anu", "apeja", "awen", "e",
            "en", "epiku", "esu", "ijo", "ike", "ilo", "insa", "jaki", "jan", "jasima", "jelo",
            "jo", "kala", "kalama", "kama", "kasi", "ken", "kepeken", "kijetesantakalu", "kili", "kin", "kipisi",
            "kiwen", "ko", "kokosila", "kon", "ku", "kule", "kulupu", "kute", "la", "lanpan", "lape",
            "laso", "lawa", "leko", "len", "lete", "li", "lili", "linja", "lipu", "loje", "lon",
            "luka", "lukin", "lupa", "ma", "mama", "mani", "meli", "meso", "mi", "mije", "misikeke",
            "moku", "moli", "monsi", "monsuta", "mu", "mun", "musi", "mute", "n", "namako",
            "nanpa", "nasa", "nasin", "nena", "ni", "nimi", "noka", "o", "oko", "olin", "ona",
            "open", "pakala", "pali", "palisa", "pan", "pana", "pi", "pilin", "pimeja", "pini", "pipi",
            "poka", "poki", "pona", "pu", "sama", "seli", "selo", "seme", "sewi", "sijelo", "sike",
            "sin", "sina", "sinpin", "sitelen", "soko", "sona", "soweli", "suli", "suno", "supa", "suwi",
            "tan", "taso", "tawa", "telo", "tenpo", "toki", "tomo", "tonsi", "tu", "unpa", "uta",
            "utala", "walo", "wan", "waso", "wawa", "weka", "wile"
    };
    /**
     * TokiPonaの単語リスト
     */
    public static final String[] tokiPonaMarkers = {
            "e", "li", "pi", "la", "o", "a", "n", "seme", "taso"
    };

    /**
     * 入力中の文字列から入力候補を生成して返す処理
     * @param input ぱらめーた
     * @return 入力候補
     */
    public static List<String> getSuggestions(String input) {
        if (input.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> suggestions = new ArrayList<>();
        for (String word : tokiPonaWords) {
            if (word.startsWith(input)) {
                suggestions.add(word);
            }
        }
        return suggestions;
    }

    /**
     * 入力候補ボタンを返す
     * @param activity ぱらめーた
     * @param candidate ぱらめーた
     * @return 入力候補ボタン
     */
    public static Button candidateButton(TokiPonaIME activity, String candidate) {
        Button button = new Button(activity);
        button.setText(candidate);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                dpToPx(activity, 38 + candidate.length()*7),
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        return button;
    }

    // dpをピクセルに変換するヘルパーメソッド
    public static int dpToPx(TokiPonaIME activity, int dp) {
        float density = activity.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
