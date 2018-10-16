package com.jiangxufa.homemodule.widget;


import com.jiangxufa.baselibrary.widgets.section.StatelessSection;
import com.jiangxufa.baselibrary.widgets.section.ViewHolder;
import com.jiangxufa.homemodule.R;

/**
 * @author zzq  作者 E-mail:   soleilyoyiyi@gmail.com
 * @date 创建时间：2017/6/8 18:00
 * 描述:增加头部或者尾部
 */

public class HeadOrFooterSection extends StatelessSection {

    public ViewHolder holder;

    public HeadOrFooterSection(int headerResourceId) {
        super(headerResourceId, R.layout.layout_empty);
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolder holder) {
        this.holder = holder;
    }
}
