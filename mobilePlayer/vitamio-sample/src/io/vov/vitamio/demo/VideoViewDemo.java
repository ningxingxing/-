/*
 * Copyright (C) 2013 yixia.com
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

package io.vov.vitamio.demo;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VideoViewDemo extends Activity {

	/**
	 * TODO: Set the path variable to a streaming video URL or a local media file
	 * path.
	 */
	private String path = "http://data.vod.itc.cn/?rb=1&prot=1&key=jbZhEJhlqlUN-Wj_HEI8BjaVqKNFvDrn&prod=flash&pt=1&new=/86/168/RUXzy059JIvGRrrXQvOkNE.mp4";
	private VideoView mVideoView;
	private EditText mEditText;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		if (!LibsChecker.checkVitamioLibs(this))
			return;
		setContentView(R.layout.videoview);


		mEditText = (EditText) findViewById(R.id.url);
		mVideoView = (VideoView) findViewById(R.id.surface_view);

		//String path = "http://data.vod.itc.cn/?rb=1&prot=1&key=jbZhEJhlqlUN-Wj_HEI8BjaVqKNFvDrn&prod=flash&pt=1&new=/86/168/RUXzy059JIvGRrrXQvOkNE.mp4";
		if (path == "") {
			// Tell the user to provide a media file URL/path.
			Toast.makeText(VideoViewDemo.this, "Please edit VideoViewDemo Activity, and set path" + " variable to your media file URL/path", Toast.LENGTH_LONG).show();
			return;
		} else {
			/*
			 * Alternatively,for streaming media you can use
			 * mVideoView.setVideoURI(Uri.parse(URLstring));
			 */
			//mVideoView.setVideoPath(path);
			mVideoView.setVideoURI(Uri.parse(path));
			mVideoView.setMediaController(new MediaController(this));
			mVideoView.requestFocus();

			mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mediaPlayer) {
					// optional need Vitamio 4.0
					mediaPlayer.setPlaybackSpeed(1.0f);
				}
			});
		}

	}
	
	public void startPlay(View view) {
	   // String url = mEditText.getText().toString();
	    //path = url;
		String url = "http://data.vod.itc.cn/?rb=1&prot=1&key=jbZhEJhlqlUN-Wj_HEI8BjaVqKNFvDrn&prod=flash&pt=1&new=/86/168/RUXzy059JIvGRrrXQvOkNE.mp4";
	    if (!TextUtils.isEmpty(url)) {
			mVideoView.setVideoURI(Uri.parse(path));
	       // mVideoView.setVideoPath(url);
	    }
    }
	
	public void openVideo(View View) {
		mVideoView.setVideoURI(Uri.parse(path));
	  //mVideoView.setVideoPath(path);
	}
	
}
