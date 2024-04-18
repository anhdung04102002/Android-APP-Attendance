package com.example.projectfinsishexamandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
     FloatingActionButton fab;
     RecyclerView recyclerView;
    ClassAdapter classAdapter;
     RecyclerView.LayoutManager layoutManager;
     ArrayList<ClassItem>  classItems = new ArrayList<>();
     EditText class_edt;
     EditText subject_edt;
     Toolbar toolbar;
    DbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);

        fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(v -> showDialog()); // viết vầy để xử lý điều kiện click trực tiếp


        loadData(); 

        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true); //tối ưu hóa các mục khi kích thước không đổi

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        classAdapter = new ClassAdapter(classItems,this);
        recyclerView.setAdapter(classAdapter);
        classAdapter.setOnItemClickListener(position -> gotoItemActivity(position));
        setToolbar();
    }

    private void loadData() {
        Cursor cursor = dbHelper.getClassTable() ;
        classItems.clear();
        while (cursor.moveToNext()) {
              int id = cursor.getInt(cursor.getColumnIndex(DbHelper.C_ID));
              String className =  cursor.getString(cursor.getColumnIndex(DbHelper.CLASS_NAME_KEY));
             String subjectName =  cursor.getString(cursor.getColumnIndex(DbHelper.SUBJECT_NAME_KEY));

            classItems.add(new ClassItem(id,className,subjectName));


        }
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        title.setText("Phần mềm điểm danh");
        subtitle.setVisibility(View.GONE); // ẩn và giải phóng không gian
        back.setVisibility(View.INVISIBLE); // ẩn các nút
        save.setVisibility(View.INVISIBLE);
    }

    private void gotoItemActivity(int position) {
        Intent intent = new Intent(this,StudentActivity.class); //điều hướng đến activity Student
        intent.putExtra("className",classItems.get(position).getClassName());
        intent.putExtra("subjectName",classItems.get(position).getSubjectName());
        intent.putExtra("position",position);
        startActivity(intent);
    }

    private void showDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.CLASS_ADD_DIALOG);
        dialog.setListener((classname,subjectname)->addClass(classname,subjectname));
    }

    private void addClass(String classname,String subjectname) {
        long  cid = dbHelper.addClass(classname,subjectname);
        ClassItem classItem = new ClassItem(cid,classname,subjectname);
        classItems.add(classItem);
        classAdapter.notifyDataSetChanged();


    }
}