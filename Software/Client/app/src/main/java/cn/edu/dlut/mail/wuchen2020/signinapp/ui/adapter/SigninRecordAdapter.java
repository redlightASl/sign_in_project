package cn.edu.dlut.mail.wuchen2020.signinapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import cn.edu.dlut.mail.wuchen2020.signinapp.R;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.SigninRecord;

public class SigninRecordAdapter extends RecyclerView.Adapter<SigninRecordAdapter.ViewHolder> {
    private List<SigninRecord> records;

    public SigninRecordAdapter() {
        records = Collections.emptyList();
    }

    public List<SigninRecord> getRecords() {
        return records;
    }

    public void setRecords(List<SigninRecord> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_signin_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        SigninRecord record = records.get(position);
        viewHolder.getTextViewLesson().setText(record.getCourseName());
        viewHolder.getTextViewLocation().setText(record.getLocation());
        viewHolder.getTextViewTime().setText(record.getTime().toLocaleString());
        String str;
        switch (record.getStatus()) {
            case 0: str = "签到"; break;
            case 4: str = "暂离"; break;
            case 5: str = "返回"; break;
            case 6: str = "签退"; break;
            default: str = "未知状态"; break;
        }
        viewHolder.getTextViewStatus().setText(str);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textLesson;
        private final TextView textLocation;
        private final TextView textTime;
        private final TextView textStatus;

        public ViewHolder(View view) {
            super(view);
            textLesson = view.findViewById(R.id.text_lesson);
            textLocation = view.findViewById(R.id.text_location);
            textTime = view.findViewById(R.id.text_time);
            textStatus = view.findViewById(R.id.text_status);
        }

        public TextView getTextViewLesson() {
            return textLesson;
        }

        public TextView getTextViewLocation() {
            return textLocation;
        }

        public TextView getTextViewTime() {
            return textTime;
        }

        public TextView getTextViewStatus() {
            return textStatus;
        }
    }
}
