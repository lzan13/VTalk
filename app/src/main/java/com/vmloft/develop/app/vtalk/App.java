package com.vmloft.develop.app.vtalk;

import com.vmloft.develop.library.tools.VMApp;
import com.vmloft.develop.library.tools.VMTools;

/**
 * Create by lzan13 on 18/8/15 上午11:54
 */
public class App extends VMApp {

    @Override
    public void onCreate() {
        super.onCreate();

        VMTools.init(context);
    }
}
