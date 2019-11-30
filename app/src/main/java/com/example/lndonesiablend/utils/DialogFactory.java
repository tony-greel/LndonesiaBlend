package com.example.lndonesiablend.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.example.lndonesiablend.R;
import com.example.lndonesiablend.adapter.RecyclerViewHolderAdapter;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.lang.reflect.Field;


/**
 * Created by SiKang on 2018/2/6.
 * 快速创建常用Dialog
 */
public class DialogFactory {

    /**
     * 创建一个半透明背景带有Icon的Dialog （Loading、Error等)
     */
    public static Dialog createTipDialog(Context context, @QMUITipDialog.Builder.IconType int mode, String tipWord) {
        return new QMUITipDialog.Builder(context)
                .setIconType(mode)
                .setTipWord(tipWord)
                .create();
    }

    /**
     * 创建一个带有文本提示的对话框
     */
    public static void showMessageDialog(Context context, String msg) {
        createMessageDialog(context, context.getString(R.string.text_tips), msg, context.getString(R.string.text_sure), null).show();
    }

    public static Dialog createMessageDialog(Context context, String title, String message, String btnText) {
        return createMessageDialog(context, title, message, btnText, null);
    }

    public static Dialog createMessageDialog(Context context, String title, String message, String btnText, QMUIDialogAction.ActionListener listener) {
        if (listener == null) {
            listener = new QMUIDialogAction.ActionListener() {
                @Override
                public void onClick(QMUIDialog dialog, int index) {
                    dialog.dismiss();
                }
            };
        }
        Dialog dialog = new QMUIDialog.MessageDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .addAction(btnText, listener)
                .create();
        try {
            Class<?> mAlert = dialog.getClass();
            Field field = mAlert.getDeclaredField("mAlert");
            field.setAccessible(true);
            Field mTitleView = field.get(dialog).getClass().getDeclaredField("mTitleView");
            mTitleView.setAccessible(true);
            Object AlertController = field.get(dialog);
            mTitleView.set(AlertController, new TextView(context));//该方法<span style="font-family:Microsoft YaHei;">没起作用，不知道为什么，有大神清楚么？</span>
            dialog.show();
            Object obj = mTitleView.get(AlertController);
            TextView textView = (TextView) obj;
            textView.setSingleLine(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    public static Dialog createYesOrNoDialog(Context context, String title, String msg, String yesBtnText, String noBtnText, final OnYesOrNoListener onYesOrNoListener) {
        final Dialog dialog = new QMUIDialog.MessageDialogBuilder(context)
                .setTitle(title)
                .setMessage(msg)
                .addAction(0, noBtnText, QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        onYesOrNoListener.onClick(dialog, false);
                    }
                })
                .addAction(yesBtnText, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        onYesOrNoListener.onClick(dialog, true);
                    }
                })
                .create();
        try {
            Class<?> mAlert = dialog.getClass();
            Field field = mAlert.getDeclaredField("mAlert");
            field.setAccessible(true);
            Field mTitleView = field.get(dialog).getClass().getDeclaredField("mTitleView");
            mTitleView.setAccessible(true);
            Object AlertController = field.get(dialog);
            mTitleView.set(AlertController, new TextView(context));//该方法<span style="font-family:Microsoft YaHei;">没起作用，不知道为什么，有大神清楚么？</span>
            dialog.show();
            Object obj = mTitleView.get(AlertController);
            TextView textView = (TextView) obj;
            textView.setSingleLine(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }


    /**
     * 创建一个带输入框的对话框
     */
    public static Dialog createInputDialog(Context context, String title, String placeholder, final String btnText, final OnInputActionListener listener) {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(context);
        return builder.setTitle(title)
                .setTitle(title)
                .setPlaceholder(placeholder)
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("cancel", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(btnText, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        listener.onClick(dialog, builder.getEditText());
                    }
                })
                .create();
    }

