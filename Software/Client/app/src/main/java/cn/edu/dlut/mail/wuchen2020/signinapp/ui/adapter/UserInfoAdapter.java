package cn.edu.dlut.mail.wuchen2020.signinapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cn.edu.dlut.mail.wuchen2020.signinapp.R;

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.ViewHolder> {

    private final ArrayList<Pair<String, String>> userInfo;

    public UserInfoAdapter() {
        userInfo = new ArrayList<>();
    }

    public void putUserInfo(String name, String value) {
        for (int i = 0; i < userInfo.size(); i++) {
            if (name.equals(userInfo.get(i).first)) {
                userInfo.set(i, Pair.create(name, value));
                return;
            }
        }
        userInfo.add(Pair.create(name, value));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Pair<String, String> pair =  userInfo.get(position);
        viewHolder.getTextViewKey().setText(pair.first);
        viewHolder.getTextViewValue().setText(pair.second);
    }

    @Override
    public int getItemCount() {
        return userInfo.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textKey;
        private final TextView textValue;

        public ViewHolder(View view) {
            super(view);
            textKey = view.findViewById(R.id.text_key);
            textValue = view.findViewById(R.id.text_value);
        }

        public TextView getTextViewKey() {
            return textKey;
        }

        public TextView getTextViewValue() {
            return textValue;
        }
    }
}
