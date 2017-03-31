package com.grandmagic.readingmate.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpanTextView extends AppCompatTextView {
    private SpannableString spannableString;
    private Context context;
    private final String AiteRule = "@[^,，：:\\s@]+";
    private String MatchRule = null;
    private String str = "";
    private MovementMethod mMovement;

    private String mContent;

    public SpanTextView(Context context) {
        super(context);
        this.context = context;
    }

    public SpanTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public SpanTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    public boolean hasFocusable() {
        return false;
    }

    /**
     * @param text
     * @return
     */
    public void setText(String text) {
        if (getMatchRule() == null) {
            /* 默认一个@的匹配规则 */
            setMatchRule(AiteRule);
        }
        super.setText(setHighlight(text, getMatchRule()));
    }

    public void setText(String text, String append) {
        if (getMatchRule() == null) {
            /* 默认一个@的匹配规则 */
            setMatchRule(AiteRule);
        }
        super.setText(appendColorText(text, getMatchRule(), append));
    }


    public SpannableString appendColorText(String text, String rule, String append) {
        if (TextUtils.isEmpty(append)) {
            return setHighlight(text, rule);
        }
        SpannableString ss = setHighlight(text + append, rule);
        int len = text.length();
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#1cc9a2")), len, len + append.length() ,Spanned.SPAN_MARK_POINT);
        return ss;
    }


    /**
     * '@XXX
     *
     * @param text 文字
     * @param rule 匹配规则
     * @return
     */
    private SpannableString setHighlight(String text, String rule) {
        spannableString = new SpannableString(text);
        Pattern pattern = Pattern.compile(rule);
        Matcher matcher = pattern.matcher(text);
        int length = text.length();
        int end = 0;
        for (int i = 0; i < length; i++) {
            if (matcher.find()) {
                int start = matcher.start();
                end = matcher.end();
                str = text.substring(start + 1, end);
                spannableString.setSpan(new ClickSpan(context, str), start, end, Spanned.SPAN_MARK_POINT);
            }
        }
        return spannableString;
    }

    /**
     * 这个就是解决之道
     **/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean handled = false;
        mMovement = getMovementMethod();
        if (mMovement != null) {
            handled |= mMovement.onTouchEvent(this, (Spannable) getText(), event);
        }
        ClickableSpan[] links = ((Spannable) getText()).getSpans(getSelectionStart(),
                getSelectionEnd(), ClickableSpan.class);
        if (links.length > 0) {
            handled = true;
        }
        if (handled) {
            return true;
        } else {
            final boolean superResult = super.onTouchEvent(event);
            return superResult;
        }
    }

    public String getMatchRule() {
        return MatchRule;
    }

    public void setMatchRule(String matchRule) {
        this.MatchRule = matchRule;
    }
}