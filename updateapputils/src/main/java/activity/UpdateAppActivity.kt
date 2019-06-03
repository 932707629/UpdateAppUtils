package activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.TextView
import customview.ConfirmDialog
import model.UpdateBean
import teprinciple.updateapputils.R
import util.DownloadAppUtils
import util.UpdateAppService
import util.UpdateAppUtils

class UpdateAppActivity : AppCompatActivity() {

    private var content: TextView? = null
    private var sureBtn: TextView? = null
    private var cancleBtn: TextView? = null

    private var updateBean: UpdateBean? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.view_version_tips_dialog)

        updateBean = intent.getParcelableExtra(KEY_OF_INTENT_UPDATE_BEAN)

        initView()
        initOperation()
    }


    private fun initView() {
        sureBtn = findViewById(R.id.dialog_confirm_sure) as TextView
        cancleBtn = findViewById(R.id.dialog_confirm_cancle) as TextView
        content = findViewById(R.id.dialog_confirm_title) as TextView


        var contentStr = "发现新版本:" + updateBean!!.serverVersionName + "\n是否下载更新?"
        if (!TextUtils.isEmpty(updateBean!!.updateInfo)) {
            contentStr = "发现新版本:" + updateBean!!.serverVersionName + "是否下载更新?\n\n" + updateBean!!.updateInfo
        }

        content!!.text = contentStr

        if (updateBean!!.force!!) {
            cancleBtn!!.text = "退出"
        } else {
            cancleBtn!!.text = "取消"
        }

    }


    private fun initOperation() {


        cancleBtn!!.setOnClickListener {
            if (updateBean!!.force!!) {
                System.exit(0)
            } else {
                finish()
            }
        }

        sureBtn!!.setOnClickListener { preDownLoad() }
    }


    /**
     * 预备下载 进行 6.0权限检查
     */
    private fun preDownLoad() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            download()
        } else {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                download()

            } else {//申请权限
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_CODE)
            }
        }
    }


    private fun download() {

        startService(Intent(this, UpdateAppService::class.java))

        if (updateBean!!.downloadBy == UpdateAppUtils.DOWNLOAD_BY_APP) {
            if (isWifiConnected(this)) {

                DownloadAppUtils.download(this, updateBean!!.apkPath, updateBean!!.serverVersionName)
            } else {
                ConfirmDialog(this, ConfirmDialog.Callback { position ->
                    if (position == 1) {
                        DownloadAppUtils.download(this@UpdateAppActivity, updateBean!!.apkPath, updateBean!!.serverVersionName)
                    } else {
                        if (updateBean!!.force!!) {
                            System.exit(0)
                        } else {
                            finish()
                        }
                    }
                }).setContent("目前手机不是WiFi状态\n确认是否继续下载更新？").show()
            }
        } else if (updateBean!!.downloadBy == UpdateAppUtils.DOWNLOAD_BY_BROWSER) {
            DownloadAppUtils.downloadForWebView(this, updateBean!!.apkPath)
        }

        finish()
    }


    /**
     * 权限请求结果
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                download()
            } else {
                ConfirmDialog(this, ConfirmDialog.Callback { position ->
                    if (position == 1) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.parse("package:$packageName") // 根据包名打开对应的设置界面
                        startActivity(intent)
                    }
                }).setContent("暂无读写SD卡权限\n是否前往设置？").show()
            }
        }
    }

    /**
     * 检测wifi是否连接
     */
    private fun isWifiConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cm != null) {
            val networkInfo = cm.activeNetworkInfo
            if (networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                return true
            }
        }
        return false
    }

    companion object {

        private val KEY_OF_INTENT_UPDATE_BEAN = "KEY_OF_INTENT_UPDATE_BEAN"


        fun launch(context: Context, updateBean: UpdateBean) {
            val intent = Intent(context, UpdateAppActivity::class.java)
            intent.putExtra(KEY_OF_INTENT_UPDATE_BEAN, updateBean)
            context.startActivity(intent)
        }


        private val PERMISSION_CODE = 1001
    }
}