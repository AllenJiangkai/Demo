package com.de.danaemas.module.main

import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment

data class TabContent(val tabName: String, @DrawableRes val tabIcon: Int, val fragment: Fragment,
                      val isLightStatusBar: Boolean)