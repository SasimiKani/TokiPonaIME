package com.example.tokiponaime;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.inputmethodservice.InputMethodService;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.view.inputmethod.InputConnection;

import java.util.Arrays;
import java.util.List;

public class TokiPonaIME extends InputMethodService {

    final private InputButtons inputButtons = new InputButtons();
    final private Common common = new Common();

    private View inputViewContainer;
    private View inputView;
    private View inputView_freq;
    private View inputView_qwerty;

    /**
     * キーボードが作られる処理
     * @return キーボード配列コンテナ
     */
    @Override
    public View onCreateInputView() {
        inputViewContainer = getLayoutInflater().inflate(R.layout.keyboard_layouts_container, null);

        inputView = inputViewContainer.findViewById(R.id.keyboard_layout);
        inputView_freq = inputViewContainer.findViewById(R.id.keyboard_layout_freq);
        inputView_qwerty = inputViewContainer.findViewById(R.id.keyboard_layout_qwerty);

        inputView_freq.setVisibility(View.GONE);
        inputView_qwerty.setVisibility(View.GONE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        inputButtons.initInputButtons(this, inputView, inputView_freq, screenWidth, screenHeight);
        inputButtons.initInputButtons(this, inputView_freq, inputView_qwerty, screenWidth, screenHeight);
        inputButtons.initInputButtons(this, inputView_qwerty, inputView, screenWidth, screenHeight);

        return inputViewContainer;
    }

    /**
     * なんか更新処理
     * @param oldSelStart ぱらめーた
     * @param oldSelEnd ぱらめーた
     * @param newSelStart ぱらめーた
     * @param newSelEnd ぱらめーた
     * @param candidatesStart ぱらめーた
     * @param candidatesEnd ぱらめーた
     */
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

    /**
     * 入力されたときの処理
     * @param attribute ぱらめーた
     * @param restarting ぱらめーた
     */
    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);

        InputConnection inputConnection = getCurrentInputConnection();
        if (inputConnection != null) {
            updateCandidates(common.getSuggestions(""));
        }
    }

    /**
     * 表示中のレイアウトを特定するメソッド
     */
    private View getVisibleLayout(FrameLayout container) {
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            if (child.getVisibility() == View.VISIBLE) {
                return child;
            }
        }
        return null; // 表示中のレイアウトがない場合
    }

    /**
     * 入力候補リストを更新する処理
     * @param input ぱらめーた
     */
    public void updateCandidateList(String input) {
        View visibleLayout = getVisibleLayout((FrameLayout) inputViewContainer);

        // 候補リストの親ビューをクリア
        LinearLayout candidateList = visibleLayout.findViewById(R.id.candidate_list);
        candidateList.removeAllViews();

        List<String> suggestions = common.getSuggestions("");
        // カーソルが先頭以外にある時
        if (!input.isEmpty() && input.charAt(input.length() - 1) != ' ') {
            if (Character.isAlphabetic(input.charAt(input.length() - 1))) {
                // 新しい候補を取得
                String[] inputParts = input.split("[ \n]");
                String last_input = inputParts[inputParts.length - 1];
                suggestions = common.getSuggestions(last_input);
            }
        }

        if (suggestions.isEmpty()) {
            suggestions = Arrays.asList(common.tokiPonaMarkers);
        }

        // 候補ボタンを追加
        for (String suggestion : suggestions) {
            Button button = common.candidateButton(this, suggestion);
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
                    updateCandidates(Arrays.asList(common.tokiPonaMarkers));
                }
            });
            candidateList.addView(button);
        }
    }

    /**
     * 入力候補が選択されたときの処理
     * @param candidates ぱらめーた
     */
    private void updateCandidates(List<String> candidates) {
        if (inputViewContainer == null) {
            inputViewContainer = getLayoutInflater().inflate(R.layout.keyboard_layout, null);
        }
        LinearLayout candidateList = inputViewContainer.findViewById(R.id.candidate_list);
        candidateList.removeAllViews();

        for (String candidate : candidates) {
            Button button = common.candidateButton(this, candidate);
            button.setOnClickListener(v -> getCurrentInputConnection().commitText(candidate + " ", 1));
            candidateList.addView(button);
        }
    }

}