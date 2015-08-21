package com.wezebra.zebraking.ui.installment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.model.ShapeHolder;
import com.wezebra.zebraking.model.TextHolder;

import java.util.ArrayList;
import java.util.Random;

public class AssessCountdownActivity extends ActionBarActivity
{
    private RelativeLayout countdownDonePage;
    private Button countdownDone;
    private int centerX = 0;
    private int centerY = 0;
    private ImageView iv;
    private TextView timeText;
    private ImageView iv2;
    private ImageView iv3;
    private Animation largeIn;
    private Animation largeFadeOut;
    private Animation smallIn;
    private Animation flipIn;
    private Animation flipOut;

    private RelativeLayout frontLayout;
    private RelativeLayout animLayout;
    private PositionGenerator positionGenerator;
    private String[] testStrings = {"姓名", "学历", "工作状态", "找房状态", "收入", "交租方式"};
    private static final String[] colors = {"#09f3fb", "#7f695a", "#fd6e86", "#acacac", "#51fbc7", "#428b98", "#ecfb50", "#54a0fe", "#d7b3bc"};
    int textIndex = 0;
    Random random = new Random();
    private int countTime = 10;
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 1:
                    timeText.setText(countTime + "s");
                    countTime--;
                    break;
                case 2:
                    countTime--;
                    Intent intent = new Intent();
                    switch (qualificationStatus) {
                        case 99:
                            intent.setClass(getApplicationContext(),AssessmentUnpassedActivity.class);
                            break;
                        case 100:
                            intent.setClass(getApplicationContext(),AssessmentResultActivity.class);
                            break;
                    }
                    startActivity(intent);
                    finish();
                    //countdownDonePage.setVisibility(View.VISIBLE);
//                    frontLayout.startAnimation(flipOut);
//                    countdownDonePage.startAnimation(flipIn);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    Runnable timeRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            if (countTime >= 0)
            {
                Message message = handler.obtainMessage();
                if (countTime != 0)
                {
                    message.what = 1;
                } else
                {
                    message.what = 2;
                }
                message.sendToTarget();
                handler.postDelayed(this, 1000);
            }
        }
    };

    Runnable popRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            iv2.startAnimation(largeIn);
            handler.postDelayed(this, 1100);
        }
    };

    private int qualificationStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.flip_in, R.anim.flip_out);
        setContentView(R.layout.activity_assess_countdown);

        qualificationStatus = getIntent().getIntExtra("qualificationStatus",99);

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String diploma = "";
        switch (bundle.getInt("diploma")) {
            case -1:
                diploma = "高中以下";
                break;
            case 1:
                diploma = "高中";
                break;
            case 2:
                diploma = "专科";
                break;
            case 3:
                diploma = "本科";
                break;
            case 4:
                diploma = "硕士";
                break;
            case 5:
                diploma = "博士";
                break;
        }
        String employment = "";
        switch (bundle.getInt("employment")) {
            case 1:
                employment = "已工作";
                break;
            case 2:
                employment = "未工作";
                break;
        }
        String isFound = "";
        switch (bundle.getInt("isFound")) {
            case 1:
                isFound = "已找到房";
                break;
            case 2:
                isFound = "还在找房";
                break;
        }
        String income = "";
        switch (bundle.getInt("income")) {
            case 1:
                income = "1000以下";
                break;
            case 2:
                income = "1000-2000";
                break;
            case 3:
                income = "2000-3000";
                break;
            case 4:
                income = "3000-5000";
                break;
            case 5:
                income = "5000-8000";
                break;
            case 6:
                income = "8000-12000";
                break;
            case 7:
                income = "12000以上";
                break;
        }
        String how_to_pay = "";
        switch (bundle.getInt("how_to_pay")) {
            case 3:
                how_to_pay = "季付";
                break;
            case 6:
                how_to_pay = "半年付";
                break;
        }
        String[] s = new String[6];
        s[0] = name;
        s[1] = diploma;
        s[2] = employment;
        s[3] = isFound;
        s[4] = income;
        s[5] = how_to_pay;
        testStrings = s;

        frontLayout = (RelativeLayout)findViewById(R.id.front_layout);
        countdownDonePage = (RelativeLayout)findViewById(R.id.countdown_done_page);
        countdownDone=(Button)findViewById(R.id.countdown_done);
        animLayout = (RelativeLayout) findViewById(R.id.anim_layout);
        timeText = (TextView) findViewById(R.id.time_text);
        iv = (ImageView) findViewById(R.id.img);
        iv2 = (ImageView) findViewById(R.id.img2);
        iv3 = (ImageView) findViewById(R.id.img3);

        largeIn = AnimationUtils.loadAnimation(this, R.anim.large_in);
        largeFadeOut = AnimationUtils.loadAnimation(this, R.anim.large_fade_out);
        smallIn = AnimationUtils.loadAnimation(this, R.anim.small_in);
        flipIn = AnimationUtils.loadAnimation(this, R.anim.flip_in);
        flipOut = AnimationUtils.loadAnimation(this, R.anim.flip_out);

        largeIn.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
                iv2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                iv2.startAnimation(largeFadeOut);
                iv3.startAnimation(smallIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });

        smallIn.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
                iv3.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                iv3.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });

        largeFadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                iv2.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                int width = iv.getWidth();
                int height = iv.getHeight();
                int left = iv.getLeft();
                int top = iv.getTop();

                centerX = left + width / 2;
                centerY = top + height / 2;

                positionGenerator = new PositionGenerator(animLayout.getRight(), animLayout.getBottom());
            }
        }, 100);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        animation.setInterpolator(new LinearInterpolator());
        iv.startAnimation(animation);

