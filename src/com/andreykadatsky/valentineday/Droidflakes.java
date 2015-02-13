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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class Droidflakes extends Activity {

    FlakeView flakeView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.droidflakes);
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        flakeView = new FlakeView(this);
        container.addView(flakeView);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        final TextView textViewName = (TextView) findViewById(R.id.textViewName);
        final View imageViewTail =  findViewById(R.id.imageViewTail);
        final View parentDialog =  findViewById(R.id.parentDialog);
        final View droid =  findViewById(R.id.droid);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                        final String wish = WebUtil.getInstance(Droidflakes.this).getWish().getResult();
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

        }.execute();

    }


    @Override
    protected void onPause() {
        super.onPause();
        flakeView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        flakeView.resume();
    }


}
