package com.example.projectfinsishexamandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {
    Toolbar toolbar;
    private String className;
    private String subjectName;
    private int position;
    private RecyclerView recyclerView;
    private StudentAdapter studentAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<StudentItem> studentItems = new ArrayList<>();
    private DbHelper dbHelper;
    private long cid;
    private MyCalendar calendar;
    private TextView subtitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        calendar = new MyCalendar();
        dbHelper = new DbHelper(this);
        Intent intent = getIntent();

        className = intent.getStringExtra("className");
        subjectName = intent.getStringExtra("subjectName");
        position = intent.getIntExtra("position", -1);
        cid = intent.getLongExtra("cid", -1);


        setToolbar();
        loadData();


        recyclerView = findViewById(R.id.student_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        studentAdapter = new StudentAdapter(studentItems, this);
        recyclerView.setAdapter(studentAdapter);
        studentAdapter.setOnItemClickListener(position1 -> changeStatus(position1));
        loadStatusData();
    }

    private void loadData() {
        Cursor cursor = dbHelper.getStudentTable(cid);
        studentItems.clear();
        while (cursor.moveToNext()) {
            long sid = cursor.getLong(cursor.getColumnIndex(DbHelper.S_ID));
            int roll = cursor.getInt(cursor.getColumnIndex(DbHelper.STUDENT_ROLL_KEY));
            String name = cursor.getString(cursor.getColumnIndex(DbHelper.STUDENT_NAME_KEY));
            studentItems.add(new StudentItem(sid, roll, name));
        }
        cursor.close();
    }

        private void changeStatus(int position1) {
        String status = studentItems.get(position1).getStatus();
        if (status.equals("x")) status = "Vắng";
        else status = "x";
        studentItems.get(position1).setStatus(status);
        studentAdapter.notifyItemChanged(position1);
    }
//    private void changeStatus(int position1) {
//        String status = studentItems.get(position1).getStatus();
//        if (status.equals("x")) status = "Vắng";
//        else status = "x";
//        studentItems.get(position1).setStatus(status);
//        dbHelper.updateStatus(studentItems.get(position1).getSid(), calendar.getDate(), status);
//        studentAdapter.notifyItemChanged(position1);
//    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);
        save.setOnClickListener(v -> saveStatus()); // lưu trạng thái

        title.setText(className);
        subtitle.setText(subjectName + "|" + calendar.getDate());

        back.setOnClickListener(v -> onBackPressed());
        toolbar.inflateMenu(R.menu.student_menu); //đặt menu trong toolbar
        toolbar.setOnMenuItemClickListener(menuItem -> onMenuItemClick(menuItem));
    }

    private void saveStatus() {
        for (StudentItem studentItem : studentItems) {
            String status = studentItem.getStatus();
            if (!status.equals("x")) status = "Vắng";
            long value = dbHelper.addStatus(studentItem.getSid(), cid,calendar.getDate(), status);
            if (value == -1)
                dbHelper.updateStatus(studentItem.getSid(), calendar.getDate(), status);
        }
    }

    private void loadStatusData() {
        for (StudentItem studentItem : studentItems) {
            String status = dbHelper.getStatus(studentItem.getSid(), calendar.getDate());
            if (status != null) studentItem.setStatus(status);
            else studentItem.setStatus("");
        }
        studentAdapter.notifyDataSetChanged();
    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.add_student) {
            showAddStudentDialog();
        } else if (menuItem.getItemId() == R.id.show_calendar) {
            showCalendar();
        }

        return true;
    }

    // show form chọn ngày
    private void showCalendar() {
        MyCalendar calendar = new MyCalendar();
        calendar.show(getSupportFragmentManager(), ""); // đặt sự tương tác của fragment là chuỗi rỗng (không quan tâm)
        calendar.setOnCalendarOnClickListener(this::onCalendarOkClicked); // gọi đến phương thức(khi nhấn ok sẽ hành động tiếp)
    }

    private void onCalendarOkClicked(int year, int month, int day) {

        calendar.setDate(year, month, day);
        subtitle.setText(subjectName + " | " + calendar.getDate());
        loadStatusData();
    }

    private void showAddStudentDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(), MyDialog.STUDENT_ADD_DIALOG);
        dialog.setListener((roll, name) -> addStudent(roll, name));
    }

    private void addStudent(String roll_string, String name) {
        int roll = Integer.parseInt(roll_string);
        long sid = dbHelper.addStudent(cid, roll, name);
        StudentItem studentItem = new StudentItem(sid, roll, name);
        studentItems.add(studentItem);
        studentAdapter.notifyDataSetChanged(); // cập nhật lại tất cả vị trí cuối cùng trong danh sách
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                showUpdateStudentDialog(item.getGroupId());
                break;
            case 1:
                deleteStudent(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateStudentDialog(int position) {
        MyDialog dialog = new MyDialog(studentItems.get(position).getRoll(), studentItems.get(position).getName());
        dialog.show(getSupportFragmentManager(), MyDialog.STUDENT_UPDATE_DIALOG);
        dialog.setListener((roll_string, name) -> updateStudent(position, name));
    }

    private void updateStudent(int position, String name) {
        dbHelper.updateStudent(studentItems.get(position).getSid(), name);
        studentItems.get(position).setName(name);
        studentAdapter.notifyItemChanged(position);
    }

    private void deleteStudent(int position) {
        dbHelper.deleteStudent(studentItems.get(position).getSid());
        studentItems.remove(position);
        studentAdapter.notifyItemRemoved(position);
    }
}