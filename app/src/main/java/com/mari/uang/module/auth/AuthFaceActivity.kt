package com.mari.uang.module.auth

import ai.advance.liveness.lib.GuardianLivenessDetectionSDK
import ai.advance.liveness.lib.LivenessResult
import ai.advance.liveness.lib.Market
import ai.advance.liveness.sdk.activity.LivenessActivity
import android.Manifest
import android.content.Intent
import android.text.TextUtils
import androidx.lifecycle.Observer
import com.mari.uang.BuildConfig
import com.mari.uang.R
import com.mari.uang.config.ConstantConfig.ORDER_NO_KEY
import com.mari.uang.config.ConstantConfig.PRODUCT_ID_KEY
import com.mari.uang.config.ConstantConfig.STATUS_KEY
import com.mari.uang.config.ConstantConfig.TITLE_KEY
import com.mari.uang.module.auth.dialog.AuthFaceDialog
import com.mari.uang.module.auth.dialog.CardConfirmDialog
import com.mari.uang.module.auth.dto.AuthCardInfo
import com.mari.uang.util.PermissionUtil.requestPermission
import com.mari.uang.widget.TipsDialog
import com.mari.lib_utils.cache.SDCardUtils.*
import com.coupang.common.base.BaseSimpleActivity
import com.coupang.common.extentions.createViewModel
import com.coupang.common.utils.ContextUtils
import com.coupang.common.utils.GlideLoadUtils.loadImage
import com.coupang.common.utils.setHtmlText
import com.coupang.common.utils.setStatusBarTextColor
import com.coupang.common.utils.strings
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.mari.uang.event.ActionEnum
import com.mari.uang.event.ActionUtil
import com.yanzhenjie.permission.Action
import kotlinx.android.synthetic.main.activity_face_auth.*
import java.io.File

class AuthFaceActivity : BaseSimpleActivity() {
    private val viewModel by lazy { createViewModel(AuthModel::class.java) }
    private var productId: String? = null
    private var orderNo: String? = null
    private var status: String? = null
    private var confirmDialog: CardConfirmDialog? = null

    companion object {
        const val TYPE_UPLOAD_LIVENESS = 10
        const val TO_LIVENESS_REQUEST_CODE = 10003
        const val TYPE_UPLOAD_IDCARD = 11
        const val TO_IDCARD_REQUEST_CODE = 10002
    }

    override fun onResume() {
        super.onResume()
        setStatusBarTextColor(window, true)
    }


    private val mustPermissions =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun getLayoutId(): Int {
        return R.layout.activity_face_auth
    }

    override fun initView() {
        GuardianLivenessDetectionSDK.init(
            ContextUtils.getApplication(),
            BuildConfig.livenessAccessKey,
            BuildConfig.livenessSecretKey,
            Market.Indonesia
        )


        title_bar.apply {
            setTitle(context.getString(R.string.title_auth_face))
            onClickBackListener {
                finish()
            }
        }

        setHtmlText(tv_tips, strings(R.string.auth_face_tips))
        initIntent()
        initListener()
    }

    private fun initListener() {
        ll_face.setOnClickListener {
            pageCreateTime = System.currentTimeMillis()
            requestPermission(this, mustPermissions, Action {
                viewModel.sendLog()
                startActivityForResult( Intent(this@AuthFaceActivity, LivenessActivity::class.java), TO_LIVENESS_REQUEST_CODE)

            })
        }

        bt_submit.setOnClickListener {
            viewModel.sendSubmit()
        }

        ll_phone.setOnClickListener {
            showOcrNoticeDialog()
        }
        ll_name.setOnClickListener {
            showOcrNoticeDialog()
        }
        ll_card.setOnClickListener {
            showOcrNoticeDialog()
        }
        ll_birthday.setOnClickListener {
            showOcrNoticeDialog()
        }
        rl_card.setOnClickListener {
            showOcrNoticeDialog()
        }
    }


    private fun initIntent() {
        status = intent.getStringExtra(STATUS_KEY)
        orderNo = intent.getStringExtra(ORDER_NO_KEY)
        productId = intent.getStringExtra(PRODUCT_ID_KEY)
        val title: String = intent.getStringExtra(TITLE_KEY)
        title_bar.setTitle(title)
    }


