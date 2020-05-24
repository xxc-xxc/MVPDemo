package com.example.xxc.mvpdemo.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.example.xxc.mvpdemo.R;
import com.example.xxc.mvpdemo.base.BaseActivity;
import com.example.xxc.mvpdemo.util.MenuUtil;

import butterknife.BindView;

public class ToolbarActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String[] hintArray = {"iphone", "iphone9", "iphoneX", "iphone11"};
    private SearchView.SearchAutoComplete searchAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_toolbar);
    }

    @Override
    protected void initView() {
        // 设置工具栏左边导航图标
        toolbar.setNavigationIcon(R.mipmap.ic_launcher_round);
        // 设置工具栏标题文本
        toolbar.setTitle("工具栏");
        // 设置工具栏标题文本颜色
        toolbar.setTitleTextColor(Color.RED);
        // 设置工具栏标志图片
        toolbar.setLogo(R.mipmap.ic_launcher_round);
        // 设置工具栏副标题
        toolbar.setSubtitle("副标题");
        // 设置工具栏副标题文字颜色
        toolbar.setSubtitleTextColor(Color.BLUE);
        // 设置工具栏背景
        toolbar.setBackgroundResource(R.color.holo_blue_light);
        // 使用toolbar替换actionbar
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

    }

    @SuppressLint("RestrictedApi")
    private void initSearchView(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        // 从菜单项中获取搜索框对象
        SearchView searchView = (SearchView) menuItem.getActionView();
        // 设置搜索框默认自动缩小为图标
        searchView.setIconifiedByDefault(true);
        // 设置是否显示搜索按钮
        searchView.setSubmitButtonEnabled(true);
        // 获取搜索管理器
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // 创建搜索结果页面的组件名称对象
//        ComponentName componentName = new ComponentName(this, SearchResultActivity.class);
        // 获取搜索控件信息
//        SearchableInfo searchableInfo = searchManager.getSearchableInfo(componentName);
        // 设置搜索框的可搜索信息
//        searchView.setSearchableInfo(searchableInfo);
        // 从搜索框中获取自动完成编辑框
        searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        // 设置自动完成编辑框文本颜色
        searchAutoComplete.setTextColor(Color.WHITE);
        // 设置自动完成编辑框提示文本颜色
        searchAutoComplete.setHintTextColor(Color.WHITE);
        // 给搜索框设置文本变化监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 搜索关键词输入完成
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 搜索关键词发生变化
            @Override
            public boolean onQueryTextChange(String newText) {
                doSearch(newText);
                return false;
            }
        });

        Bundle bundle = new Bundle();
        bundle.putString("data", "这是数据");
        // 额外搜索数据
        searchView.setAppSearchData(bundle);

    }

    /**
     * 自动匹配相关的关键词列表
     * @param newText
     */
    private void doSearch(String newText) {
        if (newText.indexOf("i") == 0) {
//            ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, R.layout.search_list_auto, hintArray);
//            searchAutoComplete.setAdapter(stringArrayAdapter);
            searchAutoComplete.setOnClickListener(view -> {
                searchAutoComplete.setText(((TextView)view).getText());
            });
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_toolbar;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        MenuUtil.setOverflowIconVisible(featureId, menu);
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overflow, menu);
        initSearchView(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            case R.id.menu_quit:
                finish();
                break;
            case R.id.menu_refresh:
                Toast.makeText(this, "刷新menu", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_about:
                Toast.makeText(this, "关于menu", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
