package com.jiangxufa.kotlindemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * 创建时间：2018/9/12
 * 编写人：lenovo
 * 功能描述：
 */
public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "hhhhhh";
    private Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);
        Log.d(TAG, "onCreate: ");
        fragment = BaseFragment.newInstance(1);
    }

    public void add(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.attach(fragment).commit();

//        transaction.add(R.id.container, BaseFragment.newInstance(1), "one")
//                .addToBackStack("one")
//                .commit();
        Log.d(TAG, "add: " + getSupportFragmentManager().getBackStackEntryCount()
                + "  PrimaryNavigationFragment :" + getSupportFragmentManager().getPrimaryNavigationFragment());
    }

    public void remove(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.detach(fragment).commit();

//        transaction.remove(getSupportFragmentManager().findFragmentByTag("one")).commit();
//        transaction.add(R.id.container, BaseFragment.newInstance(2), "two")
//                .addToBackStack("two")
//                .commit();
    }

    public void replace(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.container, fragment, "one")
                .commit();

//
//        transaction.replace(R.id.container, BaseFragment.newInstance(9)).commit();
    }

    public void hint(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(getSupportFragmentManager().findFragmentByTag("one")).commit();
    }

    public void show(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.show(getSupportFragmentManager().findFragmentByTag("one")).commit();
    }

    public void backStackReplace(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.attach(getSupportFragmentManager().findFragmentByTag("one")).commit();
//        transaction.replace(R.id.container, getSupportFragmentManager().findFragmentByTag("one")).commit();
    }

    public void backStackRemove(View view) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.container, getSupportFragmentManager().findFragmentByTag("one"), "one")
//                .commit();

        Fragment one = getSupportFragmentManager().findFragmentByTag("one");
        Log.d(TAG, "backStackRemove: " + one.toString());
    }

    public void log(View view) {
        logF();
    }


    public void logF() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        for (int i = 0; i < fragments.size(); i++) {
            sb.append(fragments.get(i).toString()).append("\n");
        }

        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            sb1.append(getSupportFragmentManager().getBackStackEntryAt(i).toString()).append("\n");
        }
        Log.d(TAG, "页面中的Fragment个数: " + fragments.size() + "  " + sb.toString() + "\n" + "" +
                "回退栈中的Fragment个数: " + getSupportFragmentManager().getBackStackEntryCount() + sb1.toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

}
