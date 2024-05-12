package com.example.projectfinsishexamandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;

public class SheetActivity extends AppCompatActivity {
    private TextView subtitle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);
        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> onBackPressed());
        setToolbar();
        showTable();

    }

    private void showTable() {
        DbHelper dbHelper = new DbHelper(this);
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        long [] idarrays = getIntent().getLongArrayExtra("idArray");
        int[] rollarrays = getIntent().getIntArrayExtra("rollArray");
        String[] nameArrays = getIntent().getStringArrayExtra("nameArray");
        String month = getIntent().getStringExtra("month");


        int DAY_IN_MONTH = getDayInMonth(month);

        int rowSize = idarrays.length+1; // số hàng trong bảng(hàng đầu tiên là tiêu đề)
        TableRow[] rows = new TableRow[idarrays.length+1];
        TextView[] roll_tv = new TextView[rowSize]; //stt
        TextView[] name_tv = new TextView[rowSize]; // tên
        TextView[][] status_tv = new TextView[rowSize][DAY_IN_MONTH + 1];

        for(int i = 0;i < rowSize;i++) {
            roll_tv[i] = new TextView(this);
            name_tv[i] = new TextView(this);
                    for(int j = 0;j < DAY_IN_MONTH + 1;j++) {
                        status_tv[i][j] = new TextView(this);
                    }
        }
        //header
        roll_tv[0].setText("STT");
        roll_tv[0].setTypeface(roll_tv[0].getTypeface(), Typeface.BOLD);
        name_tv[0].setText("Họ và tên");
        name_tv[0].setTypeface(name_tv[0].getTypeface(), Typeface.BOLD);
        // danh sách các ngày trong tháng
        for(int i = 1;i < DAY_IN_MONTH + 1;i++) {
            status_tv[0][i].setText(String.valueOf(i));
            status_tv[0][i].setTypeface(status_tv[0][i].getTypeface(), Typeface.BOLD);
        }
       // thực hiện lấy giá trị status
        for(int i = 1;i < rowSize;i++) {
            roll_tv[i].setText(String.valueOf(rollarrays[i-1]));
            name_tv[i].setText(nameArrays[i-1]);
            for(int j = 1;j < DAY_IN_MONTH + 1;j++) {
                String day = String.valueOf(j);
                if(day.length() == 1) day = "0" + day;
                String date  = day + "." + month;
                String status = dbHelper.getStatus(idarrays[i-1],date);
                status_tv[i][j].setText(status);
            }
        }

        for(int i = 0;i < rowSize;i++) {
            rows[i] = new TableRow(this);

            if(i%2==0)
                rows[i].setBackgroundColor(0xFFE0E0E0);
            else
                rows[i].setBackgroundColor(0xFFFFFFFF);
            roll_tv[i].setPadding(10,10,10,10);
            name_tv[i].setPadding(10,10,10,10);

            rows[i].addView(roll_tv[i]);
            rows[i].addView(name_tv[i]);
            for(int j = 1;j < DAY_IN_MONTH + 1;j++) {
                status_tv[i][j].setPadding(10,10,10,10);
                rows[i].addView(status_tv[i][j]);
            }
            tableLayout.addView(rows[i]);
        }
        tableLayout.setShowDividers(tableLayout.SHOW_DIVIDER_MIDDLE);
    }

//    private int getDayInMonth(String month) {
//        int monthInt = Integer.parseInt(month.substring(0,1));
//        int year = Integer.parseInt(month.substring(4));
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.MONTH,monthInt);
//        calendar.set(Calendar.YEAR,year);
//        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//    }
private int getDayInMonth(String month) { // lấy số ngày tháng cụ thể
    int monthIndex = Integer.parseInt(month.substring(0, 2)) - 1; // vì chỉ số của tháng bắt đầu từ 0
    int year = Integer.parseInt(month.substring(3));

    Calendar calendar = Calendar.getInstance(); // đại diện cho thời gian hiện tại
    calendar.set(Calendar.MONTH, monthIndex);
    calendar.set(Calendar.YEAR, year);
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
}
    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        subtitle.setVisibility(TextView.GONE);
        title.setText("Bảng điểm danh");
        save.setVisibility(ImageButton.GONE);

        back.setOnClickListener(v -> onBackPressed());

    }
}