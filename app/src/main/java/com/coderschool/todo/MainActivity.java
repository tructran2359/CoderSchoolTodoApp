package com.coderschool.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.coderschool.todo.Utils.Utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int RC_EDIT = 11;

    private EditText mEtInput;
    private Button mBtnAdd;
    private ListView mListView;

    private List<String> mData;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        initListView();

        setUpListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        readItems();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_EDIT && resultCode == RESULT_OK) {
            int position = data.getIntExtra(EditItemActivity.EXTRA_POSITION, -1);
            String newItem = data.getStringExtra(EditItemActivity.EXTRA_ITEM);

            if (position == -1) {
                Utils.showToast(this, "Something went wrong");
            } else {
                mData.set(position, newItem);
                writeItems();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initViews() {
        mEtInput = (EditText) findViewById(R.id.act_main_et_input);
        mBtnAdd = (Button) findViewById(R.id.act_main_btn_add);
        mListView = (ListView) findViewById(R.id.act_main_lv_content);
    }

    private void initListView() {
        mData = new ArrayList<>();
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mData);
        mListView.setAdapter(mAdapter);
    }

    private void setUpListener() {
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItem = mEtInput.getText().toString();
                if (TextUtils.isEmpty(newItem)) {
                    Utils.showToast(MainActivity.this, "Please enter an item first");
                } else {
                    mData.add(newItem);
                    mAdapter.notifyDataSetChanged();
                    mEtInput.setText("");
                    writeItems();
                }
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mData.remove(position);
                mAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToEditScreen(position);
            }
        });
    }

    private void readItems() {
        File fileDir = getFilesDir();
        File file = new File(fileDir, "todo.txt");

        mData.clear();
        try {
            mData.addAll(FileUtils.readLines(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeItems() {
        File fileDir = getFilesDir();
        File file = new File(fileDir, "todo.txt");

        try {
            FileUtils.writeLines(file, mData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goToEditScreen(int position) {
        Intent intent = EditItemActivity.getLaunchIntent(this, position, mData.get(position));
        startActivityForResult(intent, RC_EDIT);
    }
}
