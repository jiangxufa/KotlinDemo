package com.jiangxufa.kotlindemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 创建时间：2018/9/12
 * 编写人：lenovo
 * 功能描述：
 */
public class BaseFragment extends Fragment {

    private int i;

    private static final String TAG = "BaseFragment";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "第"+i+"个Fragment onAttach: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        i = getArguments().getInt("aaaa");
        Log.d(TAG, "第"+i+"个Fragment onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getContext());
        textView.setText("第"+i+"个Fragment");
        textView.setTextSize(24);
        Log.d(TAG, "第"+i+"个Fragment onCreateView: ");
        return textView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "第"+i+"个Fragment onViewCreated: ");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "第"+i+"个Fragment onActivityCreated: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "第"+i+"个Fragment onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "第"+i+"个Fragment onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "第"+i+"个Fragment onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "第"+i+"个Fragment onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "第"+i+"个Fragment onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "第"+i+"个Fragment onDestroy: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "第"+i+"个Fragment onDetach: ");
    }

    public static Fragment newInstance(int i) {
        Bundle bundle = new Bundle();
        BaseFragment fragment = new BaseFragment();
        bundle.putInt("aaaa",i);
        fragment.setArguments(bundle);
        return fragment;
    }

}
