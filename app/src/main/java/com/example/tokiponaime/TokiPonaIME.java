package com.example.tokiponaime;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.inputmethodservice.InputMethodService;
import android.widget.LinearLayout;
import android.view.inputmethod.InputConnection;

import java.util.Arrays;
import java.util.List;

public class TokiPonaIME extends InputMethodService {

    private InputButtons inputButtons = new InputButtons();
    private Common common = new Common();

    private View candidatesView;
    private View inputView;

    /**
     * キーボードが作られる処理
     * @return
     */
    @Override
    public View onCreateInputView() {
        inputView = getLayoutInflater().inflate(R.layout.keyboard_layout, null);

        inputButtons.initInputButtons(this, inputView);

        return inputView;
    }

    /**
     * なんか更新処理
     * @param oldSelStart
     * @param oldSelEnd
     * @param newSelStart
     * @param newSelEnd
     * @param candidatesStart
     * @param candidatesEnd
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
     * @param attribute
     * @param restarting
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
     * 入力候補リストを更新する処理
     * @param input
     */
    private void updateCandidateList(String input) {
        // 候補リストの親ビューをクリア
        LinearLayout candidateList = inputView.findViewById(R.id.candidate_list);
        candidateList.removeAllViews();

        List<String> suggestions = common.getSuggestions("");;
        // カーソルが先頭以外にある時
        if (!input.isEmpty() && input.charAt(input.length() - 1) != ' ') {
            if (Character.isAlphabetic(input.charAt(input.length() - 1))) {
                // 新しい候補を取得
                String[] inputParts = input.split("( |\n)");
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
     * @param candidates
     */
    private void updateCandidates(List<String> candidates) {
        if (inputView == null) {
            inputView = getLayoutInflater().inflate(R.layout.keyboard_layout, null);
        }
        LinearLayout candidateList = inputView.findViewById(R.id.candidate_list);
        candidateList.removeAllViews();

        for (String candidate : candidates) {
            Button button = common.candidateButton(this, candidate);
            button.setOnClickListener(v -> getCurrentInputConnection().commitText(candidate + " ", 1));
            candidateList.addView(button);
        }
    }

}