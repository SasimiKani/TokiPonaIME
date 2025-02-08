package com.tokiponaime.tokiponaime;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
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

    public EditorInfo mEditorInfo;

    private LayoutPreferences layoutPreferences;

    private View inputViewContainer;
    private View inputView;
    private View inputView_freq;
    private View inputView_qwerty;
    private boolean restarting;

    /**
     * キーボードが作られる処理
     * @return キーボード配列コンテナ
     */
    @Override
    public View onCreateInputView() {

        // レイアウトの保存先
        layoutPreferences = new LayoutPreferences(this);
        int savedLayout = layoutPreferences.getLayoutId(R.layout.keyboard_layouts_container);

        // レイアウトの初期化
        inputViewContainer = getLayoutInflater().inflate(R.layout.keyboard_layouts_container, null);

        // レイアウトの切り替え
        inputView = inputViewContainer.findViewById(R.id.keyboard_layout);
        inputView_freq = inputViewContainer.findViewById(R.id.keyboard_layout_freq);
        inputView_qwerty = inputViewContainer.findViewById(R.id.keyboard_layout_qwerty);

        // レイアウトの切り替え
        inputView.setVisibility(View.GONE);
        inputView_freq.setVisibility(View.GONE);
        inputView_qwerty.setVisibility(View.GONE);

        // 保存したレイアウトを表示
        if (savedLayout == R.id.keyboard_layout_freq) {
            inputView_freq.setVisibility(View.VISIBLE);
        } else if (savedLayout == R.id.keyboard_layout_qwerty) {
            inputView_qwerty.setVisibility(View.VISIBLE);
        } else {
            inputView.setVisibility(View.VISIBLE);
        }

        // 画面サイズの取得
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        float density = getResources().getDisplayMetrics().density;
        float fontSize = 15f;
        if (screenHeight < screenWidth) {
            fontSize = 24f;
        }

        // フォントサイズの調整
        if (2118 / screenHeight < 1080 / screenWidth) {
            Common.fontSize = fontSize * screenWidth / density / 1080f * 2.625f;
        } else {
            Common.fontSize = fontSize * screenHeight / density / 2118f * 2.625f;
        }

        // 入力ボタンの初期化
        InputButtons.initInputButtons(this, inputView, inputView_freq, screenWidth, screenHeight);
        InputButtons.initInputButtons(this, inputView_freq, inputView_qwerty, screenWidth, screenHeight);
        InputButtons.initInputButtons(this, inputView_qwerty, inputView, screenWidth, screenHeight);

        return inputViewContainer;
    }

    /**
     * 入力されたときの処理
     * @param attribute ぱらめーた
     * @param restarting ぱらめーた
     */
    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);

        mEditorInfo = attribute;

        // 入力候補リストの初期化
        InputConnection inputConnection = getCurrentInputConnection();
        if (inputConnection != null) {
            updateCandidates(Common.getSuggestions(""));
        }
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
        updateCandidateList();
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
     */
    public void updateCandidateList() {

        InputConnection inputConnection = getCurrentInputConnection();

        if (inputConnection != null) {
            CharSequence beforeCursor = inputConnection.getTextBeforeCursor(0xfffff, 0);
            CharSequence afterCursor = inputConnection.getTextAfterCursor(0xfffff, 0);
            if (beforeCursor != null || afterCursor != null) {
                String input1 = beforeCursor.toString();
                String input2 = afterCursor.toString();

                View visibleLayout = getVisibleLayout((FrameLayout) inputViewContainer);

                // 候補リストの親ビューをクリア
                LinearLayout candidateList = visibleLayout.findViewById(R.id.candidate_list);
                candidateList.removeAllViews();

                List<String> suggestions = Common.getSuggestions("");
                // カーソルが先頭以外にある時
                if (!input1.isEmpty() && input1.charAt(input1.length() - 1) != ' ') {
                    if (Character.isAlphabetic(input1.charAt(input1.length() - 1))) {
                        // 新しい候補を取得
                        String currentWord = "";

                        for (int i = input1.length() - 1; i >= -1; i--) {
                            if (i == -1 || Common.symbols.indexOf(input1.charAt(i)) != -1) {
                                currentWord += input1.substring(i + 1);
                                break;
                            }
                        }

                        for (int i = 0; i < input2.length(); i++) {
                            if (i == input2.length() || Common.symbols.indexOf(input2.charAt(i)) != -1) {
                                currentWord += input2.substring(0, i);
                                break;
                            }
                        }

                        suggestions = Common.getSuggestions(currentWord);
                    }
                }

                // 候補が空の時＆直前・直後にアルファベットがないとき
                if (suggestions.isEmpty() && (input1.length() == 0 || Common.symbols.indexOf(input1.charAt(input1.length() - 1)) != -1) && (input2.length() == 0 || Common.symbols.indexOf(input2.charAt(0)) != -1)) {
                    suggestions = Arrays.asList(Common.tokiPonaMarkers);
                }

                // 候補ボタンを追加
                for (String suggestion : suggestions) {
                    Button button = Common.candidateButton(this, suggestion);
                    button.setTextSize(Common.fontSize);
                    button.setAllCaps(false);
                    button.setOnClickListener(v -> {
                        InputConnection ic = getCurrentInputConnection();

                        // カーソル位置を取得して補完を排除
                        int cursorPosition = (ic.getTextBeforeCursor(0xfffff, 0).length());
                        ic.setSelection(cursorPosition - 1, cursorPosition - 1);
                        ic.setSelection(cursorPosition, cursorPosition);

                        // 選択中の文字列が存在するとき
                        CharSequence selectedText = ic.getSelectedText(0);
                        if (selectedText != null &&  selectedText.length() > 0) {
                            // それを削除してコネクションを取り直し
                            ic.deleteSurroundingText(1, 0);
                            ic = getCurrentInputConnection(); // コネクション取り直し
                        }

                        // カーソルが先頭以外にある時
                        if (ic != null) {
                            CharSequence beforeCursorText = ic.getTextBeforeCursor(0xfffff, 0);
                            CharSequence afterCursorText = ic.getTextAfterCursor(0xfffff, 0);

                            int wordLength = 0;

                            // カーソル位置から単語を削除
                            for (int i = beforeCursorText.length() - 1; i >= -1; i--, wordLength++) {
                                if (i == -1 || Common.symbols.indexOf(beforeCursorText.charAt(i)) != -1) {
                                    break;
                                }
                                ic.deleteSurroundingText(1, 0);
                            }

                            // カーソル位置から単語を削除
                            for (int i = 0; i < afterCursorText.length(); i++, wordLength++) {
                                if (i == afterCursorText.length() || Common.symbols.indexOf(afterCursorText.charAt(i)) != -1) {
                                    break;
                                }
                                ic.deleteSurroundingText(0, 1);
                            }

                            // 候補が選択されたときに入力を確定
                            ic.commitText(suggestion, 1);

                            // スペースがなかったらスペースを入れる
                            if ((ic.getTextAfterCursor(0xfffff, 0).length() == 0 || ic.getTextAfterCursor(0xfffff, 0).charAt(0) != ' ')) {
                                // スペースを挿入
                                ic.commitText(" ", 1);
                            } else {
                                // スペースの後にカーソル移動
                                cursorPosition = (ic.getTextBeforeCursor(0xfffff, 0).length());
                                ic.setSelection(cursorPosition + 1, cursorPosition + 1);
                            }

                            updateCandidates(Arrays.asList(Common.tokiPonaMarkers));
                        }
                    });
                    candidateList.addView(button);
                }
            }
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

        // 候補ボタンを追加
        for (String candidate : candidates) {
            Button button = Common.candidateButton(this, candidate);
            button.setTextSize(Common.fontSize);
            button.setAllCaps(false);
            button.setOnClickListener(v -> getCurrentInputConnection().commitText(candidate + " ", 1));
            candidateList.addView(button);
        }
    }

}