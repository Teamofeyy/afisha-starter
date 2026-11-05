package ru.itpolygon.afisha;

import android.os.Handler;
import android.os.Looper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Все вызовы и ответы выполняются в главном потоке.
final class MeetingRepository {
    // Порядок совпадает с массивом response_modes в strings.xml.
    enum Mode { NORMAL, SLOW, ERROR, EMPTY }

    interface Callback {
        void onSuccess(List<Meeting> meetings);
        void onError();
    }

    private static final long SHORT_DELAY_MS = 700L;
    private static final long SLOW_DELAY_MS = 3500L;
    private static final List<Meeting> MEETINGS = Collections.unmodifiableList(Arrays.asList(
            new Meeting(R.string.mobile_title, R.string.mobile_description, R.string.mobile_details),
            new Meeting(R.string.backend_title, R.string.backend_description, R.string.backend_details),
            new Meeting(R.string.review_title, R.string.review_description, R.string.review_details)
    ));

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable pendingResponse;

    void load(Mode mode, Callback callback) {
        // Убираем предыдущий ответ из очереди до постановки нового.
        cancel();
        pendingResponse = () -> {
            pendingResponse = null;
            switch (mode) {
                case ERROR:
                    callback.onError();
                    break;
                case EMPTY:
                    callback.onSuccess(Collections.emptyList());
                    break;
                default:
                    callback.onSuccess(MEETINGS);
            }
        };
        handler.postDelayed(pendingResponse, mode == Mode.SLOW ? SLOW_DELAY_MS : SHORT_DELAY_MS);
    }

    void cancel() {
        if (pendingResponse != null) {
            handler.removeCallbacks(pendingResponse);
            pendingResponse = null;
        }
    }
}
