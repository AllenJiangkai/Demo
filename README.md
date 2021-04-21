# coupong 组件化框架

# 1.框架说明
> 本框架基于mvc+viewModel+liveData,通信方式参考Arouter
> * 1.子工程模版 参考module_test,配置build.gradle
> * 2.loading baseActivity 已实现

# 2.arouter
> * 1.跳转与携带参数，参考arouter文档
> * 2.组件之间的通信及页面的接口调用，建议继承Iporvider暴露服务，通过注解发现实现服务，参考文档

# 3.lib_common
> 此lib只能方公用的res,widget,adpater等，实体类不要存放

# 4.lib_network
> 网络库，根据业务进行调整

# 5.lib_utils
> 公用的工具类，如缓存，图片，系统工具等，自行查看添加

# 编码规范

## 工程管理
  > * 1 统一版本号，所有依赖必须在config.gradle文件添加，如多个module需要的库，common_lib添加，单独需要者单module添加
  > * 2 子工程 builde.gradle 统一 apply from:"../common_component_build.gradle",添加resourcePrefix，避免资源冲突

## 资源文件管理

   -图片与适配
   > * 1 通用图片可放在lib_common
   > * 2 图片适配必须实现hdpi/xhdpi/xxhdpi
   > * 3 工程添加的资源文件必须经过压缩，推荐使用tinyPng (https://tinypng.com/)
   > * 4 屏幕适配 采用autoSize

   -layout
   > * 1 尽量抽取style 减少layout重复代码
   > * 2 减少view的层级，推荐用ConstraintLayoutns布局
   > * 3 对于组合控件，建议抽取出来
   > * 4 对于一些图片和文字，能一个view实现请不要创建多余的view，加强代码阅读感，如富文本，推荐使用fontUtils

   -常量
   > 全局/非全局 全局尽量保持通用

   -打印
   > 后续添加log工具,统一管理

   -命名
   > * 1 统一添加moudle前缀
   > * 2 参考阿里android开发手册















