package com.coderschool.todo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.coderschool.todo.Utils.Utils;

public class EditItemActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "EXTRA_POSITION";
    public static final String EXTRA_ITEM = "EXTRA_ITEM";
    private int mPosition;
    private String mItem;

    private EditText mEtInput;
    private Button mBtnSave;

    public static Intent getLaunchIntent(Context context, int position, String item) {
        Intent intent = new Intent(context, EditItemActivity.class);
        intent.putExtra(EXTRA_POSITION, position);
        intent.putExtra(EXTRA_ITEM, item);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Intent intent = getIntent();
        mPosition = intent.getIntExtra(EXTRA_POSITION, -1);
        mItem = intent.getStringExtra(EXTRA_ITEM);

        if (mPosition == -1) {
            Utils.showToast(this, "Something went wrong. Please try again");
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        initView();

        mEtInput.setText(mItem);

        initListener();
    }

    private void initView() {
        mEtInput = (EditText) findViewById(R.id.act_edit_et_input);
        mBtnSave = (Button) findViewById(R.id.act_edit_btn_save);
    }

    private void initListener() {
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newItem = mEtInput.getText().toString();
                if (TextUtils.isEmpty(newItem)) {
                    Utils.showToast(EditItemActivity.this, "Empty text is not allowed");
                    mEtInput.setText(mItem);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_POSITION, mPosition);
                    intent.putExtra(EXTRA_ITEM, newItem);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
