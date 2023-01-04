package com.example.game.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.game.MainScreenViewModel
import com.example.game.R
import com.example.game.databinding.FragmentMainScreenBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class MainScreenFragment : Fragment() {

    private val viewModel: MainScreenViewModel by viewModels()
    lateinit var binding: FragmentMainScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWebView()
        viewModel.getShowGame().observe(viewLifecycleOwner) {
            when (it) {
                0 -> findNavController().navigate(R.id.action_mainScreenFragment_to_gameFragment)
                1 -> binding.webView.visibility = View.VISIBLE
            }
        }
    }

    private fun setupWebView() {
        viewModel.link.observe(viewLifecycleOwner) {
            binding.webView.loadUrl(it)
        }
        binding.webView.visibility = View.GONE
        binding.webView.webViewClient = WebViewClient()
        binding.webView.webChromeClient =
            object : WebChromeClient() {

                override fun onShowFileChooser(
                    webView: WebView,
                    filePathCallback: ValueCallback<Array<Uri>>,
                    fileChooserParams: FileChooserParams
                ): Boolean {
                    if (valueCallBack != null) {
                        valueCallBack!!.onReceiveValue(null)
                    }
                    valueCallBack = filePathCallback
                    var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (takePictureIntent!!.resolveActivity(requireActivity().packageManager) != null) {
                        var photoFile: File? = null
                        try {
                            photoFile = createImageFile()
                            takePictureIntent.putExtra("PhotoPath", path)
                        } catch (ex: IOException) {
                        }
                        if (photoFile != null) {
                            path = "file:" + photoFile.getAbsolutePath()
                            takePictureIntent.putExtra(
                                MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile)
                            )
                        } else {
                            takePictureIntent = null
                        }
                    }
                    val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
                    contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
                    contentSelectionIntent.type = "/"
                    val intentArray: Array<Intent>
                    val intent = Intent()
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intentArray = takePictureIntent?.let { arrayOf(it, intent) } ?: arrayOf<Intent>()
                    val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                    chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
                    startActivityForResult(chooserIntent, 9991)
                    return true
                }
            }


        binding.webView.settings.loadsImagesAutomatically = true
        binding.webView.settings.setEnableSmoothTransition(true)
        binding.webView.settings.pluginState = android.webkit.WebSettings.PluginState.ON
        binding.webView.settings.setAppCacheEnabled(true)
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.allowFileAccess = true
        binding.webView.settings.allowContentAccess = true
        binding.webView.settings.javaScriptEnabled = true
        android.webkit.CookieManager.getInstance().setAcceptCookie(true)
        binding.webView.settings.cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
        binding.webView.settings.allowFileAccessFromFileURLs = true
        binding.webView.settings.allowUniversalAccessFromFileURLs = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true


        binding.webView.setDownloadListener { url: String?, _: String?, _: String?, _: String?, _: Long ->
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context?.startActivity(i)
        }

        setupWebViewSettings(binding.webView, binding.root.context)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (Build.VERSION.SDK_INT >= 21) {
            var results: Array<Uri>? = null
            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == 9991) {
                    if (null == valueCallBack) {
                        return
                    }
                    if (intent == null) {
                        if (path != null) {
                            results = arrayOf(Uri.parse(path))
                        }
                    } else {
                        val dataString = intent.dataString
                        if (dataString != null) {
                            results = arrayOf(Uri.parse(dataString))
                        }
                    }
                }
            }
            valueCallBack!!.onReceiveValue(results)
            valueCallBack = null
        }
    }

    private fun createImageFile(): File? {
        @SuppressLint("SimpleDateFormat") val timeStamp: String =
            SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val imageFileName = "img" + timeStamp + "_"
        val storageDir: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    private var valueCallBack: ValueCallback<Array<Uri>>? = null
    private var path: String? = null

    @SuppressLint("SetJavaScriptEnabled")
    fun setupWebViewSettings(
        webView: WebView,
        context: Context,
    ) {

    }

}