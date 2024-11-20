package com.example.signup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    private final List<WordItem> wordList;
    private final Context context;

    public WordAdapter(Context context, List<WordItem> wordList) {
        this.context = context;
        this.wordList = wordList;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_word, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        WordItem wordItem = wordList.get(position);
        holder.wordText.setText(wordItem.getWord());
        holder.meaningText.setText(wordItem.getMeaning());

        // Kiểm tra trạng thái đã lưu hay chưa
        if (wordItem.isSaved()) {
            holder.saveIcon.setImageResource(R.drawable.bookmark_circle_filled);
        } else {
            holder.saveIcon.setImageResource(R.drawable.bookmark_circle);
        }

        holder.saveIcon.setOnClickListener(v -> {
//            Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up);
//            Animation scaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down);
            ObjectAnimator rotationOut = ObjectAnimator.ofFloat(holder.saveIcon, "rotationX", 0f, 90f);
            ObjectAnimator rotationIn = ObjectAnimator.ofFloat(holder.saveIcon, "rotationX", -90f, 0f);

            rotationOut.setDuration(150);
            rotationIn.setDuration(150);

//            wordItem.setSaved(!wordItem.isSaved()); // Chuyển đổi trạng thái lưu
//            if (wordItem.isSaved()) {
//                holder.saveIcon.setImageResource(R.drawable.bookmark_circle_filled);
//            } else {
//                holder.saveIcon.setImageResource(R.drawable.bookmark_circle);
//            }
//            holder.saveIcon.startAnimation(scaleUp);
//            scaleUp.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {}
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    holder.saveIcon.startAnimation(scaleDown);
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {}
//            });
            rotationOut.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    // Chuyển đổi trạng thái lưu
                    wordItem.setSaved(!wordItem.isSaved());

                    // Cập nhật hình ảnh khi xoay xong một nửa
                    if (wordItem.isSaved()) {
                        holder.saveIcon.setImageResource(R.drawable.bookmark_circle_filled);
                    } else {
                        holder.saveIcon.setImageResource(R.drawable.bookmark_circle);
                    }
                    // Bắt đầu xoay ngược lại
                    rotationIn.start();
                }
            });

            // Bắt đầu hiệu ứng xoay
            rotationOut.start();
        });
    }


    @Override
    public int getItemCount() {
        return wordList.size();
    }

    static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView wordText, meaningText;
        ImageView saveIcon;

        WordViewHolder(View itemView) {
            super(itemView);
            wordText = itemView.findViewById(R.id.word_text);
            meaningText = itemView.findViewById(R.id.meaning_text);
            saveIcon = itemView.findViewById(R.id.save_icon);
        }
    }
}
