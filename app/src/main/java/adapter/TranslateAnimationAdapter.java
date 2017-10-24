package adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.ullas.recyclerswipeview.R;

import java.util.ArrayList;

import data.Data;

public class TranslateAnimationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final long GAP_BETWEEN_CLICKS = 600L;
    private static final int ANIMATION_DURATION = 250;

    private final Context context;
    private ArrayList<Data> countries;
    private int previousPosition = -1;
    private long previousClickedTime = 0L;


    public TranslateAnimationAdapter(Context context, ArrayList<Data> countries) {
        this.countries = countries;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new CountryViewHolder(inflater.inflate(R.layout.row_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int i) {
        CountryViewHolder holder = (CountryViewHolder) viewHolder;
        holder.bindView(countries.get(i));
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }


    private class CountryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCountry;
        private View outerLayout;
        private View zLayout;
        private View sLayout;
        private TextView zText;
        private TextView sText;

        CountryViewHolder(View view) {
            super(view);
            tvCountry = (TextView) view.findViewById(R.id.tv_country);
            outerLayout = view.findViewById(R.id.outer_layout);
            zLayout = view.findViewById(R.id.z_layout);
            sLayout = view.findViewById(R.id.swiggy_layout);
            zText = (TextView) view.findViewById(R.id.z_text);
            sText = (TextView) view.findViewById(R.id.s_text);
            animationListener();
            clickListeners();
        }


        @SuppressLint("SetTextI18n")
        void bindView(Data data) {
            float position = -outerLayout.getTranslationX() + data.x + outerLayout.getX();
            outerLayout.setTranslationX(position);

            tvCountry.setText(data.name);
            zText.setText("Z : " + (getAdapterPosition()));
            sText.setText("S : " + (getAdapterPosition()));
        }

        void animationListener() {
            outerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickEligibility()) {
                        previousClickedTime = System.currentTimeMillis();
                        if (countries.get(getAdapterPosition()).toAnimate) {
                            rightAnimation();
                        } else {
                            leftAnimation();
                        }
                    }
                }
            });
        }

        private void leftAnimation() {
            TranslateAnimation animation = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, dp(-200), Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, 0);
            animation.setDuration(ANIMATION_DURATION);
            animation.setFillAfter(true);
            outerLayout.startAnimation(animation);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if (previousPosition != -1 && previousPosition < countries.size() && previousPosition != getAdapterPosition()) {
                        countries.get(previousPosition).toAnimate = false;
                        countries.get(previousPosition).x = 0;
                        notifyItemChanged(previousPosition);
                    }
                    previousPosition = getAdapterPosition();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    countries.get(getAdapterPosition()).toAnimate = true;
                    countries.get(getAdapterPosition()).x = dp(-200);
                    notifyItemChanged(getAdapterPosition());
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        private void rightAnimation() {
            TranslateAnimation animation = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, dp(200), Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, 0);
            animation.setDuration(ANIMATION_DURATION);
            animation.setFillAfter(true);
            outerLayout.startAnimation(animation);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (previousPosition == getAdapterPosition()) {
                        previousPosition = -1;
                    }
                    countries.get(getAdapterPosition()).toAnimate = false;
                    countries.get(getAdapterPosition()).x = dp(0);
                    notifyItemChanged(getAdapterPosition());

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        private void clickListeners() {
            zLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Z : " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });

            sLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "S : " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        int dp(float value) {
            return (int) Math.ceil(context.getResources().getDisplayMetrics().density * value);
        }

        private boolean clickEligibility() {
            if (!countries.get(getAdapterPosition()).isBothNotPresent
                    && (System.currentTimeMillis() > (previousClickedTime + GAP_BETWEEN_CLICKS))) {
                return true;
            }
            if(!countries.get(getAdapterPosition()).isBothNotPresent){
                Toast.makeText(context, "Both are not present", Toast.LENGTH_SHORT).show();
            }
            if(System.currentTimeMillis() > (previousClickedTime + GAP_BETWEEN_CLICKS)){
                Toast.makeText(context, "Clicked really fast", Toast.LENGTH_SHORT).show();
            }
            return false;
        }

    }
}