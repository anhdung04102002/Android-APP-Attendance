package com.example.projectfinsishexamandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SheetListActivity extends AppCompatActivity {
    private ListView sheetList;
    private ArrayAdapter adapter;
    private ArrayList<String> listItems = new ArrayList();
    long cid;
    Toolbar toolbar;
    private TextView subtitle;
    private String className;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_list);
        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> onBackPressed());


        cid = getIntent().getLongExtra("cid", -1);

        setToolbar();
        loadListItems();
        sheetList = findViewById(R.id.list_sheet);
        adapter = new ArrayAdapter(this, R.layout.sheet_list,R.id.date_list_item,listItems);
        sheetList.setAdapter(adapter);

        sheetList.setOnItemClickListener((parent, view, position, id) -> openSheetActtivity(position));

    }

    private void openSheetActtivity(int position) {
        long [] idarrays = getIntent().getLongArrayExtra("idArray");
        int[] rollarrays = getIntent().getIntArrayExtra("rollArray");
        String[] nameArrays = getIntent().getStringArrayExtra("nameArray");

        Intent intent = new Intent(this,SheetActivity.class);
        intent.putExtra("idArray", idarrays);
        intent.putExtra("nameArray", nameArrays);
        intent.putExtra("rollArray", rollarrays);
        intent.putExtra("month", listItems.get(position));
        startActivity(intent);
    }

    private void loadListItems() {
        Cursor cursor = new DbHelper(this).getDistincMonths(cid);
        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndex(DbHelper.DATE_KEY)); // 05.05/2024
            listItems.add(date.substring(3));
        }
    }
    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        subtitle.setVisibility(TextView.GONE);
        title.setText("Danh sách các bảng điểm danh trong tháng");
       save.setVisibility(ImageButton.GONE);

        back.setOnClickListener(v -> onBackPressed());

    }
}