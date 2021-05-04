package com.mari.uang.event;

import android.location.Location;

/**
 * @ProjectName: My Application
 * @Package: com.kejutan.common.utils.loc
 * @ClassName: LocationCallBack
 * @Description: java类作用描述
 * @Author: jtao
 * @CreateDate: 2021/1/13 5:22 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/13 5:22 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface LocationCall {
    void error(String errorMsg);
    void location(Location location, String addressDetail, String addressJson);
}
