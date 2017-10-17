package com.example.apple.imageselect;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.imageselect.adapter.ImageSelectAdapter;
import com.example.apple.imageselect.data.ImageData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 17/10/9.
 */

public class ImageSelectActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener{

    private final String TAG = "ImageSelectActivity";

    private Button btnCancel;
    private Button btnOk;
    private TextView tvSelectNumber;
    private GridView gvImageShow;
    private ImageSelectAdapter mImageSelectAdapter;
    private List<ImageData> mAllPath = new ArrayList<>();
    private ArrayList<String> mSelectPath = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);

        initView();
    }

    private void initView() {

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        tvSelectNumber = (TextView) findViewById(R.id.tv_select_number);
        gvImageShow = (GridView) findViewById(R.id.gv_image_show);
        gvImageShow.setOnItemClickListener(new OnItemClickListener());

        mImageSelectAdapter = new ImageSelectAdapter(ImageSelectActivity.this, mAllPath);
        gvImageShow.setAdapter(mImageSelectAdapter);
        getLoaderManager().initLoader(1, null, ImageSelectActivity.this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_cancel:
                finish();
                break;

            case R.id.btn_ok:
                Intent intent = new Intent();
                intent.putStringArrayListExtra("selectPath", mSelectPath);
                ImageSelectActivity.this.setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String columns[] = new String[]{
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Thumbnails.DATA};

        CursorLoader cursorLoader = new CursorLoader(
                this,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns,
                null,
                null,
                null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (mAllPath != null && mAllPath.size() > 0) {
            mAllPath.clear();
        }
        if (cursor.moveToNext()) {
            int thumbPathIndex = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
            int timeIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
            int pathIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

            do {
                ImageData imageData = new ImageData();
                String thumbPath = cursor.getString(thumbPathIndex);
                Long date = cursor.getLong(timeIndex);
                String filepath = cursor.getString(pathIndex);
                imageData.setPath(filepath);
                mAllPath.add(imageData);
            } while (cursor.moveToNext());
        }
        mImageSelectAdapter.setData(mAllPath);
        gvImageShow.setAdapter(mImageSelectAdapter);
        mImageSelectAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    //item click
    class OnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int selectSize = getSelect().size();
            if (selectSize < 9) {
                if (mAllPath.get(position).isSelect()) {
                    mAllPath.get(position).setSelect(false);
                } else {
                    mAllPath.get(position).setSelect(true);
                }
                tvSelectNumber.setText(getSelect().size() + "");
                mImageSelectAdapter.notifyDataSetChanged();
            } else {

                if (mAllPath.get(position).isSelect()){
                    mAllPath.get(position).setSelect(false);
                    tvSelectNumber.setText(getSelect().size() + "");
                    mImageSelectAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getApplication(), "最多只能选择9张", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private List<String> getSelect() {

        if (mSelectPath != null && mSelectPath.size() > 0) {
            mSelectPath.clear();
        }
        if (mAllPath != null && mAllPath.size() > 0) {
            for (int i = 0; i < mAllPath.size(); i++) {
                if (mAllPath.get(i).isSelect()) {
                    mSelectPath.add(mAllPath.get(i).getPath());
                }
            }
        }
        return mSelectPath;
    }

}