    /**
     * 创建底部选择框
     *
     * @param context
     * @param items
     * @param listener
     * @return
     */
    public static void createBottomSheet(Context context, String[] items, String title, OnItemChoicectionListener listener) {
        TextView view = new TextView(context);
        view.setText(title);
        view.setGravity(Gravity.CENTER);
        view.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        QMUIBottomSheet.BottomListSheetBuilder bottomListSheetBuilder = new QMUIBottomSheet.BottomListSheetBuilder(context)
                .addHeaderView(view)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        if (listener != null) {
                            listener.onClick(dialog, position);
                        }
                        dialog.dismiss();
                    }
                });
        for (String item : items) {
            bottomListSheetBuilder.addItem(item);
        }
        bottomListSheetBuilder.build().show();
    }

    /**
     * 创建菜单类型对话框
     */
    public static Dialog createMenuDialog(Context context, String[] items, DialogInterface.OnClickListener listener) {
        Dialog dialog = new QMUIDialog.MenuDialogBuilder(context)
                .addItems(items, listener)
                .create();
        try {
           /* Class<?> mAlert = dialog.getClass();
            Field field = mAlert.getDeclaredField("mAlert");
            field.setAccessible(true);
            Field mTitleView = field.get(dialog).getClass().getDeclaredField("mTitleView");
            mTitleView.setAccessible(true);
            Object AlertController = field.get(dialog);
            mTitleView.set(AlertController, new TextView(context));//该方法<span style="font-family:Microsoft YaHei;">没起作用，不知道为什么，有大神清楚么？</span>
            dialog.show();
            Object obj = mTitleView.get(AlertController);
            TextView textView = (TextView) obj;
            textView.setSingleLine(false);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }


    /**
     * 创建一个单选菜单
     */
    public static Dialog createSingleChoiceDialog(Context context, String[] items, int checkedIndex, DialogInterface.OnClickListener listene) {
        return new QMUIDialog.CheckableDialogBuilder(context)
                .setCheckedIndex(checkedIndex)
                .addItems(items, listene)
                .create();
    }


    /**
     * 创建一个多选对话框
     */
    public static Dialog createMultiChoiceDialog(Context context, String btnText, String[] items, int[] checkedItems, final OnMultiChoicectionListener listene) {
        final QMUIDialog.MultiCheckableDialogBuilder builder = new QMUIDialog.MultiCheckableDialogBuilder(context)
                .setCheckedItems(checkedItems)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.addAction("cancel", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
            }
        }).addAction(btnText, new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                listene.onClick(dialog, builder.getCheckedItemIndexes());
            }
        });
        return builder.create();
    }


    /**
     * 创建一个自定义的View
     */
    public static Dialog createCustomDialog(Context context, @LayoutRes int layout, OnViewCreatedListener listener) {
        View view = LayoutInflater.from(context).inflate(layout, null);
        Dialog dialog = new CustomDialog(context, view);
        listener.onCreatedView(dialog, new RecyclerViewHolderAdapter(view));
        return dialog;
    }


    public interface OnItemChoicectionListener {
        void onClick(QMUIBottomSheet dialog, int position);
    }

    public interface OnInputActionListener {
        void onClick(QMUIDialog dialog, EditText editText);
    }

    public interface OnMultiChoicectionListener {
        void onClick(QMUIDialog dialog, int[] checkedItemIndexes);
    }

    public interface OnYesOrNoListener {
        void onClick(Dialog dialog, boolean isAgree);
    }

    public interface OnViewCreatedListener {
        void onCreatedView(Dialog dialog, RecyclerViewHolderAdapter viewHolder);
    }

    public static void dismiss(Dialog dialog) {
        if (dialog != null) {
            Context context = ((ContextWrapper) dialog.getContext()).getBaseContext();
            if (context instanceof Activity) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed())
                        dialog.dismiss();
                } else
                    dialog.dismiss();
            } else
                dialog.dismiss();
        }
    }

    public static class CustomDialog extends Dialog {
        View view;

        public CustomDialog(@NonNull Context context, View view) {
            super(context);
            this.view = view;
        }

        @Override
        public void show() {
            super.show();
            setContentView(view);
        }

    }

    public static AlertDialog.Builder alertDialogBuilder(Context context) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        return builder;
    }

    public static void showAlertAndSetStyle(AlertDialog.Builder builder) {
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3478F6"));
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextSize(14);
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#EA4E3D"));
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextSize(14);
    }
}
