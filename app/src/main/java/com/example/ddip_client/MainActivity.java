package com.example.ddip_client;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Memo 관련 변수
    private EditText memoInput;
    private ImageButton previousDayButton, nextDayButton, moreOptionsButton;
    private TextView dateTextView;
    private Calendar calendar;

    private static final String PREFS_NAME = "MemoPrefs"; // SharedPreferences 파일명
    private static final String MEMO_PREFIX = "memo_"; // 메모 저장 키의 접두사

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ------------------ Header (상단바) ------------------
        TextView titleTextView = findViewById(R.id.title_text);
        titleTextView.setText("쿠잉");

        // ------------------ Bottom Navigation (하단바) ------------------
        ImageButton homeButton = findViewById(R.id.home_button);
        ImageButton calendarButton = findViewById(R.id.calendar_button);
        ImageButton crewRoomButton = findViewById(R.id.crew_room_button);
        ImageButton myPageButton = findViewById(R.id.mypage_button);

        homeButton.setOnClickListener(v -> Toast.makeText(this, "홈 버튼 클릭됨", Toast.LENGTH_SHORT).show());
        calendarButton.setOnClickListener(v -> Toast.makeText(this, "캘린더 버튼 클릭됨", Toast.LENGTH_SHORT).show());
        crewRoomButton.setOnClickListener(v -> Toast.makeText(this, "크루룸 버튼 클릭됨", Toast.LENGTH_SHORT).show());
        myPageButton.setOnClickListener(v -> Toast.makeText(this, "마이페이지 버튼 클릭됨", Toast.LENGTH_SHORT).show());

        // ------------------ Add Work (근무지 추가) ------------------
        Button addWorkButton = findViewById(R.id.add_work_button);
        addWorkButton.setOnClickListener(v -> Toast.makeText(this, "근무지 추가 클릭됨", Toast.LENGTH_SHORT).show());

        // ------------------ Memo (메모) ------------------
        memoInput = findViewById(R.id.memo_input);
        previousDayButton = findViewById(R.id.previous_day_button);
        nextDayButton = findViewById(R.id.next_day_button);
        moreOptionsButton = findViewById(R.id.more_options_button);
        dateTextView = findViewById(R.id.date_text);

        // Calendar 인스턴스 생성 (현재 날짜로 설정)
        calendar = Calendar.getInstance();

        // 날짜 형식 설정 (예: "2024.10.15")
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());

        // 초기 날짜 표시
        dateTextView.setText(dateFormat.format(calendar.getTime()));

        // 메모 불러오기
        loadMemo(dateFormat);

        // 이전 날 버튼 클릭 리스너
        previousDayButton.setOnClickListener(v -> {
            calendar.add(Calendar.DAY_OF_MONTH, -1); // 하루 이전으로 이동
            updateDate(dateFormat);
        });

        // 다음 날 버튼 클릭 리스너
        nextDayButton.setOnClickListener(v -> {
            calendar.add(Calendar.DAY_OF_MONTH, 1); // 하루 다음으로 이동
            updateDate(dateFormat);
        });

        // 점 세 개 버튼 클릭 리스너 (메모 삭제 옵션)
        moreOptionsButton.setOnClickListener(v -> showOptionsMenu());
    }

    // 날짜를 갱신하여 TextView에 반영하는 메서드
    private void updateDate(SimpleDateFormat dateFormat) {
        String formattedDate = dateFormat.format(calendar.getTime());
        dateTextView.setText(formattedDate);
        loadMemo(dateFormat); // 해당 날짜의 메모를 불러옴
    }

    // SharedPreferences에 메모 저장
    private void saveMemo(String memoText) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String currentDate = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(calendar.getTime());
        editor.putString(MEMO_PREFIX + currentDate, memoText);
        editor.apply();
        Toast.makeText(this, "메모가 저장되었습니다", Toast.LENGTH_SHORT).show();
    }

    // SharedPreferences에서 메모 불러오기
    private void loadMemo(SimpleDateFormat dateFormat) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String currentDate = dateFormat.format(calendar.getTime());
        String memoText = sharedPreferences.getString(MEMO_PREFIX + currentDate, "");
        memoInput.setText(memoText);
    }

    // 메모 삭제 기능
    private void deleteMemo(SimpleDateFormat dateFormat) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String currentDate = dateFormat.format(calendar.getTime());
        editor.remove(MEMO_PREFIX + currentDate);
        editor.apply();
        memoInput.setText(""); // 메모 입력란 초기화
        Toast.makeText(this, "메모가 삭제되었습니다", Toast.LENGTH_SHORT).show();
    }

    // 점 세 개 버튼 클릭 시 메뉴를 표시하는 메서드
    private void showOptionsMenu() {
        // PopupMenu 생성
        PopupMenu popupMenu = new PopupMenu(this, moreOptionsButton);

        // 메뉴 항목을 팽창 (메뉴 파일을 PopupMenu에 로드)
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.memo_menu, popupMenu.getMenu());

        // 메뉴 항목 클릭 리스너 설정 (삭제 기능 추가)
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_delete_memo) {
                deleteMemo(new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()));
                return true;
            }
            return false;
        });

        // 팝업 메뉴를 화면에 표시
        popupMenu.show();
    }
}
