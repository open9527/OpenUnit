package com.open.pkg.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringDef


object PkgShare {
    private const val PKG_SHARE_TITLE = "\n ---来自OpenUnit的分享---"

    fun shareText(context: Context, text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = ShareType.SHARE_TYPE_TEXT
        shareIntent.putExtra(Intent.EXTRA_TEXT, text + PKG_SHARE_TITLE)
        context.startActivity(Intent.createChooser(shareIntent, PKG_SHARE_TITLE))
    }

    fun shareMedia(context: Context, @ShareType shareType: String, uri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = shareType
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        context.startActivity(Intent.createChooser(shareIntent, PKG_SHARE_TITLE))
    }

    fun shareMultipleMedia(
        context: Context,
        @ShareType shareType: String,
        uris: ArrayList<Uri>
    ) {
        val shareIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
        shareIntent.type = shareType
        context.startActivity(Intent.createChooser(shareIntent, PKG_SHARE_TITLE))
    }


    fun shareThirdPartyText(
        context: Context,
        @SharePackage sharePackage: String,
        text: String
    ) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setPackage(sharePackage)
        shareIntent.type = ShareType.SHARE_TYPE_TEXT
        shareIntent.putExtra(Intent.EXTRA_TEXT, text + PKG_SHARE_TITLE)
        context.startActivity(shareIntent)
    }

    fun shareThirdPartyMedia(
        context: Context,
        @SharePackage sharePackage: String,
        @ShareType shareType: String,
        uri: Uri
    ) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setPackage(sharePackage)
        shareIntent.type = shareType
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        context.startActivity(Intent.createChooser(shareIntent, PKG_SHARE_TITLE))
    }

    fun shareThirdPartyMultipleMedia(
        context: Context,
        @SharePackage sharePackage: String,
        @ShareType shareType: String,
        uris: ArrayList<Uri>
    ) {
        val shareIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
        shareIntent.setPackage(sharePackage)
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
        shareIntent.type = shareType
        context.startActivity(Intent.createChooser(shareIntent, PKG_SHARE_TITLE))
    }


    @Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
    @MustBeDocumented
    @StringDef(
        ShareType.SHARE_TYPE_ALL,
        ShareType.SHARE_TYPE_TEXT,
        ShareType.SHARE_TYPE_IMAGE,
        ShareType.SHARE_TYPE_VIDEO,
        ShareType.SHARE_TYPE_AUDIO

    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class ShareType {
        companion object {
            const val SHARE_TYPE_ALL = "*/*"
            const val SHARE_TYPE_TEXT = "text/plain"
            const val SHARE_TYPE_IMAGE = "image/*"
            const val SHARE_TYPE_VIDEO = "video/*"
            const val SHARE_TYPE_AUDIO = "audio/*"
        }
    }


    @Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
    @MustBeDocumented
    @StringDef(
        SharePackage.PACKAGE_WECHAT,
        SharePackage.PACKAGE_QQ,
        SharePackage.PACKAGE_TIM,
        SharePackage.PACKAGE_WEIBO

    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class SharePackage {
        companion object {
            const val PACKAGE_WECHAT = "com.tencent.mm"
            const val PACKAGE_QQ = "com.tencent.mobileqq"
            const val PACKAGE_TIM = "com.tencent.tim"
            const val PACKAGE_WEIBO = "com.sina.weibo"
        }
    }
}