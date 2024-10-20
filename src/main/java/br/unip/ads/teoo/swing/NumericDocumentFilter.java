package br.unip.ads.teoo.swing;

import javax.swing.text.DocumentFilter;
import javax.swing.text.BadLocationException;

public class NumericDocumentFilter extends DocumentFilter {

    private int maxLength;

    public NumericDocumentFilter(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws BadLocationException {
        if (string == null) {
            return;
        }
        if (string.matches("[0-9]+") && (fb.getDocument().getLength() + string.length() <= maxLength)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws BadLocationException {
        if (text == null) {
            return;
        }
        if (text.matches("[0-9]+") && (fb.getDocument().getLength() + text.length() - length <= maxLength)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
    }
}
