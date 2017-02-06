package com.example.tacademy.recycleviewtest.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tacademy.recycleviewtest.R;

/**
 * Created by Tacademy on 2017-02-06.
 */

public class PostViewHolder extends RecyclerView.ViewHolder{

    ImageView profile, heart;
    TextView nickName, heart_count, title, content;

    public PostViewHolder(View itemView) {
        super(itemView);

        profile = (ImageView)itemView.findViewById(R.id.profile);
        heart = (ImageView)itemView.findViewById(R.id.heart);
        nickName = (TextView)itemView.findViewById(R.id.nickName);
        heart_count = (TextView)itemView.findViewById(R.id.heart_count);
        title = (TextView)itemView.findViewById(R.id.title);
        content = (TextView)itemView.findViewById(R.id.content);

    }

    // 데이터를 개별 뷰에 설정
    public void bindToPost(Post model, View.OnClickListener listener){
        profile.setOnClickListener(listener);
        heart.setOnClickListener(listener);
        nickName.setText(model.getAuthor());
        heart_count.setText(""+model.getHeart_count());
        title.setText(model.getTitle());
        content.setText(model.getContent());
    }
}
