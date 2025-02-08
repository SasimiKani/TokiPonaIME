package com.tokiponaime.tokiponaime;

import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.List;

public class InputButtons {

    private static LayoutPreferences layoutPreferences;

    /**
     * キーボタンを作成する処理
     *
     * @param activity    ぱらめーた
     * @param inputView   ぱらめーた
     * @param screenWidth   ぱらめーた
     */
    public static void initInputButtons(TokiPonaIME activity, View inputView, View nextView, int screenWidth, int screenHeight) {

        layoutPreferences = new LayoutPreferences(activity);

        if (screenWidth < screenHeight) {
            resizeButtons(inputView, screenWidth, screenHeight, Common.SCREEN_ROTATE_V);
        } else {
            resizeButtons(nextView, screenHeight, screenWidth, Common.SCREEN_ROTATE_H);
        }

        initCandidatesView(activity, inputView);

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

        // ボタンのクリックイベントを設定
        btnA.setOnClickListener(v -> activity.getCurrentInputConnection().commitText("a", 1));
        btnE.setOnClickListener(v -> activity.getCurrentInputConnection().commitText("e", 1));
        btnI.setOnClickListener(v -> activity.getCurrentInputConnection().commitText("i", 1));
        btnO.setOnClickListener(v -> activity.getCurrentInputConnection().commitText("o", 1));
        btnU.setOnClickListener(v -> activity.getCurrentInputConnection().commitText("u", 1));

        btnJ.setOnClickListener(v -> activity.getCurrentInputConnection().commitText("j", 1));
        btnK.setOnClickListener(v -> activity.getCurrentInputConnection().commitText("k", 1));
        btnL.setOnClickListener(v -> activity.getCurrentInputConnection().commitText("l", 1));
        btnM.setOnClickListener(v -> activity.getCurrentInputConnection().commitText("m", 1));
        btnN.setOnClickListener(v -> activity.getCurrentInputConnection().commitText("n", 1));
        btnP.setOnClickListener(v -> activity.getCurrentInputConnection().commitText("p", 1));
        btnS.setOnClickListener(v -> activity.getCurrentInputConnection().commitText("s", 1));
        btnT.setOnClickListener(v -> activity.getCurrentInputConnection().commitText("t", 1));
        btnW.setOnClickListener(v -> activity.getCurrentInputConnection().commitText("w", 1));

        //btnEnter.setOnClickListener(v -> activity.getCurrentInputConnection().commitText("\n", 1));
        btnEnter.setOnClickListener(v -> {
            InputConnection ic = activity.getCurrentInputConnection();
            if (ic == null) {
                return;
            }

            // Enter キーの KeyEvent を作成
            KeyEvent eventDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER);
            KeyEvent eventUp = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER);

            // KeyEvent を送信
            ic.sendKeyEvent(eventDown);
            ic.sendKeyEvent(eventUp);
        });

        btnSpace.setOnClickListener(v -> {
            InputConnection ic = activity.getCurrentInputConnection();
            if (ic == null) {
                return;
            }

            // SPACE キーの KeyEvent を作成
            KeyEvent eventDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SPACE);
            KeyEvent eventUp = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_SPACE);

            // KeyEvent を送信
            ic.sendKeyEvent(eventDown);
            ic.sendKeyEvent(eventUp);
        });

        btnBackSpace.setOnTouchListener(new View.OnTouchListener() {
            final private Handler handler = new Handler();
            private Runnable deleteRunnable;
            // 長押しの判定フラグ

            // DEL キーの削除処理
            public void sendDelKeyEvent(InputConnection inputConnection) {
                // DEL キーの KeyEvent を作成
                KeyEvent eventDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL);
                KeyEvent eventUp = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL);

                // KeyEvent を送信
                inputConnection.sendKeyEvent(eventDown);
                inputConnection.sendKeyEvent(eventUp);
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputConnection inputConnection = activity.getCurrentInputConnection();

                if (inputConnection != null) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // 最初に1文字を削除
                            sendDelKeyEvent(inputConnection);

                            // 長押しの処理を開始
                            deleteRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    CharSequence currentText = inputConnection.getExtractedText(
                                            new ExtractedTextRequest(), 0
                                    ).text;

                                    if (currentText.length() > 0) {
                                        sendDelKeyEvent(inputConnection);
                                    }
                                    handler.postDelayed(this, 60);  // 100msごとに繰り返し削除
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
        btnPeriod.setOnClickListener(v -> activity.getCurrentInputConnection().commitText(".", 1));
        btnComma.setOnClickListener(v -> activity.getCurrentInputConnection().commitText(",", 1));
        btnCoron.setOnClickListener(v -> activity.getCurrentInputConnection().commitText(":", 1));
        btnExclamation.setOnClickListener(v -> activity.getCurrentInputConnection().commitText("!", 1));
        btnQuestion.setOnClickListener(v -> activity.getCurrentInputConnection().commitText("?", 1));

        btnUp.setOnTouchListener(new View.OnTouchListener() {
            final Handler handler = new Handler();
            Runnable longPressRunnable;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    moveCursor(activity, "up"); // 最初に1回カーソル移動

                    longPressRunnable = new Runnable() {
                        @Override
                        public void run() {
                            moveCursor(activity, "up");
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
            final Handler handler = new Handler();
            Runnable longPressRunnable;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    moveCursor(activity, "down"); // 最初に1回カーソル移動

                    longPressRunnable = new Runnable() {
                        @Override
                        public void run() {
                            moveCursor(activity, "down");
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
            final Handler handler = new Handler();
            Runnable longPressRunnable;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    moveCursor(activity, "left"); // 最初に1回カーソル移動

                    longPressRunnable = new Runnable() {
                        @Override
                        public void run() {
                            moveCursor(activity, "left");
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
            final Handler handler = new Handler();
            Runnable longPressRunnable;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    moveCursor(activity, "right"); // 最初に1回カーソル移動

                    longPressRunnable = new Runnable() {
                        @Override
                        public void run() {
                            moveCursor(activity, "right");
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

        // 長押し処理
        btnA.setOnLongClickListener(v -> {
            activity.getCurrentInputConnection().commitText("A", 1);
            return true;
        });
        btnE.setOnLongClickListener(v -> {
            activity.getCurrentInputConnection().commitText("E", 1);
            return true;
        });
        btnI.setOnLongClickListener(v -> {
            activity.getCurrentInputConnection().commitText("I", 1);
            return true;
        });
        btnO.setOnLongClickListener(v -> {
            activity.getCurrentInputConnection().commitText("O", 1);
            return true;
        });
        btnU.setOnLongClickListener(v -> {
            activity.getCurrentInputConnection().commitText("U", 1);
            return true;
        });
        btnJ.setOnLongClickListener(v -> {
            activity.getCurrentInputConnection().commitText("J", 1);
            return true;
        });
        btnK.setOnLongClickListener(v -> {
            activity.getCurrentInputConnection().commitText("K", 1);
            return true;
        });
        btnL.setOnLongClickListener(v -> {
            activity.getCurrentInputConnection().commitText("L", 1);
            return true;
        });
        btnM.setOnLongClickListener(v -> {
            activity.getCurrentInputConnection().commitText("M", 1);
            return true;
        });
        btnN.setOnLongClickListener(v -> {
            activity.getCurrentInputConnection().commitText("N", 1);
            return true;
        });

        btnP.setOnLongClickListener(v -> {
            activity.getCurrentInputConnection().commitText("P", 1);
            return true;
        });
        btnS.setOnLongClickListener(v -> {
            activity.getCurrentInputConnection().commitText("S", 1);
            return true;
        });
        btnT.setOnLongClickListener(v -> {
            activity.getCurrentInputConnection().commitText("T", 1);
            return true;
        });
        btnW.setOnLongClickListener(v -> {
            activity.getCurrentInputConnection().commitText("W", 1);
            return true;
        });
        btnSpace.setOnLongClickListener( v -> {
            // InputMethodManagerを取得
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null) {
                // IME選択画面を表示
                imm.showInputMethodPicker();
            }
            return true;
        });
        btnPeriod.setOnLongClickListener(v -> {
            //activity.getCurrentInputConnection().commitText(".", 1);
            return true;
        });
        btnComma.setOnLongClickListener(v -> {
            //activity.getCurrentInputConnection().commitText(",", 1);
            return true;
        });
        btnCoron.setOnLongClickListener(v -> {
            //activity.getCurrentInputConnection().commitText(":", 1);
            return true;
        });
        btnExclamation.setOnLongClickListener(v -> {
            // 表示するレイアウトを切り替える
            nextView.setVisibility(View.VISIBLE);
            inputView.setVisibility(View.GONE);

            // 候補リストを更新
            activity.updateCandidateList();

            // 表示中のレイアウトを保存
            layoutPreferences.saveLayoutId(nextView.getId());

            return true;
        });
        btnQuestion.setOnLongClickListener(v -> {
            //activity.getCurrentInputConnection().commitText("?", 1);
            return true;
        });
    }

    /**
     * サイズ調整
     */
    private static void resizeButtons(View inputView, int screenWidth, int screenHeight, boolean screenRotate) {

        // フォントサイズ調整
        ((Button)inputView.findViewById(R.id.btn_a)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_e)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_i)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_o)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_u)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_j)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_k)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_l)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_m)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_n)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_p)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_s)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_t)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_w)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_enter)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_space)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_back_space)).setTextSize(Common.fontSize * 0.8f);
        ((Button)inputView.findViewById(R.id.btn_period)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_comma)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_coron)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_exclamation)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_question)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_up)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_down)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_left)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_right)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_left)).setTextSize(Common.fontSize);
        ((Button)inputView.findViewById(R.id.btn_right)).setTextSize(Common.fontSize);

        // ボタンのサイズ調整
        int resize = (int) (screenHeight * Common.AREA_HEIGHT);
        inputView.findViewById(R.id.keyboard_table).getLayoutParams().height = resize;

        if (screenRotate == Common.SCREEN_ROTATE_H) { // 横画面のとき
            resize = (int) (screenHeight * Common.AREA_WIDTH);
            inputView.findViewById(R.id.keyboard_table).getLayoutParams().width = resize;
        }

        resize = (int) (screenHeight * Common.GRID_HEIGHT_50); // 50dp
        inputView.findViewById(R.id.grid_layout_4).getLayoutParams().height = resize;
        inputView.findViewById(R.id.candidate_list).getLayoutParams().height = resize;

        resize = (int) (screenHeight * Common.GRID_HEIGHT_55); // 55dp
        inputView.findViewById(R.id.grid_layout_1).getLayoutParams().height = resize;
        inputView.findViewById(R.id.grid_layout_2).getLayoutParams().height = resize;
        inputView.findViewById(R.id.grid_layout_3).getLayoutParams().height = resize;

        int  tmpScreenWidth = screenWidth;
        if (screenRotate == Common.SCREEN_ROTATE_H) { // 横画面のとき
            screenWidth = screenHeight;
        }

        resize = (int) (screenWidth * Common.KEY_WIDTH);
        inputView.findViewById(R.id.btn_e).getLayoutParams().width = resize;
        inputView.findViewById(R.id.btn_a).getLayoutParams().width = resize;
        inputView.findViewById(R.id.btn_i).getLayoutParams().width = resize;
        inputView.findViewById(R.id.btn_o).getLayoutParams().width = resize;
        inputView.findViewById(R.id.btn_u).getLayoutParams().width = resize;
        inputView.findViewById(R.id.btn_j).getLayoutParams().width = resize;
        inputView.findViewById(R.id.btn_k).getLayoutParams().width = resize;
        inputView.findViewById(R.id.btn_l).getLayoutParams().width = resize;
        inputView.findViewById(R.id.btn_m).getLayoutParams().width = resize;
        inputView.findViewById(R.id.btn_n).getLayoutParams().width = resize;
        inputView.findViewById(R.id.btn_p).getLayoutParams().width = resize;
        inputView.findViewById(R.id.btn_s).getLayoutParams().width = resize;
        inputView.findViewById(R.id.btn_t).getLayoutParams().width = resize;
        inputView.findViewById(R.id.btn_w).getLayoutParams().width = resize;

        resize = (int) (screenWidth * Common.KEY_WIDTH_ENTER);
        inputView.findViewById(R.id.btn_enter).getLayoutParams().width = resize;

        resize = (int) (screenWidth * Common.KEY_WIDTH_BS);
        inputView.findViewById(R.id.btn_back_space).getLayoutParams().width = resize;

        resize = (int) (screenWidth * Common.KEY_WIDTH_LR);
        inputView.findViewById(R.id.btn_left).getLayoutParams().width = resize;
        inputView.findViewById(R.id.btn_right).getLayoutParams().width = resize;
        resize = (int) (screenWidth * Common.KEY_WIDTH_UD);
        inputView.findViewById(R.id.btn_down).getLayoutParams().width = resize;
        inputView.findViewById(R.id.btn_up).getLayoutParams().width = resize;

        resize = (int) (screenWidth * Common.KEY_WIDTH_SYMBOL);
        inputView.findViewById(R.id.btn_period).getLayoutParams().width = resize;
        inputView.findViewById(R.id.btn_comma).getLayoutParams().width = resize;
        inputView.findViewById(R.id.btn_coron).getLayoutParams().width = resize;
        inputView.findViewById(R.id.btn_exclamation).getLayoutParams().width = resize;
        inputView.findViewById(R.id.btn_question).getLayoutParams().width = resize;

        resize = (int) (screenWidth * Common.KEY_WIDTH_SPACE);
        inputView.findViewById(R.id.btn_space).getLayoutParams().width = resize;

        resize = (int) (screenWidth * Common.SPACE_20); // space 20pd
        inputView.findViewById(R.id.space_20_1).getLayoutParams().width = resize;
        inputView.findViewById(R.id.space_20_2).getLayoutParams().width = resize;

        resize = (int) (screenWidth * Common.SPACE_35); // space 35pd
        inputView.findViewById(R.id.space_35).getLayoutParams().width = resize;

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) inputView.findViewById(R.id.grid_layout_1).getLayoutParams();
        params.topMargin = (int) (screenHeight * Common.MARGIN_1);
        inputView.findViewById(R.id.grid_layout_1).setLayoutParams(params);

        params = (FrameLayout.LayoutParams) inputView.findViewById(R.id.grid_layout_3).getLayoutParams();
        params.topMargin = (int) (screenHeight * Common.MARGIN_2);
        params.width = (int) (screenWidth * Common.AREA_WIDTH);
        inputView.findViewById(R.id.grid_layout_3).setLayoutParams(params);

        params = (FrameLayout.LayoutParams) inputView.findViewById(R.id.grid_layout_4).getLayoutParams();
        params.topMargin = (int) (screenHeight * Common.MARGIN_3);
        inputView.findViewById(R.id.grid_layout_4).setLayoutParams(params);

    }

    /**
     * カーソル移動の処理
     * @param activity ぱらめーた
     * @param direction ぱらめーた
     */
    private static void moveCursor(TokiPonaIME activity, String direction) {
        InputConnection inputConnection = activity.getCurrentInputConnection();

        if (inputConnection != null) {
            CharSequence beforeCursorText = inputConnection.getTextBeforeCursor(0xfffff, 0);
            CharSequence afterCursorText = inputConnection.getTextAfterCursor(0xfffff, 0);

            // カーソル位置を取得
            int cursorPosition = (beforeCursorText != null ? beforeCursorText.length() : 0);

            // カーソルを移動
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

                        for (int i = 0; i < afterCursorText.length(); i++) {
                            if (afterCursorText.charAt(i) == '\n') {
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

    /**
     * 入力候補リストを初期化する処理
     * @param activity ぱらめーた
     * @param inputView ぱらめーた
     */
    private static void initCandidatesView(TokiPonaIME activity, View inputView) {
        LinearLayout candidateList = inputView.findViewById(R.id.candidate_list);

        // 仮の候補リスト
        List<String> candidates = Arrays.asList(Common.tokiPonaMarkers);

        // 候補を追加
        for (String candidate : candidates) {
            Button button = Common.candidateButton(activity, candidate);
            button.setTextSize(Common.fontSize);
            button.setAllCaps(false);
            button.setOnClickListener(v -> {
                // 候補が選択されたときの処理
                activity.getCurrentInputConnection().commitText(candidate, 1);
            });
            candidateList.addView(button);
        }
    }

}
