package com.mari.uang.module.contact

import android.Manifest
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.mari.uang.R
import com.mari.uang.config.ConstantConfig
import com.mari.uang.module.contact.dto.ContactInfo
import com.mari.uang.module.contact.dto.NameTypeInfo
import com.mari.uang.util.PermissionUtil
import com.bigkoo.pickerview.OptionsPickerView
import com.coupang.common.base.BaseSimpleActivity
import com.coupang.common.extentions.createViewModel
import com.coupang.common.network.ParameterTool.toRequestBody
import com.coupang.common.utils.setStatusBarTextColor
import com.mari.uang.event.ActionEnum
import com.mari.uang.event.ActionUtil
import com.yanzhenjie.permission.Action
import kotlinx.android.synthetic.main.activity_contact.*
import kotlinx.android.synthetic.main.activity_order.title_bar
import java.util.HashMap
import java.util.regex.Pattern

class ContactActivity : BaseSimpleActivity() {
    private val viewModel by lazy { createViewModel(ContactModel::class.java) }

    private var productId: String? = null
    private var orderNo: String? = null
    private var status: String? = null

    private var TYPE_CONTACT_FIRST = 999
    private var TYPE_CONTACT_SECOND = 1000
    private var currentType = -1
    private var mustPermissions = arrayOf(Manifest.permission.READ_CONTACTS)
    private var firstUpload = false
    private var contactInfo: ContactInfo? = null
    private var firstContact: NameTypeInfo? = null
    private var secondContact: NameTypeInfo? = null


    override fun getLayoutId(): Int {
        return R.layout.activity_contact
    }

    override fun initView() {
        ll_contact_1.setOnClickListener {
            selectOptionDialog(contactInfo?.emergent?.lineal_list, it)
        }
        ll_contact_2.setOnClickListener {
            selectOptionDialog(contactInfo?.emergent?.other_list, it)
        }

        ll_chose_1.setOnClickListener {
            currentType = TYPE_CONTACT_FIRST
            startContacts(currentType)
        }
        ll_chose_2.setOnClickListener {
            currentType = TYPE_CONTACT_SECOND
            startContacts(currentType)
        }

        bt_submit.setOnClickListener {
            if (contactInfo?.emergent?.lineal_list.isNullOrEmpty() ||
                contactInfo?.emergent?.other_list.isNullOrEmpty()) {
                showToast("Silakan menyempurnakan informasi pertama")
            }else {
                val params: MutableMap<String, Any> = HashMap()
                params["first_relation"] =firstContact?.type?:""
                params["first_mobile"] =contactInfo?.emergent?.linealMobile?:""
                params["first_name"] = contactInfo?.emergent?.linealName?:""
                params["second_relation"] =secondContact?.type?:""
                params["second_mobile"] = contactInfo?.emergent?.otherMobile?:""
                params["second_name"] =contactInfo?.emergent?.otherName?:""
                params["product_id"] = productId?:""
                viewModel.saveContactInfo(toRequestBody(params))
            }
        }
        initIntent()

    }

    override fun registerObserver() {
        registerLiveDataCommonObserver(viewModel)
        viewModel.contactInfo.observe(this, Observer {
            contactInfo = it
            it.emergent?.apply {
                getNameTypeBean(lineal_list, linealRelation ?: "")?.let { nameType ->
                    tv_contact_1.text = nameType.name
                    tv_lineal_name_1.text = linealName
                    tv_lineal_mobile_1.text = linealMobile
                    firstContact = nameType
                }
                getNameTypeBean(other_list, otherRelation ?: "")?.let { nameType ->
                    tv_contact_2.text = nameType.name
                    tv_lineal_name_2.text = otherName
                    tv_lineal_mobile_2.text = otherMobile
                    secondContact = nameType
                }
                bt_submit.isEnabled = true
            }

        })

        viewModel.saveInfo.observe(this, Observer {
            ActionUtil.actionRecord(ActionEnum.Contact, productId,pageCreateTime)
            finish()
        })
    }

    override fun initData() {
        viewModel.getPersonInfo(productId)
    }

    override fun onResume() {
        super.onResume()
        setStatusBarTextColor(window, true)
    }

