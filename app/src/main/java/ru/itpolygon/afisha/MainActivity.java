package ru.itpolygon.afisha;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public final class MainActivity extends Activity {
    private final MeetingRepository repository = new MeetingRepository();
    private Spinner modeSpinner;
    private Button loadButton;
    private Button retryButton;
    private View loadingPanel;
    private View errorPanel;
    private View emptyPanel;
    private LinearLayout meetingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applySystemBarInsets();

        modeSpinner = findViewById(R.id.mode_spinner);
        loadButton = findViewById(R.id.load_button);
        retryButton = findViewById(R.id.retry_button);
        loadingPanel = findViewById(R.id.loading_panel);
        errorPanel = findViewById(R.id.error_panel);
        emptyPanel = findViewById(R.id.empty_panel);
        meetingList = findViewById(R.id.meeting_list);

        ArrayAdapter<CharSequence> modes = ArrayAdapter.createFromResource(
                this, R.array.response_modes, R.layout.item_mode);
        modes.setDropDownViewResource(R.layout.item_mode);
        modeSpinner.setAdapter(modes);
        loadButton.setOnClickListener(view -> loadMeetings());
    }

    private void loadMeetings() {
        MeetingRepository.Mode mode =
                MeetingRepository.Mode.values()[modeSpinner.getSelectedItemPosition()];
        clearResult();

        repository.load(mode, new MeetingRepository.Callback() {
            @Override
            public void onSuccess(List<Meeting> meetings) {
                if (!meetings.isEmpty()) {
                    showMeetings(meetings);
                }
            }

            @Override
            public void onError() {
                // Практикум: показать подготовленную панель ошибки.
            }
        });
    }

    private void clearResult() {
        loadingPanel.setVisibility(View.GONE);
        errorPanel.setVisibility(View.GONE);
        emptyPanel.setVisibility(View.GONE);
        meetingList.setVisibility(View.GONE);
        meetingList.removeAllViews();
    }

    private void showMeetings(List<Meeting> meetings) {
        for (Meeting meeting : meetings) {
            View card = getLayoutInflater().inflate(R.layout.item_meeting, meetingList, false);
            TextView title = card.findViewById(R.id.meeting_title);
            TextView description = card.findViewById(R.id.meeting_description);
            TextView details = card.findViewById(R.id.meeting_details);
            title.setText(meeting.title);
            description.setText(meeting.description);
            details.setText(meeting.details);
            meetingList.addView(card);
        }
        meetingList.setVisibility(View.VISIBLE);
    }

    private void applySystemBarInsets() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
            View screen = findViewById(R.id.screen);
            screen.setOnApplyWindowInsetsListener((view, insets) -> {
                var safeArea = insets.getInsets(
                        WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
                view.setPadding(safeArea.left, safeArea.top, safeArea.right, safeArea.bottom);
                return insets;
            });
        }
    }

    @Override
    protected void onDestroy() {
        repository.cancel();
        super.onDestroy();
    }
}