//        RelativeLayout layout = (RelativeLayout) findViewById(R.id.container);
        animLayout.addView(new MyAnimationView(this));
        animLayout.addView(new TextAnimationView(this));


        handler.post(timeRunnable);
        handler.postDelayed(popRunnable,1000);
    }

    public void countdownDoneClick(View v)
    {
        Intent intent = new Intent(this, SubmitInstallmentApplicationStepOneActivity.class);
        startActivity(intent);
        finish();
//        overridePendingTransition(R.anim.flip_in,R.anim.flip_out);
    }

    @Override
    public void onBackPressed()
    {
        return;
    }

    public class MyAnimationView extends View
    {
        public final ArrayList<ShapeHolder> balls = new ArrayList<ShapeHolder>();

        public MyAnimationView(Context context)
        {
            super(context);
            handler.postDelayed(runnable, 100);
            handler.postDelayed(shapeRunnable, 100);
        }

        Handler handler = new Handler();
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                invalidate();
                handler.postDelayed(this, 20);
            }
        };

        Runnable shapeRunnable = new Runnable()
        {
            @Override
            public void run()
            {

                int i = random.nextInt(2);

                float size;
                float[] position;
                if (i == 0)
                {
                    size = 10 + random.nextInt(5);
                    position = positionGenerator.generateSmallShapePosition();
                } else
                {
                    size = 25 + random.nextInt(15);
                    position = positionGenerator.generateBigShapePosition();
                }

                ShapeHolder newBall = addBall(position[0], position[1], size);

                float startX = newBall.getX();
                float startY = newBall.getY();

                ValueAnimator animX = ObjectAnimator.ofFloat(newBall, "x", startX, centerX - random.nextInt(30));
                ValueAnimator animY = ObjectAnimator.ofFloat(newBall, "y", startY, centerY - random.nextInt(30));
                ValueAnimator animW = ObjectAnimator.ofFloat(newBall, "width", size,10);
                ValueAnimator animH = ObjectAnimator.ofFloat(newBall,"height", size,10);
                animX.setDuration(1500);
                animY.setDuration(1500);
                animW.setDuration(1500);
                animH.setDuration(1500);

                // Fading animation - remove the ball when the animation is done
                ValueAnimator fadeAnim = ObjectAnimator.ofFloat(newBall, "alpha",
                        1f, 0f);
                fadeAnim.setDuration(1500);
                fadeAnim.setInterpolator(new LinearInterpolator());
                fadeAnim.addListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        balls.remove(((ObjectAnimator) animation).getTarget());

                    }
                });

                AnimatorSet set = new AnimatorSet();
                set.play(animX).with(animY).with(animW).with(animH).with(fadeAnim);
                set.start();

                handler.postDelayed(this, 100);
            }
        };

        private ShapeHolder addBall(float x, float y, float size)
        {
            OvalShape circle = new OvalShape();
            circle.resize(size, size);
            ShapeDrawable drawable = new ShapeDrawable(circle);
            ShapeHolder shapeHolder = new ShapeHolder(drawable);
            shapeHolder.setX(x - size / 2);
            shapeHolder.setY(y - size / 2);
//            int red = (int) (Math.random() * 255);
//            int green = (int) (Math.random() * 255);
//            int blue = (int) (Math.random() * 255);
//            int color = 0xff000000 | red << 16 | green << 8 | blue;


            Paint paint = drawable.getPaint(); // new Paint(Paint.ANTI_ALIAS_FLAG);

            paint.setColor(Color.parseColor(colors[random.nextInt(colors.length)]));
            shapeHolder.setPaint(paint);
            balls.add(shapeHolder);
            return shapeHolder;
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            for (int i = 0; i < balls.size(); ++i)
            {
                ShapeHolder shapeHolder = balls.get(i);
                canvas.save();
                canvas.translate(shapeHolder.getX(), shapeHolder.getY());
                shapeHolder.getShape().draw(canvas);

                canvas.restore();
            }
        }
    }

    public class TextAnimationView extends View
    {
        public final ArrayList<TextHolder> texts = new ArrayList<>();

        public TextAnimationView(Context context)
        {
            super(context);
            setBackgroundColor(Color.TRANSPARENT);
            handler.postDelayed(runnable, 100);
            handler.postDelayed(textRunnable, 100);
        }

        Handler handler = new Handler();
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                invalidate();
                handler.postDelayed(this, 20);
            }
        };

        Runnable textRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                float[] position = positionGenerator.generateTextPosition();

                TextHolder newText = addText(position[0], position[1]);

                float startX = newText.getX();
                float startY = newText.getY();

                ValueAnimator textanimX = ObjectAnimator.ofFloat(newText, "x", startX, centerX / 2);
                ValueAnimator textanimY = ObjectAnimator.ofFloat(newText, "y", startY, centerY / 2);
                textanimX.setDuration(1500);
                textanimY.setDuration(1500);

                ValueAnimator textSizeAnim = ObjectAnimator.ofFloat(newText, "size", 60f, 10f);
                textSizeAnim.setDuration(1500);

                // Fading animation - remove the text when the animation is done
                ValueAnimator textfadeAnim = ObjectAnimator.ofFloat(newText, "alpha",
                        1f, 0f);
                textfadeAnim.setDuration(1500);
                textfadeAnim.setInterpolator(new AccelerateInterpolator());
                textfadeAnim.addListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        texts.remove(((ObjectAnimator) animation).getTarget());
                    }
                });

                AnimatorSet set = new AnimatorSet();
                set.play(textanimX).with(textanimY).with(textfadeAnim).with(textSizeAnim);
                set.start();


                handler.postDelayed(this, 500);
            }
        };

        private TextHolder addText(float x, float y)
        {
            TextHolder holder = new TextHolder();
            if (textIndex == testStrings.length - 1)
            {
                textIndex = 0;
            } else
            {
                textIndex++;
            }
            holder.setText(testStrings[textIndex]);
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#a4a9b2"));
            holder.setPaint(paint);
            holder.setX(x / 2);
            holder.setY(y / 2);

            texts.add(holder);

            return holder;
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            for (int i = 0; i < texts.size(); ++i)
            {
                TextHolder textHolder = texts.get(i);
                canvas.save();
                canvas.translate(textHolder.getX(), textHolder.getY());

                canvas.drawText(textHolder.getText(), textHolder.getX(), textHolder.getY(), textHolder.getPaint());
                canvas.restore();
            }
        }
    }

    private class PositionGenerator
    {
        int maxX;
        int maxY;

        public PositionGenerator(int maxX, int maxY)
        {
            this.maxX = maxX;
            this.maxY = maxY;
        }

        public float[] generateTextPosition()
        {
            float[] position = new float[2];

            int maxX = this.maxX - 150;

            float x;
            float y;

//            if (maxX / 5 < x && x < maxX * 4 / 5)
//            {
//                while (maxY / 5 < y && y < maxY * 4 / 5)
//                {
//                    int i = random.nextInt(2);
//                    if (i == 0)
//                    {
//                        y = random.nextInt(maxY / 5);
//                    } else
//                    {
//                        y = maxY * 4 / 5 + random.nextInt(maxY / 5);
//                    }
//                }
//            }

            int i = random.nextInt(4);
            if (i == 0)
            {
                x = maxX / 6 + random.nextInt(maxX * 5 / 6);
                y = maxY / 6;
            } else if (i == 1)
            {
                x = maxX / 6 + random.nextInt(maxX * 5 / 6);
                y = maxY * 5 / 6;
            } else if (i == 2)
            {
                x = maxX / 6;
                y = maxY / 6 + random.nextInt(maxY * 5 / 6);
            } else
            {
                x = maxX * 5 / 6;
                y = maxY / 6 + random.nextInt(maxY * 5 / 6);
            }

            position[0] = x;
            position[1] = y;

            return position;
        }

        public float[] generateBigShapePosition()
        {
            float[] position = new float[2];
            float x;
            float y;

            int i = random.nextInt(4);
            if (i == 0)
            {
                x = maxX / 8 + random.nextInt(maxX * 7 / 8);
                y = maxY / 8;
            } else if (i == 1)
            {
                x = maxX / 8 + random.nextInt(maxX * 7 / 8);
                y = maxY * 7 / 8;
            } else if (i == 2)
            {
                x = maxX / 8;
                y = maxY / 8 + random.nextInt(maxY * 7 / 8);
            } else
            {
                x = maxX * 7 / 8;
                y = maxY / 8 + random.nextInt(maxY * 7 / 8);
            }

            position[0] = x;
            position[1] = y;

            return position;
        }

        public float[] generateSmallShapePosition()
        {
            float[] position = new float[2];
            float x;
            float y;

            int i = random.nextInt(4);
            if (i == 0)
            {
                x = maxX / 4 + random.nextInt(maxX * 3 / 4);
                y = maxY / 4;
            } else if (i == 1)
            {
                x = maxX / 4 + random.nextInt(maxX * 3 / 4);
                y = maxY * 3 / 4;
            } else if (i == 2)
            {
                x = maxX / 4;
                y = maxY / 4 + random.nextInt(maxY * 3 / 4);
            } else
            {
                x = maxX * 3 / 4;
                y = maxY / 4 + random.nextInt(maxY * 3 / 4);
            }

            position[0] = x;
            position[1] = y;

            return position;
        }
    }
}
