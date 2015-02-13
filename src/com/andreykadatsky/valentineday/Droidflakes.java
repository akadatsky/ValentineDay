/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.andreykadatsky.valentineday;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Droidflakes extends Activity {

    FlakeView flakeView;
    private AsyncTask<Void, Void, String> task;
    private String name = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.droidflakes);

        Intent intent = getIntent();
        // check if this intent is started via custom scheme link
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            List<String> params = uri.getPathSegments();
            if(params != null && !params.isEmpty()){
                String method = params.get(0); // androidhappyvalentinesday
                if(method.equals("androidhappyvalentinesday")){
                    if(params.size()>1){
                        name = params.get(1); // name to congrat

                    }
                }
            }
        }

        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        flakeView = new FlakeView(this);
        container.addView(flakeView);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
    }


    @Override
    protected void onResume() {
        super.onResume();
        flakeView.resume();

        final TextView textViewName = (TextView) findViewById(R.id.textViewName);
        final View imageViewTail = findViewById(R.id.imageViewTail);
        final View parentDialog = findViewById(R.id.parentDialog);
        final View droid = findViewById(R.id.droid);


        task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                while (!isCancelled()) {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                        final String wish = WebUtil.getInstance(Droidflakes.this).getWish(name).getResult();
                        if (!TextUtils.isEmpty(wish)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    parentDialog.setVisibility(View.VISIBLE);
                                    imageViewTail.setVisibility(View.VISIBLE);
                                    droid.setVisibility(View.VISIBLE);
                                    textViewName.setText(wish);
                                }
                            });
                        }
                    } catch (Exception e) {
                        break;
                    }
                }
                return null;
            }

        };
        task.execute();

    }

    @Override
    protected void onPause() {
        super.onPause();
        flakeView.pause();
        if (task != null) {
            task.cancel(true);
        }
    }


}
