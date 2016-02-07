package com.poitot.cryptoexample.utils;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import timber.log.Timber;

/**
 * Copy/paste helper methods
 */
public class ClipboardUtils {

    private static final String CLIPBOARD_LABEL = "CryptoTestClipboard";

    /**
     * Copies text to the clipboard
     */
    public static void copyToClipboard(final Context context, String text) {
        if (context == null) {
            Timber.e("copyToClipboard() -- Context cannot be null");
            return;
        }
        final ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Application.CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText(CLIPBOARD_LABEL, text);

        Timber.d("Copied text to clipboard: %s", text);

        clipboard.setPrimaryClip(clip);
    }

    /**
     * Gets text data from the clipboard. This is a rudimentary implementation that assuems that
     * we're dealing with just text data... no rich media.
     */
    public static String getTextFromClipboard(final Context context) {
        if (context == null) {
            Timber.e("getTextFromClipboard() -- Context cannot be null");
            return "";
        }

        String pasteData = "";
        final ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Application.CLIPBOARD_SERVICE);
        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

        if (item == null || item.getText() == null) {
            Timber.e("getTextFromClipboard() -- ClipData.Item was null for some reason");
            return "";
        }

        return item.getText().toString();
    }
}
