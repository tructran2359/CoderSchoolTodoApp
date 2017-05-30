package com.coderschool.todo.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by TrucTran on 5/31/17.
 */

public class Utils {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}
