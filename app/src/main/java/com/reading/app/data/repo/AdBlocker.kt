package com.reading.app.data.repo

/**
 * 内置广告域名黑名单，配合 WebView shouldInterceptRequest 拦截广告请求。
 * 同时通过 Jsoup 剥离 script/iframe/adsbygoogle 等标签实现正文净化。
 */
object AdBlocker {
    private val adHosts = setOf(
        "googleads.g.doubleclick.net", "pagead2.googlesyndication.com",
        "googleadservices.com", "googlesyndication.com", "doubleclick.net",
        "google-analytics.com", "googletagmanager.com", "googletagservices.com",
        "hm.baidu.com", "pos.baidu.com", "cbjs.baidu.com",
        "cnzz.com", "umeng.com", "umeng.co", "alog.umeng.com",
        "click.a.vidaa.com", "tanx.com", "alimama.com", "mmstat.com",
        "admaster.com.cn", "mediav.com", "miaozhen.com", "ipinyou.com",
        "adsame.com", "vdhtml.com", "vdvideo.com", "lianmeng.com",
        "cpro.baidu.com", "cpro.baidustatic.com", "bdstatic.com"
    )

    fun shouldBlock(url: String?): Boolean {
        if (url.isNullOrEmpty()) return false
        return try {
            val host = java.net.URI(url).host?.lowercase() ?: return false
            adHosts.any { host == it || host.endsWith(".$it") }
        } catch (e: Exception) { false }
    }
}