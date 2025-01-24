package com.example.tokiponaime;

import static androidx.core.util.TypedValueCompat.dpToPx;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedTextRequest;
import android.widget.Button;
import android.inputmethodservice.InputMethodService;
import android.widget.LinearLayout;
import android.view.inputmethod.InputConnection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TokiPonaIME extends InputMethodService {
    private View candidatesView;
    private View inputView;

    @Override
    public View onCreateInputView() {
        inputView = getLayoutInflater().inflate(R.layout.keyboard_layout, null);

        initCandidatesView(inputView);

        // 母音
        Button btnA = inputView.findViewById(R.id.btn_a);
        Button btnE = inputView.findViewById(R.id.btn_e);
        Button btnI = inputView.findViewById(R.id.btn_i);
        Button btnO = inputView.findViewById(R.id.btn_o);
        Button btnU = inputView.findViewById(R.id.btn_u);

        // 子音
        Button btnJ = inputView.findViewById(R.id.btn_j);
        Button btnK = inputView.findViewById(R.id.btn_k);
        Button btnL = inputView.findViewById(R.id.btn_l);
        Button btnM = inputView.findViewById(R.id.btn_m);
        Button btnN = inputView.findViewById(R.id.btn_n);
        Button btnP = inputView.findViewById(R.id.btn_p);
        Button btnS = inputView.findViewById(R.id.btn_s);
        Button btnT = inputView.findViewById(R.id.btn_t);
        Button btnW = inputView.findViewById(R.id.btn_w);

        // その他
        Button btnEnter = inputView.findViewById(R.id.btn_enter);
        Button btnSpace = inputView.findViewById(R.id.btn_space);
        Button btnBackSpace = inputView.findViewById(R.id.btn_back_space);
        Button btnPeriod = inputView.findViewById(R.id.btn_period);
        Button btnComma = inputView.findViewById(R.id.btn_comma);
        Button btnCoron = inputView.findViewById(R.id.btn_coron);
        Button btnExclamation = inputView.findViewById(R.id.btn_exclamation);
        Button btnQuestion = inputView.findViewById(R.id.btn_question);

        Button btnUp = inputView.findViewById(R.id.btn_up);
        Button btnDown = inputView.findViewById(R.id.btn_down);
        Button btnLeft = inputView.findViewById(R.id.btn_left);
        Button btnRight = inputView.findViewById(R.id.btn_right);

        btnA.setOnClickListener(v -> {
            getCurrentInputConnection().commitText("a", 1);
        });
        btnE.setOnClickListener(v -> {
            getCurrentInputConnection().commitText("e", 1);
        });
        btnI.setOnClickListener(v -> {
            getCurrentInputConnection().commitText("i", 1);
        });
        btnO.setOnClickListener(v -> {
            getCurrentInputConnection().commitText("o", 1);
        });
        btnU.setOnClickListener(v -> {
            getCurrentInputConnection().commitText("u", 1);
        });

        btnJ.setOnClickListener(v -> {
            getCurrentInputConnection().commitText("j", 1);
        });
        btnK.setOnClickListener(v -> {
            getCurrentInputConnection().commitText("k", 1);
        });
        btnL.setOnClickListener(v -> {
            getCurrentInputConnection().commitText("l", 1);
        });
        btnM.setOnClickListener(v -> {
            getCurrentInputConnection().commitText("m", 1);
        });
        btnN.setOnClickListener(v -> {
            getCurrentInputConnection().commitText("n", 1);
        });
        btnP.setOnClickListener(v -> {
            getCurrentInputConnection().commitText("p", 1);
        });
        btnS.setOnClickListener(v -> {
            getCurrentInputConnection().commitText("s", 1);
        });
        btnT.setOnClickListener(v -> {
            getCurrentInputConnection().commitText("t", 1);
        });
        btnW.setOnClickListener(v -> {
            getCurrentInputConnection().commitText("w", 1);
        });

        btnEnter.setOnClickListener(v -> {
            getCurrentInputConnection().commitText("\n", 1);
        });
        btnSpace.setOnClickListener(v -> {
            getCurrentInputConnection().commitText(" ", 1);
        });
        btnBackSpace.setOnTouchListener(new View.OnTouchListener() {
            private Handler handler = new Handler();
            private Runnable deleteRunnable;
            private boolean isLongPress = false;  // 長押しの判定フラグ

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputConnection inputConnection = getCurrentInputConnection();

                if (inputConnection != null) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // 最初に1文字を削除
                            inputConnection.deleteSurroundingText(1, 0);

                            // 長押しの処理を開始
                            isLongPress = false;
                            deleteRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    CharSequence currentText = inputConnection.getExtractedText(
                                            new ExtractedTextRequest(), 0
                                    ).text;

                                    if (currentText.length() > 0 && currentText != null) {
                                        inputConnection.deleteSurroundingText(1, 0);
                                    }
                                    handler.postDelayed(this, 60);  // 100msごとに繰り返し削除
                                    isLongPress = true;  // 長押しが始まった
                                }
                            };
                            handler.postDelayed(deleteRunnable, 500);  // 500ms後に繰り返し処理を開始
                            break;

                        case MotionEvent.ACTION_UP:
                            // ボタンが離された時に削除を停止
                            handler.removeCallbacks(deleteRunnable);
                            break;
                    }
                }
                return true;
            }
        });
        btnPeriod.setOnClickListener(v -> {
            getCurrentInputConnection().commitText(".", 1);
        });
        btnComma.setOnClickListener(v -> {
            getCurrentInputConnection().commitText(",", 1);
        });
        btnCoron.setOnClickListener(v -> {
            getCurrentInputConnection().commitText(":", 1);
        });
        btnExclamation.setOnClickListener(v -> {
            getCurrentInputConnection().commitText("!", 1);
        });
        btnQuestion.setOnClickListener(v -> {
            getCurrentInputConnection().commitText("?", 1);
        });

        btnUp.setOnTouchListener(new View.OnTouchListener() {
            Handler handler = new Handler();
            Runnable longPressRunnable;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    moveCursor("up"); // 最初に1回カーソル移動

                    longPressRunnable = new Runnable() {
                        @Override
                        public void run() {
                            moveCursor("up");
                            handler.postDelayed(this, 60); // 60ms後に再実行
                        }
                    };
                    handler.postDelayed(longPressRunnable, 500); // 500ms後に実行開始
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    handler.removeCallbacks(longPressRunnable); // 長押し処理をキャンセル
                }
                return false; // クリックイベントも処理したい場合はtrueにする
            }
        });

        btnDown.setOnTouchListener(new View.OnTouchListener() {
            Handler handler = new Handler();
            Runnable longPressRunnable;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    moveCursor("down"); // 最初に1回カーソル移動

                    longPressRunnable = new Runnable() {
                        @Override
                        public void run() {
                            moveCursor("down");
                            handler.postDelayed(this, 60); // 60ms後に再実行
                        }
                    };
                    handler.postDelayed(longPressRunnable, 500); // 500ms後に実行開始
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    handler.removeCallbacks(longPressRunnable); // 長押し処理をキャンセル
                }
                return false; // クリックイベントも処理したい場合はtrueにする
            }
        });


        btnLeft.setOnTouchListener(new View.OnTouchListener() {
            Handler handler = new Handler();
            Runnable longPressRunnable;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    moveCursor("left"); // 最初に1回カーソル移動

                    longPressRunnable = new Runnable() {
                        @Override
                        public void run() {
                            moveCursor("left");
                            handler.postDelayed(this, 60); // 60ms後に再実行
                        }
                    };
                    handler.postDelayed(longPressRunnable, 500); // 500ms後に実行開始
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    handler.removeCallbacks(longPressRunnable); // 長押し処理をキャンセル
                }
                return false; // クリックイベントも処理したい場合はtrueにする
            }
        });

        btnRight.setOnTouchListener(new View.OnTouchListener() {
            Handler handler = new Handler();
            Runnable longPressRunnable;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    moveCursor("right"); // 最初に1回カーソル移動

                    longPressRunnable = new Runnable() {
                        @Override
                        public void run() {
                            moveCursor("right");
                            handler.postDelayed(this, 60); // 60ms後に再実行
                        }
                    };
                    handler.postDelayed(longPressRunnable, 500); // 500ms後に実行開始
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    handler.removeCallbacks(longPressRunnable); // 長押し処理をキャンセル
                }
                return false; // クリックイベントも処理したい場合はtrueにする
            }
        });

        return inputView;
    }

    private void moveCursor(String direction) {
        InputConnection inputConnection = getCurrentInputConnection();

        if (inputConnection != null) {
            CharSequence beforeCursorText = inputConnection.getTextBeforeCursor(0xfffff, 0);
            CharSequence afterCursorText = inputConnection.getTextAfterCursor(0xfffff, 0);
            CharSequence text = beforeCursorText.toString() + afterCursorText.toString();

            int cursorPosition = (beforeCursorText != null ? beforeCursorText.length() : 0);

            switch (direction) {
                case "up":
                    // 上方向のカーソル移動
                    // 前の行の開始位置を計算 (改行文字 '\n' を探す)
                    int previousLineStart = -1;
                    if (beforeCursorText != null) {
                        for (int i = beforeCursorText.length() - 1, k = 0; i >= 0; i--, k++) {
                            if (beforeCursorText.charAt(i) == '\n') {
                                for (int j = i - 1, l = 0; j >= -1; j--, l++) {
                                    if (j == -1 || beforeCursorText.charAt(j) == '\n') {
                                        if (l < k) {
                                            previousLineStart = j + 1;
                                            break;
                                        }
                                        previousLineStart = j + k + 1;
                                        break;
                                    }
                                }
                                break;
                            } else if (i == 0) {
                                previousLineStart = 0;
                            }
                        }
                    }
                    // カーソル位置を更新
                    if (previousLineStart != -1) {
                        inputConnection.setSelection(previousLineStart, previousLineStart);
                    }
                    break;

                case "down":
                    // 下方向のカーソル移動
                    // 次の行の開始位置を計算 (改行文字 '\n' を探す)
                    int nextLineStart = -1;
                    if (afterCursorText != null) {
                        int k = 0;
                        for (int i = beforeCursorText.length() - 1; i > -1 && beforeCursorText.charAt(i) != '\n'; i--, k++);
                        System.out.println(k);
                        for (int i = 0; i < afterCursorText.length(); i++) {
                            if (afterCursorText.charAt(i) == '\n') {
                                System.out.println(i);
                                System.out.println(cursorPosition);
                                int l = 0;
                                for (int j = i + 1; j < afterCursorText.length() && afterCursorText.charAt(j) != '\n'; j++, l++);
                                if (l < k) {
                                    nextLineStart = cursorPosition + i + l + 1;
                                    break;
                                }
                                nextLineStart = cursorPosition + i + k + 1;
                                break;
                            }
                        }
                    }
                    System.out.println(nextLineStart);

                    // カーソル位置を更新
                    if (nextLineStart != -1) {
                        inputConnection.setSelection(nextLineStart, nextLineStart);
                    }
                    break;

                case "left":
                    // 左方向へのカーソル移動
                    if (beforeCursorText != null && cursorPosition > 0) {
                        inputConnection.setSelection(cursorPosition - 1, cursorPosition - 1);
                    }
                    break;

                case "right":
                    // 右方向へのカーソル移動
                    if (afterCursorText != null && afterCursorText.length() > 0) {
                        inputConnection.setSelection(cursorPosition + 1, cursorPosition + 1);
                    }
                    break;
            }
        }
    }

    private final String[] tokiPonaWords = {
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
    private final String[] tokiPonaMarkers = {
            "e", "li", "pi", "la", "o", "a", "n", "seme", "taso"
    };

    private List<String> getSuggestions(String input) {
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
    };

    public View initCandidatesView(View inputView) {
        LinearLayout candidateList = inputView.findViewById(R.id.candidate_list);

        // 仮の候補リスト
        List<String> candidates = Arrays.asList(tokiPonaMarkers);

        // 候補を追加
        for (String candidate : candidates) {
            Button button = candidateButton(candidate);
            button.setOnClickListener(v -> {
                // 候補が選択されたときの処理
                getCurrentInputConnection().commitText(candidate, 1);
            });
            candidateList.addView(button);
        }

        return candidatesView;
    }

    @Override
    public void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd);

        InputConnection inputConnection = getCurrentInputConnection();

        if (inputConnection != null) {
            CharSequence currentText = inputConnection.getTextBeforeCursor(0xfffff, 0);
            if (currentText != null) {
                updateCandidateList(currentText.toString());
            }
        }
    }

    private void updateCandidateList(String input) {
        // 候補リストの親ビューをクリア
        LinearLayout candidateList = inputView.findViewById(R.id.candidate_list);
        candidateList.removeAllViews();

        List<String> suggestions = getSuggestions("");;
        // カーソルが先頭以外にある時
        if (!input.isEmpty() && input.charAt(input.length() - 1) != ' ') {
            if (Character.isAlphabetic(input.charAt(input.length() - 1))) {
                // 新しい候補を取得
                String[] inputParts = input.split("( |\n)");
                String last_input = inputParts[inputParts.length - 1];
                suggestions = getSuggestions(last_input);
            }
        }

        if (suggestions.isEmpty()) {
            suggestions = Arrays.asList(tokiPonaMarkers);
        }

        // 候補ボタンを追加
        for (String suggestion : suggestions) {
            Button button = candidateButton(suggestion);
            button.setOnClickListener(v -> {
                InputConnection inputConnection = getCurrentInputConnection();
                if (inputConnection != null) {
                    CharSequence beforeCursorText = inputConnection.getTextBeforeCursor(0xfffff, 0);
                    CharSequence afterCursorText = inputConnection.getTextAfterCursor(0xfffff, 0);

                    int wordLength = 0;

                    for (int i = beforeCursorText.length() - 1; i >= -1; i--, wordLength++) {
                        if (i == -1 || beforeCursorText.charAt(i) == ' ' || beforeCursorText.charAt(i) == '\n') {
                            break;
                        }
                        inputConnection.deleteSurroundingText(1, 0);
                    }

                    for (int i = 0; i < afterCursorText.length(); i++, wordLength++) {
                        inputConnection.deleteSurroundingText(0, 1);
                        if (i == afterCursorText.length() || afterCursorText.charAt(i) == ' ' || afterCursorText.charAt(i) == '\n') {
                            break;
                        }
                    }

                    // 候補が選択されたときに入力を確定
                    inputConnection.commitText(suggestion + " ", 1);
                    updateCandidates(Arrays.asList(tokiPonaMarkers));
                }
            });
            candidateList.addView(button);
        }
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);

        InputConnection inputConnection = getCurrentInputConnection();
        if (inputConnection != null) {
            updateCandidates(getSuggestions(""));
        }
    }

    private void updateCandidates(List<String> candidates) {
        if (inputView == null) {
            inputView = getLayoutInflater().inflate(R.layout.keyboard_layout, null);
        }
        LinearLayout candidateList = inputView.findViewById(R.id.candidate_list);
        candidateList.removeAllViews();

        for (String candidate : candidates) {
            Button button = candidateButton(candidate);
            button.setOnClickListener(v -> getCurrentInputConnection().commitText(candidate + " ", 1));
            candidateList.addView(button);
        }
    }

    public Button candidateButton(String candidate) {
        Button button = new Button(this);
        button.setText(candidate);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                dpToPx(30 + candidate.length()*8),
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        return button;
    }

    // dpをピクセルに変換するヘルパーメソッド
    public int dpToPx(int dp) {
        float density = this.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}