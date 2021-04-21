package com.mari.lib_utils.tools.front

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * package: com.easytools.tools.ClipboardUtils
 * author: gyc
 * description:剪贴板相关工具类
 * time: create at 2016/10/17 17:05
 */
object ClipboardUtils {
    /**
     * 复制文本到剪贴板
     *
     * @param context 上下文
     * @param text    文本
     */
    fun copyText(context: Context, text: CharSequence?) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText("text", text))
    }

    /**
     * 获取剪贴板的文本
     *
     * @param context 上下文
     * @return 剪贴板的文本
     */
    fun getText(context: Context): CharSequence? {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = clipboardManager.primaryClip
        return if (clipData != null && clipData.itemCount > 0) {
            clipData.getItemAt(0).coerceToText(context)
        } else null
    }

    /**
     * 复制uri到剪贴板
     *
     * @param context 上下文
     * @param uri     uri
     */
    fun copyUri(context: Context, uri: Uri?) {
        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newUri(context.contentResolver, "uri", uri))
    }

    /**
     * 获取剪贴板的uri
     *
     * @param context 上下文
     * @return 剪贴板的uri
     */
    fun getUri(context: Context): Uri? {
        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = clipboard.primaryClip
        return if (clipData != null && clipData.itemCount > 0) {
            clipData.getItemAt(0).uri
        } else null
    }

    /**
     * 复制意图到剪贴板
     *
     * @param context 上下文
     * @param intent  意图
     */
    fun copyIntent(context: Context, intent: Intent?) {
        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newIntent("intent", intent))
    }

    /**
     * 获取剪贴板的意图
     *
     * @param context 上下文
     * @return 剪贴板的意图
     */
    fun getIntent(context: Context): Intent? {
        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = clipboard.primaryClip
        return if (clip != null && clip.itemCount > 0) {
            clip.getItemAt(0).intent
        } else null
    }
}