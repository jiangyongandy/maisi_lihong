package org.cocos2dx.lua.model;


import com.tencent.smtt.sdk.WebView;
import com.zuiai.nn.R;

import org.cocos2dx.lua.ui.ChargeActivity;

/**
 * 功能
 * Created by Jiang on 2017/11/30.
 */

public class LiveModel {

    public static final int live_site_type = 2;
    public static final int didiaokan_site_type = 1;
    private static LiveModel instance = null;
    //def -1 charge 0
    private int action = -1;

    private Class intent;
    private boolean needVip;
    private int currentPosition = 0;

    public static LiveModel getInstance() {
        if (instance == null)
            instance = new LiveModel();
        return instance;
    }

    private String[] names = {
            "游戏", "体育", "电视",
            "秀场", "影视", "美女",
            "战旗", "斗鱼", "熊猫",
    };
    private int[] icons = {
            R.drawable.ic_live_icon0, R.drawable.ic_live_icon1, R.drawable.ic_live_icon3,
            R.drawable.ic_live_icon4, R.drawable.ic_live_icon5, R.drawable.ic_live_icon6,
            R.drawable.ic_live_icon7, R.drawable.ic_live_icon8, R.drawable.ic_live_icon9,
    };
    private Class[] activitys = {
            ChargeActivity.class, ChargeActivity.class, ChargeActivity.class,
            ChargeActivity.class, ChargeActivity.class, ChargeActivity.class,
            ChargeActivity.class, ChargeActivity.class, ChargeActivity.class,
    };

    private String[] urls = {
            "http://m.tv.uuu9.com/", "http://m.didiaokan.com/", "http://www.icantv.cn/",
            "http://show.sogou.com/xiuchang/", "http://show.sogou.com/yingshi/", "http://show.sogou.com/meinv/",
            "https://m.zhanqi.tv/games/", "https://m.douyu.com/", "https://m.panda.tv/",
    };

    private int[] menuIndex = {
            0,
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8,
    };

    public boolean isPCUAbyCurPosition() {
        boolean isPC = false;
        switch (currentPosition) {
        }
        return isPC;
    }

    public void handlePingBiAd(WebView webView) {
        switch (currentPosition) {
            case 1:
//                handleDiDiaoKan(webView);
                break;
            case 6:
                handleZhanQiAd(webView);
                break;
            case 7:
                handleDouYuAd(webView);
                break;
            case 8:
                handleXiongMaoAd(webView);
                break;
        }
    }

    private void handleDiDiaoKan(WebView webView) {
        String temp = "javascript:" +
                "var div = document.createElement('div');\n" +
                "\t\t\t\tdiv.setAttribute('style', 'width: 100%!important; height: 30%!important; z-index: 2147483647!important; position: fixed!important; bottom: 0px; left: 0px; background: #000000;');\n" +
                "\t\t\t\tdocument.body.appendChild(div);";
        webView.loadUrl(temp);
    }

    private void handleZhanQiAd(WebView webView) {
        String temp = "javascript:" +
                "var elementName = \"=====\";\n" +
                "function countTotalElement(node) {\n" +
                "    var total = 0;\n" +
                "    if (node.nodeType == 1) {\n" +
                "        total++;\n" +
                "        elementName = elementName + node.tagName + \"\";\n" +
                "        if (node.className == 'footer') {\n" +
                "            console.log(\"找到对应的node------------------\");\n" +
                "            node.style.display=\"none\";"+
                "            return;\n" +
                "        }\n" +
                "    }\n" +
                "    var childrens = node.childNodes;\n" +
                "    for (var i = 0; i < childrens.length; i++) {\n" +
                "        total += countTotalElement(childrens[i]);\n" +
                "    }\n" +
                "    return total;\n" +
                "}\n" +
                "countTotalElement(document);";;
        webView.loadUrl(temp);
    }

    private void handleDouYuAd(WebView webView) {
        String temp = "javascript:" +
                "var elementName = \"=====\";\n" +
                "function countTotalElement(node) {\n" +
                "    var total = 0;\n" +
                "    if (node.nodeType == 1) {\n" +
                "        total++;\n" +
                "        elementName = elementName + node.tagName + \"\";\n" +
                "        if (node.className == 'fix-download') {\n" +
                "            console.log(\"找到对应的node------------------\");\n" +
                "            node.style.display=\"none\";"+
                "            return;\n" +
                "        }\n" +
                "    }\n" +
                "    var childrens = node.childNodes;\n" +
                "    for (var i = 0; i < childrens.length; i++) {\n" +
                "        total += countTotalElement(childrens[i]);\n" +
                "    }\n" +
                "    return total;\n" +
                "}\n" +
                "countTotalElement(document);";;
        webView.loadUrl(temp);
    }

    private void handleXiongMaoAd(WebView webView) {
        String temp = "javascript:" +
                "var node = document.getElementById('tip-download');\n" +
                "node.style.display=\"none\";";
        webView.loadUrl(temp);
    }


    public String getUrlByPosition() {
        return urls[currentPosition];
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public int[] getMenuIndex() {
        return menuIndex;
    }

    public void setMenuIndex(int[] menuIndex) {
        this.menuIndex = menuIndex;
    }

    public int[] getIcons() {
        return icons;
    }

    public void setIcons(int[] icons) {
        this.icons = icons;
    }

    public Class[] getActivitys() {
        return activitys;
    }

    public void setActivitys(Class[] activitys) {
        this.activitys = activitys;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}
