package com.mei.myimageloader;

import com.mei.imageloader.ImageLoader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ImageListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private List<String> imageList = new ArrayList<String>() {
        {
            add("https://img.iplaysoft.com/wp-content/uploads/2019/free-images/free_stock_photo.jpg!0x0.webp");
            add("https://img95.699pic.com/photo/50046/5562.jpg_wh300.jpg");
            add("https://lh3.googleusercontent.com/proxy/4z3zFAwyDFYTdYuKrUWRyWLs4Kmif9bC5dm_J9wEsgNICgF_7ddDeQuKp8iBpeV8XyLPDihp3CAFKrs26QI5zwqvhBhYTH6L4EBFVLXSOI4ITOvhq5D13sFc0mXnA9Li");
            add("https://exp-picture.cdn.bcebos.com/2a1ecb460596b814e9fce0b043d246fe464e2283.jpg?x-bce-process=image%2Fresize%2Cm_lfit%2Cw_500%2Climit_1");
            add("https://static.veer.com/veer/static/resources/keyword/2020-02-19/533ed30de651499da1c463bca44b6d60.jpg");
            add("https://pic4.zhimg.com/v2-3be05963f5f3753a8cb75b6692154d4a_1440w.jpg?source=172ae18b");
            add("https://img95.699pic.com/photo/40100/6015.jpg_wh300.jpg");
            add("https://pic.qqtn.com/up/2019-9/15690311636958128.jpg");
            add("https://pic2.zhimg.com/v2-3b4fc7e3a1195a081d0259246c38debc_720w.jpg?source=172ae18b");
            add("https://static.runoob.com/images/demo/demo2.jpg");
            add("https://pic-bucket.ws.126.net/photo/0009/2019-12-10/F02FNPP40AI20009NOS.jpg");
            add("https://png.pngtree.com/thumb_back/fw800/back_our/20190625/ourmid/pngtree-girl-sea-background-picture-image_255114.jpg");
            add("https://static.veer.com/veer/static/resources/keyword/2020-02-19/b94a42fb11d64052ae5a9baa25f5370c.jpg");
            add("https://lh3.googleusercontent.com/proxy/J_osXJ1ualB2bu47YTZv7k5TVjDSab0lkzlcORLY1JL6-kWh-6UU6Xe-ChqgZ4Zl2w_lzDwL4PywMiP-OpLpPA11gK8_UrxuJceR90I2YbY3uj4");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQ_1nXsZUzK08hsx8IA_jMre_5mP56FR-tkEg&usqp=CAU");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSX8ZwJ3f2IHaLqlDe7nyz1WiH6FJW2O3oQkA&usqp=CAU");
            add("https://goss2.veer.com/creative/vcg/veer/612/veer-312002591.jpg");
            add("https://up.enterdesk.com/edpic_360_360/27/8f/93/278f938be4b460a57962d542eee989f6.jpg");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcROvNcbxQJPwp5aJnyjJhGoNn-gbsODe7b0Vw&usqp=CAU");
            add("https://lh3.googleusercontent.com/proxy/1xd7iM03kDSCzAOrA1jX7ZEAchfqKT5sbGrNEzMEoAEZnGUNWwLHwZXnIUVFoPv4WkDCQnWykmWPuvoBpz6HcdH6jUUu93xbfGq_TPPEAv8mAq4Y");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSQkbCGSp2OST3SskoRSecyGAfg4KBWamBE3w&usqp=CAU");
            add("https://img95.699pic.com/photo/40007/4125.jpg_wh300.jpg");
            add("https://wimg.ruan8.com/uploadimg/ico/2019/0918/1568785811648209.jpg");
            add("https://pic2.zhimg.com/v2-4bba972a094eb1bdc8cbbc55e2bd4ddf_1440w.jpg?source=172ae18b");
            add("https://lh3.googleusercontent.com/proxy/iWdS9uxZJZqLS6K80Fq1lhrUnfWVrO6Bm1qVdAbPA9nzDyvFuhy5NpoGMqAghOie1TaXjWfvMzSfoaV1Yj2Dgw_UPaQkXgYlWbtMeS_6fKJai7xedAIxzEz7P2xFPnMG7PwgFoezA_w");
            add("https://lh3.googleusercontent.com/proxy/PstnBVks2VkjcdhHtteie1BYu_BEWRJHF4DyXrwUyuzUIA0a1aFXud-OvtEWRvPm3cseJXquyxwYI2yTRaHEcxDLZ6Gtw0CF8TN8TZrwOnK0xeB9rIZd6DZIaTY");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQHaHlXq6TcbnIq8y2x6uIl0XzfYQt8OWVkaQ&usqp=CAU");
            add("https://tu.sioe.cn/gj/qiege/image.jpg");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQQHlJW3iX0eSx0YEsPnrToEC7iKW4m3TEgiQ&usqp=CAU");
            add("https://www.diyimei.net/upload/2018/1523631328703738.jpg");
            add("https://cdn.ai.qq.com/aiplat/page/product/visionimgidy/img/demo6-16a47e5d31.jpg?max_age=31536000");
            add("https://img95.699pic.com/photo/40008/0111.jpg_wh300.jpg");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRtkb8Qkk4yqBAbCn8P3x2bkwLbynnH7_t3pw&usqp=CAU");
            add("https://www.crazy-tutorial.com/wp-content/uploads/2019/09/%E4%B8%AD%E7%A7%8B%E7%AF%80%E5%9C%96%E7%89%87%E5%8C%85-1200x628.jpg");
            add("https://lh3.googleusercontent.com/proxy/Vdc2gprlFWWUZolKXr0dxuErZXT_8jlOB4q3kUpC6AEIswZjE3zdET0uWFwfodJVQMNnV6lMoRis0o5rfq1mJtILqYvvX7J96EuHUAt1qw");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQgdaa62aEVE1hqiMW116Ej7u9OmnieMLF3cw&usqp=CAU");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSDKBylvlmVzrb4s-WdSqUnG10LBN4hxHgGpg&usqp=CAU");

            add("https://img.iplaysoft.com/wp-content/uploads/2019/free-images/free_stock_photo.jpg!0x0.webp");
            add("https://img95.699pic.com/photo/50046/5562.jpg_wh300.jpg");
            add("https://lh3.googleusercontent.com/proxy/4z3zFAwyDFYTdYuKrUWRyWLs4Kmif9bC5dm_J9wEsgNICgF_7ddDeQuKp8iBpeV8XyLPDihp3CAFKrs26QI5zwqvhBhYTH6L4EBFVLXSOI4ITOvhq5D13sFc0mXnA9Li");
            add("https://exp-picture.cdn.bcebos.com/2a1ecb460596b814e9fce0b043d246fe464e2283.jpg?x-bce-process=image%2Fresize%2Cm_lfit%2Cw_500%2Climit_1");
            add("https://static.veer.com/veer/static/resources/keyword/2020-02-19/533ed30de651499da1c463bca44b6d60.jpg");
            add("https://pic4.zhimg.com/v2-3be05963f5f3753a8cb75b6692154d4a_1440w.jpg?source=172ae18b");
            add("https://img95.699pic.com/photo/40100/6015.jpg_wh300.jpg");
            add("https://pic.qqtn.com/up/2019-9/15690311636958128.jpg");
            add("https://pic2.zhimg.com/v2-3b4fc7e3a1195a081d0259246c38debc_720w.jpg?source=172ae18b");
            add("https://static.runoob.com/images/demo/demo2.jpg");
            add("https://pic-bucket.ws.126.net/photo/0009/2019-12-10/F02FNPP40AI20009NOS.jpg");
            add("https://png.pngtree.com/thumb_back/fw800/back_our/20190625/ourmid/pngtree-girl-sea-background-picture-image_255114.jpg");
            add("https://static.veer.com/veer/static/resources/keyword/2020-02-19/b94a42fb11d64052ae5a9baa25f5370c.jpg");
            add("https://lh3.googleusercontent.com/proxy/J_osXJ1ualB2bu47YTZv7k5TVjDSab0lkzlcORLY1JL6-kWh-6UU6Xe-ChqgZ4Zl2w_lzDwL4PywMiP-OpLpPA11gK8_UrxuJceR90I2YbY3uj4");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQ_1nXsZUzK08hsx8IA_jMre_5mP56FR-tkEg&usqp=CAU");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSX8ZwJ3f2IHaLqlDe7nyz1WiH6FJW2O3oQkA&usqp=CAU");
            add("https://goss2.veer.com/creative/vcg/veer/612/veer-312002591.jpg");
            add("https://up.enterdesk.com/edpic_360_360/27/8f/93/278f938be4b460a57962d542eee989f6.jpg");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcROvNcbxQJPwp5aJnyjJhGoNn-gbsODe7b0Vw&usqp=CAU");
            add("https://lh3.googleusercontent.com/proxy/1xd7iM03kDSCzAOrA1jX7ZEAchfqKT5sbGrNEzMEoAEZnGUNWwLHwZXnIUVFoPv4WkDCQnWykmWPuvoBpz6HcdH6jUUu93xbfGq_TPPEAv8mAq4Y");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSQkbCGSp2OST3SskoRSecyGAfg4KBWamBE3w&usqp=CAU");
            add("https://img95.699pic.com/photo/40007/4125.jpg_wh300.jpg");
            add("https://wimg.ruan8.com/uploadimg/ico/2019/0918/1568785811648209.jpg");
            add("https://pic2.zhimg.com/v2-4bba972a094eb1bdc8cbbc55e2bd4ddf_1440w.jpg?source=172ae18b");
            add("https://lh3.googleusercontent.com/proxy/iWdS9uxZJZqLS6K80Fq1lhrUnfWVrO6Bm1qVdAbPA9nzDyvFuhy5NpoGMqAghOie1TaXjWfvMzSfoaV1Yj2Dgw_UPaQkXgYlWbtMeS_6fKJai7xedAIxzEz7P2xFPnMG7PwgFoezA_w");
            add("https://lh3.googleusercontent.com/proxy/PstnBVks2VkjcdhHtteie1BYu_BEWRJHF4DyXrwUyuzUIA0a1aFXud-OvtEWRvPm3cseJXquyxwYI2yTRaHEcxDLZ6Gtw0CF8TN8TZrwOnK0xeB9rIZd6DZIaTY");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQHaHlXq6TcbnIq8y2x6uIl0XzfYQt8OWVkaQ&usqp=CAU");
            add("https://tu.sioe.cn/gj/qiege/image.jpg");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQQHlJW3iX0eSx0YEsPnrToEC7iKW4m3TEgiQ&usqp=CAU");
            add("https://www.diyimei.net/upload/2018/1523631328703738.jpg");
            add("https://cdn.ai.qq.com/aiplat/page/product/visionimgidy/img/demo6-16a47e5d31.jpg?max_age=31536000");
            add("https://img95.699pic.com/photo/40008/0111.jpg_wh300.jpg");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRtkb8Qkk4yqBAbCn8P3x2bkwLbynnH7_t3pw&usqp=CAU");
            add("https://www.crazy-tutorial.com/wp-content/uploads/2019/09/%E4%B8%AD%E7%A7%8B%E7%AF%80%E5%9C%96%E7%89%87%E5%8C%85-1200x628.jpg");
            add("https://lh3.googleusercontent.com/proxy/Vdc2gprlFWWUZolKXr0dxuErZXT_8jlOB4q3kUpC6AEIswZjE3zdET0uWFwfodJVQMNnV6lMoRis0o5rfq1mJtILqYvvX7J96EuHUAt1qw");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQgdaa62aEVE1hqiMW116Ej7u9OmnieMLF3cw&usqp=CAU");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSDKBylvlmVzrb4s-WdSqUnG10LBN4hxHgGpg&usqp=CAU");

            add("https://img.iplaysoft.com/wp-content/uploads/2019/free-images/free_stock_photo.jpg!0x0.webp");
            add("https://img95.699pic.com/photo/50046/5562.jpg_wh300.jpg");
            add("https://lh3.googleusercontent.com/proxy/4z3zFAwyDFYTdYuKrUWRyWLs4Kmif9bC5dm_J9wEsgNICgF_7ddDeQuKp8iBpeV8XyLPDihp3CAFKrs26QI5zwqvhBhYTH6L4EBFVLXSOI4ITOvhq5D13sFc0mXnA9Li");
            add("https://exp-picture.cdn.bcebos.com/2a1ecb460596b814e9fce0b043d246fe464e2283.jpg?x-bce-process=image%2Fresize%2Cm_lfit%2Cw_500%2Climit_1");
            add("https://static.veer.com/veer/static/resources/keyword/2020-02-19/533ed30de651499da1c463bca44b6d60.jpg");
            add("https://pic4.zhimg.com/v2-3be05963f5f3753a8cb75b6692154d4a_1440w.jpg?source=172ae18b");
            add("https://img95.699pic.com/photo/40100/6015.jpg_wh300.jpg");
            add("https://pic.qqtn.com/up/2019-9/15690311636958128.jpg");
            add("https://pic2.zhimg.com/v2-3b4fc7e3a1195a081d0259246c38debc_720w.jpg?source=172ae18b");
            add("https://static.runoob.com/images/demo/demo2.jpg");
            add("https://pic-bucket.ws.126.net/photo/0009/2019-12-10/F02FNPP40AI20009NOS.jpg");
            add("https://png.pngtree.com/thumb_back/fw800/back_our/20190625/ourmid/pngtree-girl-sea-background-picture-image_255114.jpg");
            add("https://static.veer.com/veer/static/resources/keyword/2020-02-19/b94a42fb11d64052ae5a9baa25f5370c.jpg");
            add("https://lh3.googleusercontent.com/proxy/J_osXJ1ualB2bu47YTZv7k5TVjDSab0lkzlcORLY1JL6-kWh-6UU6Xe-ChqgZ4Zl2w_lzDwL4PywMiP-OpLpPA11gK8_UrxuJceR90I2YbY3uj4");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQ_1nXsZUzK08hsx8IA_jMre_5mP56FR-tkEg&usqp=CAU");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSX8ZwJ3f2IHaLqlDe7nyz1WiH6FJW2O3oQkA&usqp=CAU");
            add("https://goss2.veer.com/creative/vcg/veer/612/veer-312002591.jpg");
            add("https://up.enterdesk.com/edpic_360_360/27/8f/93/278f938be4b460a57962d542eee989f6.jpg");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcROvNcbxQJPwp5aJnyjJhGoNn-gbsODe7b0Vw&usqp=CAU");
            add("https://lh3.googleusercontent.com/proxy/1xd7iM03kDSCzAOrA1jX7ZEAchfqKT5sbGrNEzMEoAEZnGUNWwLHwZXnIUVFoPv4WkDCQnWykmWPuvoBpz6HcdH6jUUu93xbfGq_TPPEAv8mAq4Y");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSQkbCGSp2OST3SskoRSecyGAfg4KBWamBE3w&usqp=CAU");
            add("https://img95.699pic.com/photo/40007/4125.jpg_wh300.jpg");
            add("https://wimg.ruan8.com/uploadimg/ico/2019/0918/1568785811648209.jpg");
            add("https://pic2.zhimg.com/v2-4bba972a094eb1bdc8cbbc55e2bd4ddf_1440w.jpg?source=172ae18b");
            add("https://lh3.googleusercontent.com/proxy/iWdS9uxZJZqLS6K80Fq1lhrUnfWVrO6Bm1qVdAbPA9nzDyvFuhy5NpoGMqAghOie1TaXjWfvMzSfoaV1Yj2Dgw_UPaQkXgYlWbtMeS_6fKJai7xedAIxzEz7P2xFPnMG7PwgFoezA_w");
            add("https://lh3.googleusercontent.com/proxy/PstnBVks2VkjcdhHtteie1BYu_BEWRJHF4DyXrwUyuzUIA0a1aFXud-OvtEWRvPm3cseJXquyxwYI2yTRaHEcxDLZ6Gtw0CF8TN8TZrwOnK0xeB9rIZd6DZIaTY");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQHaHlXq6TcbnIq8y2x6uIl0XzfYQt8OWVkaQ&usqp=CAU");
            add("https://tu.sioe.cn/gj/qiege/image.jpg");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQQHlJW3iX0eSx0YEsPnrToEC7iKW4m3TEgiQ&usqp=CAU");
            add("https://www.diyimei.net/upload/2018/1523631328703738.jpg");
            add("https://cdn.ai.qq.com/aiplat/page/product/visionimgidy/img/demo6-16a47e5d31.jpg?max_age=31536000");
            add("https://img95.699pic.com/photo/40008/0111.jpg_wh300.jpg");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRtkb8Qkk4yqBAbCn8P3x2bkwLbynnH7_t3pw&usqp=CAU");
            add("https://www.crazy-tutorial.com/wp-content/uploads/2019/09/%E4%B8%AD%E7%A7%8B%E7%AF%80%E5%9C%96%E7%89%87%E5%8C%85-1200x628.jpg");
            add("https://lh3.googleusercontent.com/proxy/Vdc2gprlFWWUZolKXr0dxuErZXT_8jlOB4q3kUpC6AEIswZjE3zdET0uWFwfodJVQMNnV6lMoRis0o5rfq1mJtILqYvvX7J96EuHUAt1qw");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQgdaa62aEVE1hqiMW116Ej7u9OmnieMLF3cw&usqp=CAU");
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSDKBylvlmVzrb4s-WdSqUnG10LBN4hxHgGpg&usqp=CAU");


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, int itemPosition,
                    @NonNull RecyclerView parent) {
                super.getItemOffsets(outRect, itemPosition, parent);
                outRect.set(5, 5, 5, 5);
            }
        });
        mRecyclerView.setAdapter(new RecyclerView.Adapter<ImageListActivity.ViewHolder>() {
            @NonNull
            @Override
            public ImageListActivity.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                    int viewType) {
                View view = LayoutInflater.from(ImageListActivity.this)
                        .inflate(R.layout.item_image_view, parent, false);
                return new ImageListActivity.ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ImageListActivity.ViewHolder holder, int position) {
                ImageLoader.getInstance(ImageListActivity.this)
                        .bindBitmap(getItem(position), holder.imageView);

                holder.itemView.setOnClickListener(v -> {
                    PreviewImageActivity
                            .start(ImageListActivity.this, holder.imageView, getItem(position));
                });
            }

            @Override
            public int getItemCount() {
                return imageList.size();
            }

            public String getItem(int position) {
                return position < 0 || position >= getItemCount() ? null : imageList.get(position);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}