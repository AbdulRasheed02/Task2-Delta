package com.example.task2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.RequiresApi;
import java.util.Random;

public class CustomViewLayout extends SurfaceView implements Runnable {

    Thread thread=null;
    Boolean canDraw=false;
    Boolean flag, HardMode, gameOverSoundEffectBool;
    Context context;

    Canvas canvas;
    SurfaceHolder surfaceHolder;

    int circle_x,circle_y,x_direction,y_direction,circle_radius;
    int speedx,speedy;
    int randomx;

    Random random;
    Paint paint_white, paint_grey;

    Rect topwall, leftwall, rightwall, slider;
    int wallThickness, sliderThickness, sliderLength;

    int score,highScore;

    RectF btn1Background,btn2Background;
    Paint textPaint;

    MediaPlayer sliderSoundEffect,wallSoundEffect,gameOverSoundEffect;

    public CustomViewLayout(Context context) {
        super(context);
        surfaceHolder=getHolder();
        canvas=new Canvas();
        this.context=context;
    }

    public CustomViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomViewLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void run() {
        initialize();
        paint();

        while (canDraw){
            if(!surfaceHolder.getSurface().isValid()){
                continue;
            }
            canvas=surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            walls();
            movementCircle();
            slider();
            collisionDetection();
            score();
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause(){

        canDraw=false;

        while (true) {
            try {
                thread.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        thread=null;
    }

    public void resume(){
        canDraw=true;
        thread=new Thread(this);
        thread.start();
    }

    private void initialize(){
        score=0;
        Bundle transporter = ((Activity)getContext()).getIntent().getExtras();
        HardMode=transporter.getBoolean("HardMode");

        random=new Random();
        randomx=random.nextInt(2);
        if(randomx==0){
            x_direction=1;
        }
        else{
            x_direction=-1;
        }
        y_direction=1;
        circle_radius=30;

        speedx=10+random.nextInt(5);
        speedy=10+random.nextInt(5);

        topwall=new Rect();
        leftwall=new Rect();
        rightwall=new Rect();
        wallThickness=40;

        slider=new Rect();
        sliderThickness=50;
        sliderLength=250;

        flag=true;

        sliderSoundEffect=MediaPlayer.create(getContext(),R.raw.slider);
        wallSoundEffect=MediaPlayer.create(getContext(),R.raw.wall);
        gameOverSoundEffect=MediaPlayer.create(getContext(),R.raw.gameover);

        gameOverSoundEffectBool=true;
    }

    private void walls(){
        topwall.left=0;
        topwall.top=200;
        topwall.right=canvas.getWidth();
        topwall.bottom=topwall.top+wallThickness;

        leftwall.left=0;
        leftwall.top=topwall.bottom;
        leftwall.right=leftwall.left+wallThickness;
        leftwall.bottom=canvas.getHeight();

        rightwall.left=canvas.getWidth()-wallThickness;
        rightwall.top=topwall.bottom;
        rightwall.right=canvas.getWidth();
        rightwall.bottom=canvas.getHeight();

        canvas.drawRect(topwall,paint_grey);
        canvas.drawRect(leftwall,paint_grey);
        canvas.drawRect(rightwall,paint_grey);
    }

    private void movementCircle(){

        if(circle_y-circle_radius>=canvas.getHeight()){
            endGame();
        }
        else {
            if (circle_x == 0 && circle_y == 0) {
                circle_x = canvas.getWidth() / 2;
                circle_y = (canvas.getHeight() + topwall.bottom) / 2;
            }

            if (circle_x + circle_radius >= canvas.getWidth() - wallThickness) {
                x_direction = -1;
                wallSoundEffect.start();
            }
            if (circle_x - circle_radius <= wallThickness) {
                x_direction = 1;
                wallSoundEffect.start();
            }
            if (circle_y - circle_radius <= topwall.bottom) {
                y_direction = 1;
                wallSoundEffect.start();
            }
            circle_x = circle_x + (x_direction * speedx);
            circle_y = circle_y + (y_direction * speedy);

            canvas.drawCircle(circle_x, circle_y, circle_radius, paint_white);
        }
    }


    private void slider(){
        if(slider.left==0 || slider.top==0) {
            slider.left = (canvas.getWidth() - sliderLength) / 2;
            slider.top = canvas.getHeight() - sliderThickness;
            slider.right = slider.left + sliderLength;
            slider.bottom = slider.top + sliderThickness;
        }
        canvas.drawRect(slider, paint_grey);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean value = super.onTouchEvent(event);

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN: {
                float downx=event.getX();
                float downy=event.getY();

                if(!flag){
                    if(downx>=btn1Background.left && downx<=btn1Background.right && downy>= btn1Background.top && downy <= btn1Background.bottom){
                        Intent Restart= new Intent(context, CustomViewActivity.class);
                        Restart.putExtra("HardMode", HardMode);
                        context.startActivity(Restart);
                    }
                    else if(downx>=btn2Background.left && downx<=btn2Background.right && downy>= btn2Background.top && downy <= btn2Background.bottom){
                        context.startActivity(new Intent(context, MainActivity.class));
                    }

                }

                return true;
            }

            case MotionEvent.ACTION_MOVE:{
                float x=event.getX();

                if(x-(sliderLength/2)<=leftwall.right || x+(sliderLength/2)>=rightwall.left){
                    return true;
                }
                else if(x>=slider.left && x<=slider.right){
                    if(flag) {
                        slider.left = (int) x - (sliderLength / 2);
                        slider.right = (int) x + (sliderLength / 2);
                    }
                    return true;
                }
                return value;
            }
        }
        return value;
    }

    private void collisionDetection(){
        if(circle_x+circle_radius>=slider.left && circle_x-circle_radius<=slider.right && circle_y+circle_radius>=slider.top && circle_y+circle_radius<=slider.bottom){
            y_direction=-1;
            sliderSoundEffect.start();
            if(HardMode){
               speedx=speedx+1+random.nextInt(2);
               speedy=speedy+1+random.nextInt(2);
            }
            score++;
        }
        if(circle_x+circle_radius>=slider.left && circle_x+circle_radius<=slider.left+(sliderLength/2) && circle_y>=slider.top){
            x_direction=-1;
            sliderSoundEffect.start();
        }
        if(circle_x-circle_radius<=slider.right && circle_x-circle_radius>=slider.left+(sliderLength/2) && circle_y>=slider.top){
            x_direction=1;
            sliderSoundEffect.start();
        }
    }

    private void score(){
        paint_white.setTextAlign(Paint.Align.CENTER);
        paint_white.setTextSize(100);
        canvas.drawText("Score : "+score,canvas.getWidth()/2,140,paint_white);
    }

    private void endGame(){
        paint_white.setTextAlign(Paint.Align.CENTER);
        paint_white.setTextSize(120);
        canvas.drawText("GAME OVER",canvas.getWidth()/2,canvas.getHeight()/2,paint_white);
        flag=false;
        buttons();

        SharedPreferences prefs = context.getSharedPreferences("highScorePrefsKey", Context.MODE_PRIVATE);
        highScore = prefs.getInt("highScoreKey", 0);

        if(score>=highScore){
            highScore=score;
        }

        prefs = context.getSharedPreferences("highScorePrefsKey", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("highScoreKey", highScore);
        editor.commit();


        if(gameOverSoundEffectBool) {
            gameOverSoundEffect.start();
            gameOverSoundEffectBool=false;
        }
    }

    private void buttons(){
        btn1Background=new RectF();
        btn2Background=new RectF();
        textPaint=new Paint();

        String str_restart = "RESTART";
        String str_menu="MENU";
        int margin=10;

        Paint.FontMetrics fm = new Paint.FontMetrics();
        textPaint.setColor(Color.parseColor("#808080"));
        textPaint.setTextSize(75.0F);
        textPaint.getFontMetrics(fm);

        btn1Background.left=(canvas.getWidth()/2)-margin-(textPaint.measureText(str_restart)/2)-10;
        btn1Background.top=((canvas.getHeight()-topwall.bottom)/2)+fm.top-margin+550+20;
        btn1Background.right=(canvas.getWidth()/2)+margin+(textPaint.measureText(str_restart)/2)+10;
        btn1Background.bottom=((canvas.getHeight()-topwall.bottom)/2)+fm.bottom+margin+550-10;

        btn2Background.left=(canvas.getWidth()/2)-margin-(textPaint.measureText(str_menu)/2)-10;
        btn2Background.top=btn1Background.top+200;
        btn2Background.right=(canvas.getWidth()/2)+margin+(textPaint.measureText(str_menu)/2)+10;
        btn2Background.bottom=btn1Background.bottom+200;

        canvas.drawRect(btn1Background,textPaint);
        canvas.drawRect(btn2Background,textPaint);

        textPaint.setColor(Color.WHITE);

        canvas.drawText(str_restart,(canvas.getWidth()/2)-(textPaint.measureText(str_restart)/2),((canvas.getHeight()-topwall.bottom)/2)+550,textPaint);
        canvas.drawText(str_menu,(canvas.getWidth()/2)-(textPaint.measureText(str_menu)/2),((canvas.getHeight()-topwall.bottom)/2)+750,textPaint);
    }

    private void paint(){
        paint_white =new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_white.setColor(Color.WHITE);
        paint_white.setStyle(Paint.Style.FILL);

        paint_grey =new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_grey.setColor(Color.parseColor("#808080"));
        paint_grey.setStyle(Paint.Style.FILL);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
