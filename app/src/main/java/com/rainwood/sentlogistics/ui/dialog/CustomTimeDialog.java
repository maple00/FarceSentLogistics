package com.rainwood.sentlogistics.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.sentlogistics.R;
import com.rainwood.sentlogistics.helper.PickerLayoutManager;
import com.rainwood.tools.utils.DateTimeUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.MyAdapter;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author : a797s
 * @time : 2019/08/17
 * @desc : 时间选择对话框
 */
public final class CustomTimeDialog {

    public static final class Builder
            extends UIDialog.Builder<Builder> implements Runnable {

        private final RecyclerView mMonthDayView;
        private final RecyclerView mHourView;
        private final RecyclerView mMinuteView;
        private final RecyclerView mSecondView;

        private final PickerLayoutManager mMonthDayManager;
        private final PickerLayoutManager mHourManager;
        private final PickerLayoutManager mMinuteManager;
        private final PickerLayoutManager mSecondManager;

        private final PickerAdapter mMonthDayAdapter;
        private final PickerAdapter mHourAdapter;
        private final PickerAdapter mMinuteAdapter;
        private final PickerAdapter mSecondAdapter;

        private OnListener mListener;

        @SuppressWarnings("all")
        public Builder(Context context) {
            super(context);
            setCustomView(R.layout.dialog_time);
            setTitle(R.string.time_title);

            mMonthDayView = findViewById(R.id.rv_month_day);
            mHourView = findViewById(R.id.rv_time_hour);
            mMinuteView = findViewById(R.id.rv_time_minute);
            mSecondView = findViewById(R.id.rv_time_second);

            mMonthDayAdapter = new PickerAdapter(context);
            mHourAdapter = new PickerAdapter(context);
            mMinuteAdapter = new PickerAdapter(context);
            mSecondAdapter = new PickerAdapter(context);

            // 本地时区
            Calendar calendar = Calendar.getInstance(Locale.CHINA);

            // 生产 MM-dd 07-10
            ArrayList<String> monthDayData = new ArrayList<>(30);
            for (int i = 0; i <= 29; i++) {
                // 距离现在之后的多少天
                String dayData;
                if (i == 0) {
                    dayData = "今天";
                } else if (i == 1) {
                    dayData = "明天";
                } else {
                    dayData = DateTimeUtils.getLongTime(-24 * i, DateTimeUtils.DatePattern.ONLY_MONTH_DAY);
                }
                monthDayData.add(dayData);
            }

            // 生产小时
            ArrayList<String> hourData = new ArrayList<>(24);
            for (int i = 0; i <= 23; i++) {
                hourData.add((i < 10 ? "0" : "") + i + " " + getString(R.string.common_hour));
            }

            // 生产分钟
            ArrayList<String> minuteData = new ArrayList<>(60);
            for (int i = 0; i <= 59; i++) {
                minuteData.add((i < 10 ? "0" : "") + i + " " + getString(R.string.common_minute));
            }

            // 生产秒钟
            ArrayList<String> secondData = new ArrayList<>(60);
            for (int i = 0; i <= 59; i++) {
                secondData.add((i < 10 ? "0" : "") + i + " " + getString(R.string.common_second));
            }

            mMonthDayAdapter.setData(monthDayData);
            mHourAdapter.setData(hourData);
            mMinuteAdapter.setData(minuteData);
            mSecondAdapter.setData(secondData);

            mMonthDayManager = new PickerLayoutManager.Builder(context)
                    .build();
            mHourManager = new PickerLayoutManager.Builder(context)
                    .build();
            mMinuteManager = new PickerLayoutManager.Builder(context)
                    .build();
            mSecondManager = new PickerLayoutManager.Builder(context)
                    .build();

            mMonthDayView.setLayoutManager(mMonthDayManager);
            mHourView.setLayoutManager(mHourManager);
            mMinuteView.setLayoutManager(mMinuteManager);
            mSecondView.setLayoutManager(mSecondManager);

            mMonthDayView.setAdapter(mMonthDayAdapter);
            mHourView.setAdapter(mHourAdapter);
            mMinuteView.setAdapter(mMinuteAdapter);
            mSecondView.setAdapter(mSecondAdapter);

            // 设置当前的时分秒
            setMonthDay(DateTimeUtils.getNowDate(DateTimeUtils.DatePattern.ONLY_MONTH_DAY));
            setHour(calendar.get(Calendar.HOUR_OF_DAY));
            setMinute(calendar.get(Calendar.MINUTE));
            setSecond(calendar.get(Calendar.SECOND));

            postDelayed(this, 1000);
        }

        @Override
        public void run() {
            if (mHourView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE &&
                    mMinuteView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE &&
                    mSecondView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, mHourManager.getPickedPosition());
                calendar.set(Calendar.MINUTE, mMinuteManager.getPickedPosition());
                calendar.set(Calendar.SECOND, mSecondManager.getPickedPosition());
                if (System.currentTimeMillis() - calendar.getTimeInMillis() < 3000) {
                    calendar = Calendar.getInstance();
                    setHour(calendar.get(Calendar.HOUR_OF_DAY));
                    setMinute(calendar.get(Calendar.MINUTE));
                    setSecond(calendar.get(Calendar.SECOND));
                    postDelayed(this, 1000);
                }
            }
        }

        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        /**
         * 不选择秒数
         */
        public Builder setIgnoreSecond() {
            mSecondView.setVisibility(View.GONE);
            return this;
        }