    private fun initIntent() {
        status = intent.getStringExtra(ConstantConfig.STATUS_KEY)
        orderNo = intent.getStringExtra(ConstantConfig.ORDER_NO_KEY)
        productId = intent.getStringExtra(ConstantConfig.PRODUCT_ID_KEY)
        val title: String = intent.getStringExtra(ConstantConfig.TITLE_KEY)
        title_bar.setTitle(title)
    }

    private fun getNameTypeBean(
        list: List<NameTypeInfo>?,
        linealRelation: String
    ): NameTypeInfo? {
        var nameTypeModel: NameTypeInfo? = NameTypeInfo()
        if (list == null || list.isEmpty() || TextUtils.isEmpty(linealRelation)) {
            return nameTypeModel
        }
        for (i in list.indices) {
            val bean: NameTypeInfo = list[i]
            if (bean.type.equals(linealRelation)) {
                nameTypeModel = bean
                break
            }
        }
        return nameTypeModel
    }

    private fun startContacts(reqCode: Int) {
        PermissionUtil.requestPermission(
            this, mustPermissions,
            Action {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
                startActivityForResult(intent, reqCode)
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            requestContacts(data?.data, requestCode)
            //联系人全量数据上报一次
            if (!firstUpload) {
                firstUpload = true
                //todo
//                MUInfoUploadUtil.uploadContacts()
            }
        }
    }

    private fun requestContacts(contactUri: Uri?, reqCode: Int) {
        contactUri?.apply {
            var cursor: Cursor? = null
            try {
                val projection = arrayOf(
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                )
                cursor = this@ContactActivity.contentResolver.query(
                    contactUri, projection,
                    null, null, null
                )
                if (cursor != null && cursor.moveToFirst()) {
                    val nameIndex =
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    val numberIndex =
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val name = cursor.getString(nameIndex)
                    val number = cursor.getString(numberIndex)
                    if (reqCode == 999) {
                        tv_lineal_name_1.text = name
                        tv_lineal_mobile_1.text = toNum(number)
                        contactInfo?.emergent?.linealName = name
                        contactInfo?.emergent?.linealMobile = toNum(number)
                    } else {
                        tv_lineal_name_2.text = name
                        tv_lineal_mobile_2.text = toNum(number)
                        contactInfo?.emergent?.otherName = name
                        contactInfo?.emergent?.otherMobile = toNum(number)
                    }
                    cursor.close()
                }
            } catch (e: Exception) {
            } finally {
                cursor?.close()
            }
        }
    }


    fun toNum(str: String?): String? {
        var str = str
        val regEx = "[^0-9]"
        val p = Pattern.compile(regEx)
        val m = p.matcher(str)
        str = m.replaceAll("").trim { it <= ' ' }
        return str
    }

    private fun selectOptionDialog(list: List<NameTypeInfo>?, view: View) {
        OptionsPickerView.Builder(this) { options1, options2, options3, v ->
            when (view.id) {
                R.id.ll_contact_1 -> {
                    firstContact = list?.get(options1)
                    tv_contact_1.text = firstContact?.name
                }
                R.id.ll_contact_2 -> {
                    secondContact = list?.get(options1)
                    tv_contact_2.text = secondContact?.name
                }
            }

        }.setSubCalSize(16)
            .setSubmitColor(ContextCompat.getColor(this, R.color.main_color))
            .setCancelColor(ContextCompat.getColor(this, R.color.color_999999))
            .setTextColorCenter(ContextCompat.getColor(this, R.color.color_666666))
            .setTextColorOut(ContextCompat.getColor(this, R.color.color_999999))
            .setDividerColor(ContextCompat.getColor(this, R.color.color_f4f5f6))
            .setTitleBgColor(ContextCompat.getColor(this, R.color.white))
            .setContentTextSize(20)
            .setDividerColor(Color.BLACK)
            .setCancelText(this.getString(R.string.dialog_per_left))
            .setSubmitText(this.getString(R.string.dialog_confirm))
            .setOutSideCancelable(true)
            .build().apply {
                setPicker(list)
                show()
            }
    }

}