package com.example.tokiponaime;

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
import java.util.List;

public class TokiPonaIME extends InputMethodService {
    private View candidatesView;
    private View inputView;

    @Override
    public View onCreateInputView() {
        inputView = getLayoutInflater().inflate(R.layout.keyboard_layout, null);

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
                                    handler.postDelayed(this, 100);  // 100msごとに繰り返し削除
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

        btnUp.setOnClickListener(v -> moveCursor("up"));
        btnDown.setOnClickListener(v -> moveCursor("down"));
        btnLeft.setOnClickListener(v -> moveCursor("left"));
        btnRight.setOnClickListener(v -> moveCursor("right"));

        return inputView;
    }

    private void moveCursor(String direction) {
        InputConnection inputConnection = getCurrentInputConnection();

        if (inputConnection != null) {
            CharSequence beforeCursorText = inputConnection.getTextBeforeCursor(100, 0);
            CharSequence afterCursorText = inputConnection.getTextAfterCursor(100, 0);

            int cursorPosition = (beforeCursorText != null ? beforeCursorText.length() : 0);

            switch (direction) {
                case "up":
                    // 上方向のカーソル移動（必要ならカスタマイズ）
                    break;

                case "down":
                    // 下方向のカーソル移動（必要ならカスタマイズ）
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
    }

    @Override
    public View onCreateCandidatesView() {
        // レイアウトをインフレート
        candidatesView = getLayoutInflater().inflate(R.layout.candidates_view, null);
        LinearLayout candidateList = candidatesView.findViewById(R.id.candidate_list);

        // 仮の候補リスト
        List<String> candidates = getSuggestions("");

        // 候補を追加
        for (String candidate : candidates) {
            Button button = new Button(this);
            button.setText(candidate);
            button.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
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
            CharSequence currentText = inputConnection.getTextBeforeCursor(100, 0);
            if (currentText != null) {
                updateCandidateList(currentText.toString());
            }
        }
    }

    private void updateCandidateList(String input) {
        // 候補リストの親ビューをクリア
        LinearLayout candidateList = candidatesView.findViewById(R.id.candidate_list);
        candidateList.removeAllViews();

        List<String> suggestions = getSuggestions("");;
        // カーソルが先頭以外にある時
        if (!input.isEmpty() && input.charAt(input.length() - 1) != ' ') {
            if (Character.isAlphabetic(input.charAt(input.length() - 1))) {
                // 新しい候補を取得
                String[] inputParts = input.split(" ");
                String last_input = inputParts[inputParts.length - 1];
                suggestions = getSuggestions(last_input);
            }
        }

        // 候補ボタンを追加
        for (String suggestion : suggestions) {
            Button button = new Button(this);
            button.setText(suggestion);
            button.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            button.setOnClickListener(v -> {
                InputConnection inputConnection = getCurrentInputConnection();
                if (inputConnection != null) {
                    // 入力中の文字列を削除
                    CharSequence currentText = inputConnection.getExtractedText(new ExtractedTextRequest(), 0).text;
                    if (currentText != null) {
                        // 入力中の文字列を削除
                        final String[] parts = ((String)currentText).split(" ");
                        String last = parts[parts.length - 1];
                        inputConnection.deleteSurroundingText(last.length(), 0);
                    }

                    // 候補が選択されたときに入力を確定
                    inputConnection.commitText(suggestion + " ", 1);
                }
            });
            candidateList.addView(button);
        }
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);

        // 入力が開始されたら候補ビューを表示
        setCandidatesViewShown(true);

        InputConnection inputConnection = getCurrentInputConnection();
        if (inputConnection != null) {
            updateCandidates(getSuggestions(""));
        }
    }

    private void updateCandidates(List<String> candidates) {
        LinearLayout candidateList = candidatesView.findViewById(R.id.candidate_list);
        candidateList.removeAllViews();

        for (String candidate : candidates) {
            Button button = new Button(this);
            button.setText(candidate);
            button.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            button.setOnClickListener(v -> getCurrentInputConnection().commitText(candidate + " ", 1));
            candidateList.addView(button);
        }
    }
}