        public Builder setTime(String time) {
            // 102030
            if (time.matches("\\d{6}")) {
                setHour(time.substring(0, 2));
                setMinute(time.substring(2, 4));
                setSecond(time.substring(4, 6));
                // 10:20:30
            } else if (time.matches("\\d{2}:\\d{2}:\\d{2}")) {
                setHour(time.substring(0, 2));
                setMinute(time.substring(3, 5));
                setSecond(time.substring(6, 8));
            }
            return this;
        }

        public Builder setMonthDay(String nowDate) {
            int index = 0;
            if (index > mMonthDayAdapter.getItemCount() - 1) {
                index = mMonthDayAdapter.getItemCount() - 1;
            }
            mMonthDayView.scrollToPosition(index);
            return this;
        }

        public Builder setHour(String hour) {
            return setHour(Integer.valueOf(hour));
        }

        public Builder setHour(int hour) {
            int index = hour;
            if (index < 0 || hour == 24) {
                index = 0;
            } else if (index > mHourAdapter.getItemCount() - 1) {
                index = mHourAdapter.getItemCount() - 1;
            }
            mHourView.scrollToPosition(index);
            return this;
        }

        public Builder setMinute(String minute) {
            return setMinute(Integer.valueOf(minute));
        }

        public Builder setMinute(int minute) {
            int index = minute;
            if (index < 0) {
                index = 0;
            } else if (index > mMinuteAdapter.getItemCount() - 1) {
                index = mMinuteAdapter.getItemCount() - 1;
            }
            mMinuteView.scrollToPosition(index);
            return this;
        }

        public Builder setSecond(String second) {
            return setSecond(Integer.valueOf(second));
        }

        public Builder setSecond(int second) {
            int index = second;
            if (index < 0) {
                index = 0;
            } else if (index > mSecondAdapter.getItemCount() - 1) {
                index = mSecondAdapter.getItemCount() - 1;
            }
            mSecondView.scrollToPosition(index);
            return this;
        }

        @SingleClick
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_ui_confirm:
                    autoDismiss();
                    if (mListener != null) {
                        mListener.onSelected(getDialog(), mMonthDayAdapter.getItem(mMonthDayManager.getPickedPosition())
                                , mHourManager.getPickedPosition(),
                                mMinuteManager.getPickedPosition(), mSecondManager.getPickedPosition());
                    }
                    break;
                case R.id.tv_ui_cancel:
                    autoDismiss();
                    if (mListener != null) {
                        mListener.onCancel(getDialog());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private static final class PickerAdapter extends MyAdapter<String> {

        private PickerAdapter(Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder();
        }

        final class ViewHolder extends MyAdapter.ViewHolder {

            private final TextView mPickerView;

            ViewHolder() {
                super(R.layout.item_picker);
                mPickerView = (TextView) findViewById(R.id.tv_picker_name);
            }

            @Override
            public void onBindView(int position) {
                mPickerView.setText(getItem(position));
            }
        }
    }

    public interface OnListener {

        /**
         * 选择完时间后回调
         *
         * @param monthDay 月份天数
         * @param hour     时钟
         * @param minute   分钟
         * @param second   秒钟
         */
        void onSelected(BaseDialog dialog, String monthDay, int hour, int minute, int second);

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {
        }
    }
}