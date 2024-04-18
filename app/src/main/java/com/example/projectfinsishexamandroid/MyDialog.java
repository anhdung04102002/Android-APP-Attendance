package com.example.projectfinsishexamandroid;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class MyDialog extends DialogFragment {
    public static final String CLASS_ADD_DIALOG = "addClass";
    public static final String STUDENT_ADD_DIALOG = "addStudent";
    private OnclickListener listener;
    public interface OnclickListener {
        void onClick(String text01,String text02);
    }

    public void setListener(OnclickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = null;
        if(getTag().equals(CLASS_ADD_DIALOG))dialog= getAddClassDialog();
        if(getTag().equals(STUDENT_ADD_DIALOG))dialog=getAddStudentDialog();
        return dialog;
    }

    private Dialog getAddStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view); // thiết lập layout

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Thêm mới sinh viên");

        EditText roll_edt = view.findViewById(R.id.edt01);
        EditText name_edt  = view.findViewById(R.id.edt02);

        roll_edt.setHint("Số thứ tự");
        name_edt.setHint("Tên sinh viên");

        Button cancle = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);
        cancle.setOnClickListener(v ->  dismiss());
        add.setOnClickListener(v -> {
            String roll = roll_edt.getText().toString();
            String name = name_edt.getText().toString();
            roll_edt.setText(String.valueOf(Integer.parseInt(roll)+1));
            listener.onClick(roll,name);
        });
        return builder.create();
    }


    private Dialog getAddClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view); // thiết lập layout

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Thêm mới lớp học");

        EditText class_edt = view.findViewById(R.id.edt01);
        EditText subject_edt  = view.findViewById(R.id.edt02);

        class_edt.setHint("Tên lớp");
        subject_edt.setHint("Tên môn");

        Button cancle = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);
        cancle.setOnClickListener(v ->  dismiss());
        add.setOnClickListener(v -> {
            String classname = class_edt.getText().toString();
            String subjectname = subject_edt.getText().toString();
            listener.onClick(classname,subjectname);
            dismiss();;
        });
        return builder.create();
    }
}
