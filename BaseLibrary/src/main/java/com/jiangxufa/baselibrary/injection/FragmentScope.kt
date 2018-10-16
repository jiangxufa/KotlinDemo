package com.jiangxufa.baselibrary.injection

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.RUNTIME
import javax.inject.Scope

/**
 * @author zzq  作者 E-mail:   soleilyoyiyi@gmail.com
 * @date 创建时间：2017/4/28 11:50
 * 描述:Fragment 生命周期
 */

@Scope
@Retention(RUNTIME)
annotation class FragmentScope