    override fun registerObserver() {
        registerLiveDataCommonObserver(viewModel)
        viewModel.networkError.observe(this, Observer {


        })
        viewModel.submitInfo.observe(this, Observer {
            if (it.status.equals("1")) {
                showToast(strings(R.string.auth_face_save_success))
                finish()
            } else if ("0" == status && !(it.message == null || "" == it.message)) {
                TipsDialog(this).isCancelable(false)
                    .setTitle(getString(R.string.auth_face_dialog_title))
                    .setNegativeButton(getString(R.string.dialog_cancel))
                    .setPositiveButton(strings(R.string.auth_face_dialog_upload)) {
                        showOcrNoticeDialog()
                        true
                    }.show()
            }

        })
        viewModel.canClickInfo.observe(this, Observer {
//            if (it.ifCanClick.equals("1")) {
//                startActivityForResult( Intent(this@AuthFaceActivity, LivenessActivity::class.java), TO_LIVENESS_REQUEST_CODE)
//            }
        })
        viewModel.authCardInfo.observe(this, Observer {
            if (it.type == TYPE_UPLOAD_IDCARD) {
                initCardInfo(it)
                showConfirmDialog(it)
                ActionUtil.actionRecord(ActionEnum.Front, productId, pageCreateTime)
                loadImage(this, it.imagePath, img_card)
            } else {
                ActionUtil.actionRecord(ActionEnum.Face, productId, pageCreateTime)
                loadImage(this, it.imagePath, img_card_face)
            }
        })
        viewModel.submitCardInfo.observe(this, Observer {
            confirmDialog?.dismiss()
            initCardInfo(it)
        })

        viewModel.faceAuthInfo.observe(this, Observer {
            bt_submit.isEnabled = true

            if (!TextUtils.isEmpty(it.message)) {
                TipsDialog(this@AuthFaceActivity)
                    .setTitle(strings(R.string.auth_face_dialog_title))
                    .setMessage(it.message)
                    .setNegativeButton(strings(R.string.dialog_cancel))
                    .setPositiveButton(getString(R.string.auth_face_dialog_upload)) {
                        showOcrNoticeDialog()
                        true
                    }
                    .show()
            }
            if (it.item != null) {
                if (!TextUtils.isEmpty(it.item?.id_number_z_picture)) {
                    loadImage(this, it.item?.id_number_z_picture, img_card)
                }
                if (!TextUtils.isEmpty(it.item?.id_number_z_picture)) {
                    loadImage(this, it.item?.face_recognition_picture, img_card_face)
                }
            }

            initCardInfo(AuthCardInfo().apply {
                name = it.info?.name
                idCardNumber = it.info?.id_number
                if (!TextUtils.isEmpty(it.info?.birthday)) {
                    it.info?.birthday?.split("-").also { list ->
                        year = list?.get(2) ?: ""
                        month = list?.get(1) ?: ""
                        day = list?.get(0) ?: ""
                    }
                }
            })
            tv_phone.text = it.info?.mobile
        })
    }


    private fun initCardInfo(bean: AuthCardInfo) {
        tv_name.text = bean.name
        tv_card.text = bean.idCardNumber
        tv_birthday.text = bean.getBirthdayStr()
    }


    override fun initData() {
        viewModel.getPersonInfo(productId ?: "")
    }

    private fun showOcrNoticeDialog() {
        pageCreateTime = System.currentTimeMillis()
        AuthFaceDialog(this).onClickSubmitListener {
            requestCamera()
        }.show()
    }


    private fun requestCamera() {
        requestPermission(this, mustPermissions, Action {
            PictureSelector.create(this@AuthFaceActivity)
                .openCamera(PictureMimeType.ofImage())
                .isCompress(true)
                .compressQuality(50)
                .forResult(PictureConfig.REQUEST_CAMERA)
        })
    }

    private fun showConfirmDialog(cardInfoModel: AuthCardInfo) {
        confirmDialog = CardConfirmDialog(this).setData(cardInfoModel).onClickSubmitListener {

            if (TextUtils.isEmpty(orderNo) ||
                TextUtils.isEmpty(cardInfoModel.name) ||
                TextUtils.isEmpty(cardInfoModel.idCardNumber) ||
                TextUtils.isEmpty(cardInfoModel.getBirthdayStr())
            ) {
                showToast(getString(R.string.auth_face_cardinfo_null))
            }else{
                viewModel.submitCardInfo(it,orderNo,productId)
            }
        }.show {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == -1) {
            when (requestCode) {
                PictureConfig.REQUEST_CAMERA -> {
                    // 结果回调
                    val selectList = PictureSelector.obtainMultipleResult(data)
                    if (selectList != null && selectList.isNotEmpty())
                        viewModel.uploadImage(
                            selectList[0].compressPath, TYPE_UPLOAD_IDCARD
                        )
                }
            }
        }

        if (requestCode == TO_LIVENESS_REQUEST_CODE) {
            if (LivenessResult.isSuccess()) { // 活体检测成功
                val livenessBitmap = LivenessResult.getLivenessBitmap() // 本次活体图片
                val filePath: String = getImageDataPath() + System.currentTimeMillis() + ".jpg"
                createOrExistsDir(File(getImageDataPath()))
                if (saveImage(filePath, livenessBitmap)) {
                    viewModel.uploadImage(filePath, TYPE_UPLOAD_LIVENESS)
                } else {
                    showToast("Gagal menyimpan gambar")
                }
            } else {
                val errorMsg = LivenessResult.getErrorMsg()?:""
                showToast(errorMsg)
            }
        }


    }


}