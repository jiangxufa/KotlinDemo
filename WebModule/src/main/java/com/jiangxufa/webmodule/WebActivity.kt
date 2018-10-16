package com.jiangxufa.webmodule

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ToastUtils
import com.jiangxufa.baselibrary.utils.AppUtils
import com.jiangxufa.baselibrary.utils.ClipboardUtils
import com.jiangxufa.baselibrary.utils.StatusBarUtil
import com.jiangxufa.businesslibrary.router.RouterPath
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.web_toolbar.*
// 在支持路由的页面上添加注解(必选)
// 这里的路径需要注意的是至少需要有两级，/xx/xx
@Route(path = RouterPath.WebCenter.PATH_WEB)
class WebActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_URL = "extra_url"
        private const val EXTRA_TITLE = "extra_title"
        private const val EXTRA_IMAGE = "extra_image"

        fun startActivity(context: Context, url: String, title: String, img: String) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(EXTRA_URL, url)
            intent.putExtra(EXTRA_TITLE, title)
            intent.putExtra(EXTRA_IMAGE, img)
            context.startActivity(intent)
        }
    }

    private var mTitle: String? = null
    private lateinit var mUrl: String
    private lateinit var mImg: String

    private val mChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            if (newProgress >= 40) {
                fl_loading.visibility = View.GONE
            } else {
                fl_loading.visibility = View.VISIBLE
            }
            webview.settings.blockNetworkImage = false
            super.onProgressChanged(webview, newProgress)
        }
    }

    private val mWebViewClient = object : WebViewClient() {

        override fun onPageFinished(webView: WebView, s: String) {
            super.onPageFinished(webView, s)
            fl_loading.visibility = View.GONE
            webView.settings.blockNetworkImage = false
            val h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            val w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            webView.measure(w, h)
        }

        override fun onReceivedError(webView: WebView, i: Int, s: String, s1: String) {
            super.onReceivedError(webView, i, s, s1)
            fl_loading.visibility = View.GONE
            val errorHtml = "<html><body><h2>找不到网页</h2></body></html>"
            webView.loadDataWithBaseURL(null, errorHtml, "text/html", "UTF-8", null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        initVariables()
        initView()
    }

    private fun initVariables() {
        val intent = intent
        if (intent != null) {
            mTitle = intent.getStringExtra(EXTRA_TITLE)
            mUrl = intent.getStringExtra(EXTRA_URL)
            mImg = intent.getStringExtra(EXTRA_IMAGE)
        }
    }

    private fun initView() {
        with(toolbar) {
            setNavigationIcon(R.mipmap.ic_clip_back_white)
            title = mTitle ?: "详情"
            setSupportActionBar(this)
            setNavigationOnClickListener { finish() }
        }
        initWebView()
        StatusBarUtil.setColorNoTranslucent(this, AppUtils.getColor(R.color.colorPrimary))
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        val webChromeClient = mChromeClient
        val webViewClient = mWebViewClient
        webview.settings.apply {
            //设置js支持
            javaScriptEnabled = true
            // 设置支持javascript脚本
            javaScriptCanOpenWindowsAutomatically = false
            //设置缓存
            cacheMode = WebSettings.LOAD_NO_CACHE
            domStorageEnabled = true
            setGeolocationEnabled(true)
            useWideViewPort = true//关键点
            loadWithOverviewMode = true//全屏
            builtInZoomControls = true// 设置显示缩放按钮
            setSupportZoom(true)//支持缩放
            displayZoomControls = false
            setAppCacheEnabled(true)
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        }
        with(webview) {
            isDrawingCacheEnabled = true
            settings.blockNetworkImage = true
            setWebViewClient(webViewClient)
            requestFocus(View.FOCUS_DOWN)
            settings.defaultTextEncodingName = "UTF-8"
            setWebChromeClient(webChromeClient)
            loadUrl(mUrl)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack()// 返回前一个页面
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_brower, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_open -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(mUrl)
                startActivity(intent)
            }
            R.id.menu_share -> {
                showShare()
            }
            R.id.menu_copy -> {
                ClipboardUtils.copyText(mUrl)
                ToastUtils.showLong("复制成功")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showShare() {
//        //  ShareSDK.initSDK(this);
//        val oks = OnekeyShare()
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize()
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
//        oks.setTitle("来自" + getString(R.string.app_name) + "的分享")
//        // titleUrl是标题的网络链接，QQ和QQ空间等使用
//        oks.setTitleUrl(mUrl)
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText(mTitle + "" + mUrl)
//        oks.setImageUrl(mImg)
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl(mUrl)
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("文本")
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(getString(R.string.app_name))
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl(mUrl)
//        // 启动分享GUI
//        oks.show(this)
    }

    override fun onDestroy() {
        webview.destroy()
        super.onDestroy()
    }
}
