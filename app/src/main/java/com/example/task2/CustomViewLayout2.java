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

public class CustomViewLayout2 extends SurfaceView implements Runnable {

    Thread thread=null;
    Boolean canDraw=false;
    Boolean flag;
    Boolean NoviceMode, NormalMode, NightmareMode,gameOverSoundEffectBool;
    Boolean powerUp1, powerUp2, powerUp3, powerUpInitialise;
    Context context;

    Canvas canvas;
    SurfaceHolder surfaceHolder;

    int circle_x,circle_y,x_direction,y_direction,circle_radius;
    int speedx,speedy;
    int randomx,randomy,randomComputer;
    int powerUp1x,powerUp1y, powerUp2x, powerUp2y, powerUp3x, powerUp3y;

    Random random;
    Paint paint_white, paint_grey,paint_powerup;

    Rect leftwall, rightwall, sliderUser, sliderComputer;
    int wallThickness, sliderThickness, sliderLengthUser, sliderLengthComputer;

    int userScore,computerScore;

    RectF btn1Background,btn2Background;
    Paint textPaint;

    MediaPlayer sliderSoundEffect,wallSoundEffect,gameOverSoundEffect;

    public CustomViewLayout2(Context context) {
        super(context);
        surfaceHolder=getHolder();
        canvas=new Canvas();
        this.context=context;
    }

    public CustomViewLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomViewLayout2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
            userSlider();
            computerSlider();

            collisionDetectionUser();
            collisionDetectionComputer();

            movementComputerSlider();

            powerUpInitialize();
            powerUpCollision();

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
        userScore=0;
        computerScore=0;
        Bundle transporter = ((Activity)getContext()).getIntent().getExtras();
        NoviceMode=transporter.getBoolean("NoviceMode");
        NormalMode=transporter.getBoolean("NormalMode");
        NightmareMode=transporter.getBoolean("NightmareMode");

        random=new Random();
        randomx=random.nextInt(2);
        if(randomx==0){
            x_direction=1;
        }
        else{
            x_direction=-1;
        }
        randomy=random.nextInt(2);
        if(randomy==0){
            y_direction=1;
        }
        else{
            y_direction=-1;
        }
        circle_radius=30;

        speedx=8+random.nextInt(5);
        speedy=8+random.nextInt(5);

        leftwall=new Rect();
        rightwall=new Rect();
        wallThickness=40;

        sliderUser=new Rect();
        sliderComputer=new Rect();
        sliderThickness=50;
        sliderLengthUser=250;
        sliderLengthComputer=250;

        flag=true;

        sliderSoundEffect=MediaPlayer.create(getContext(),R.raw.slider);
        wallSoundEffect=MediaPlayer.create(getContext(),R.raw.wall);
        gameOverSoundEffect=MediaPlayer.create(getContext(),R.raw.gameover);

        gameOverSoundEffectBool=true;

        powerUpInitialise=true;

    }

    private void paint(){
        paint_white =new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_white.setColor(Color.WHITE);
        paint_white.setStyle(Paint.Style.FILL);

        paint_grey =new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_grey.setColor(Color.parseColor("#808080"));
        paint_grey.setStyle(Paint.Style.FILL);

        paint_powerup=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_powerup.setColor(Color.WHITE);
        paint_powerup.setStyle(Paint.Style.FILL);
    }

    private void walls(){
        leftwall.left=0;
        leftwall.top=200;
        leftwall.right=leftwall.left+wallThickness;
        leftwall.bottom=canvas.getHeight();

        rightwall.left=canvas.getWidth()-wallThickness;
        rightwall.top=200;
        rightwall.right=canvas.getWidth();
        rightwall.bottom=canvas.getHeight();

        canvas.drawRect(leftwall,paint_grey);
        canvas.drawRect(rightwall,paint_grey);
    }

    private void movementCircle(){

        if(userScore==3 || computerScore==3){
            endGame();
        }
        else {
            if (circle_x == 0 && circle_y == 0) {
                circle_x = canvas.getWidth() / 2;
                circle_y = (canvas.getHeight() + 200) / 2;
            }
            if (circle_y - circle_radius >= canvas.getHeight()) {
                computerScore++;
                rebound();
            } else if (circle_y + circle_radius <= 200) {
                userScore++;
                rebound();
            } else {
                if (circle_x + circle_radius >= canvas.getWidth() - wallThickness) {
                    x_direction = -1;
                    wallSoundEffect.start();
                }
                if (circle_x - circle_radius <= wallThickness) {
                    x_direction = 1;
                    wallSoundEffect.start();
                }
                circle_x = circle_x + (x_direction * speedx);
                circle_y = circle_y + (y_direction * speedy);

                canvas.drawCircle(circle_x, circle_y, circle_radius, paint_white);
            }
        }
    }

    private void userSlider(){
        if(sliderUser.left==0 || sliderUser.top==0) {
            sliderUser.left = (canvas.getWidth() - sliderLengthUser) / 2;
            sliderUser.top = canvas.getHeight() - sliderThickness;
            sliderUser.right = sliderUser.left + sliderLengthUser;
            sliderUser.bottom = sliderUser.top + sliderThickness;
        }
        canvas.drawRect(sliderUser, paint_grey);
    }

    private void computerSlider(){
        if(sliderComputer.left==0 || sliderComputer.top==0) {
            sliderComputer.left = (canvas.getWidth() - sliderLengthComputer) / 2;
            sliderComputer.top = 200;
            sliderComputer.right = sliderComputer.left + sliderLengthComputer;
            sliderComputer.bottom = sliderComputer.top + sliderThickness;
        }
        canvas.drawRect(sliderComputer, paint_grey);
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
                        Intent Restart= new Intent(context, CustomViewActivity2.class);
                        Restart.putExtra("NoviceMode", NoviceMode);
                        Restart.putExtra("NormalMode", NormalMode);
                        Restart.putExtra("NightmareMode", NightmareMode);
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

                if(x-(sliderLengthUser/2)<=leftwall.right || x+(sliderLengthUser/2)>=rightwall.left){
                    return true;
                }
                else if(x>=sliderUser.left && x<=sliderUser.right){
                    if(flag) {
                        sliderUser.left = (int) x - (sliderLengthUser / 2);
                        sliderUser.right = (int) x + (sliderLengthUser / 2);
                    }
                    return true;
                }
                return value;
            }
        }
        return value;
    }

    private void collisionDetectionUser(){
        if(circle_x+circle_radius>=sliderUser.left && circle_x-circle_radius<=sliderUser.right && circle_y+circle_radius>=sliderUser.top && circle_y+circle_radius<=sliderUser.bottom){
            y_direction=-1;
            sliderSoundEffect.start();
            if(NormalMode || NightmareMode){
                speedx=speedx+1+random.nextInt(2);
                speedy=speedy+1+random.nextInt(2);
            }
        }
        if(circle_x+circle_radius>=sliderUser.left && circle_x+circle_radius<=sliderUser.left+(sliderLengthUser/2) && circle_y>=sliderUser.top){
            x_direction=-1;
            sliderSoundEffect.start();
        }
        if(circle_x-circle_radius<=sliderUser.right && circle_x-circle_radius>=sliderUser.left+(sliderLengthUser/2) && circle_y>=sliderUser.top){
            x_direction=1;
            sliderSoundEffect.start();
        }
    }

    private void collisionDetectionComputer(){
        if(circle_x+circle_radius>=sliderComputer.left && circle_x-circle_radius<=sliderComputer.right && circle_y-circle_radius>=sliderComputer.top && circle_y-circle_radius<=sliderComputer.bottom){
            y_direction=1;
            sliderSoundEffect.start();
            if(NormalMode || NightmareMode){
                speedx=speedx+1+random.nextInt(2);
                speedy=speedy+1+random.nextInt(2);
            }
        }
        if(circle_x+circle_radius>=sliderComputer.left && circle_x+circle_radius<=sliderComputer.left+(sliderLengthComputer/2) && circle_y<=sliderComputer.bottom){
            x_direction=-1;
            sliderSoundEffect.start();
        }
        if(circle_x-circle_radius<=sliderComputer.right && circle_x-circle_radius>=sliderComputer.left-(sliderLengthComputer/2) && circle_y<=sliderComputer.bottom){
            x_direction=1;
            sliderSoundEffect.start();
        }

    }

    private void movementComputerSlider(){
        if(circle_y<=(canvas.getHeight()+200)/2 && circle_x-(sliderLengthComputer/2)>=leftwall.right && circle_x+(sliderLengthComputer/2)<=rightwall.left && y_direction==-1){
            if(NoviceMode){
                randomComputer=random.nextInt(15);
                if(randomComputer==0){
                    sliderComputer.left = circle_x - (sliderLengthComputer / 2);
                    sliderComputer.right = circle_x + (sliderLengthComputer / 2);
                }
                else{

                }
            }
            else if(NormalMode){
                randomComputer=random.nextInt(3);
                if(randomComputer==0){
                    sliderComputer.left = circle_x - (sliderLengthComputer / 2);
                    sliderComputer.right = circle_x + (sliderLengthComputer / 2);
                }
                else{

                }
            }
            else{
                sliderComputer.left = circle_x - (sliderLengthComputer / 2);
                sliderComputer.right = circle_x + (sliderLengthComputer / 2);
            }
        }
    }

    private void powerUpInitialize(){
        if(powerUpInitialise) {
            powerUp1 = true;
            powerUp2 = true;
            powerUp3=true;

            powerUp1x = random.nextInt(canvas.getWidth() / 2) + (canvas.getWidth() / 4);
            powerUp1y = random.nextInt(canvas.getHeight() / 2) + (canvas.getHeight() / 4);

            powerUp2x = random.nextInt(canvas.getWidth() / 2) + (canvas.getWidth() / 4);
            powerUp2y = random.nextInt(canvas.getHeight() / 2) + (canvas.getHeight() / 4);

            powerUp3x = random.nextInt(canvas.getWidth() / 2) + (canvas.getWidth() / 4);
            powerUp3y = random.nextInt(canvas.getHeight() / 2) + (canvas.getHeight() / 4);

            paint_powerup.setTextSize(60);
            powerUpInitialise=false;
        }
    }

    private void powerUpCollision(){
        if(powerUp1){
            canvas.drawText("*",powerUp1x,powerUp1y,paint_powerup);
            if(circle_x+circle_radius>=powerUp1x-10 && circle_x-circle_radius<=powerUp1x+10 && circle_y+circle_radius>=powerUp1y-10 && circle_y-circle_radius<=powerUp1y+10 && y_direction==-1){
                sliderLengthUser=2*sliderLengthUser;
                powerUp1=false;
            }
        }
        if(powerUp2) {
            canvas.drawText("*", powerUp2x, powerUp2y, paint_powerup);
            if (circle_x + circle_radius >= powerUp2x - 10 && circle_x - circle_radius <= powerUp2x + 10 && circle_y + circle_radius >= powerUp2y - 10 && circle_y - circle_radius <= powerUp2y + 10 && y_direction == -1) {
                circle_radius = circle_radius+(circle_radius/2);
                powerUp2 = false;
            }
        }
        if(powerUp3){
            canvas.drawText("*",powerUp3x,powerUp3y,paint_powerup);
            if(circle_x+circle_radius>=powerUp3x-10 && circle_x-circle_radius<=powerUp3x+10 && circle_y+circle_radius>=powerUp3y-10 && circle_y-circle_radius<=powerUp3y+10 && y_direction==-1){
                sliderLengthComputer=sliderLengthComputer/2;
                powerUp3=false;
            }
        }
    }

    private void score(){

        paint_white.setTextAlign(Paint.Align.CENTER);
        paint_white.setTextSize(60);

        canvas.drawText("User: "+userScore,210,120,paint_white);
        canvas.drawText("Computer: "+computerScore,canvas.getWidth()-250,120,paint_white);
    }

    private void rebound(){
        circle_x = canvas.getWidth() / 2;
        circle_y = (canvas.getHeight() + 200) / 2;

        randomx=random.nextInt(2);
        if(randomx==0){
            x_direction=1;
        }
        else{
            x_direction=-1;
        }
        randomy=random.nextInt(2);
        if(randomy==0){
            y_direction=1;
        }
        else{
            y_direction=-1;
        }
        speedx=8+random.nextInt(5);
        speedy=8+random.nextInt(5);

        sliderLengthUser=250;
        circle_radius=30;
        sliderLengthComputer=250;

        powerUpInitialise=true;
        powerUpInitialize();
    }

    private void endGame(){
        paint_white.setTextAlign(Paint.Align.CENTER);
        paint_white.setTextSize(120);
        if(userScore==3) {
            canvas.drawText("YOU WON", canvas.getWidth() / 2, canvas.getHeight() / 2, paint_white);
        }
        else{
            canvas.drawText("YOU LOST", canvas.getWidth() / 2, canvas.getHeight() / 2, paint_white);
        }
        flag=false;
        buttons();

        if(gameOverSoundEffectBool) {
            gameOverSoundEffect.start();
            gameOverSoundEffectBool=false;
        }
        powerUp1=false;
        powerUp2=false;
        powerUp3=false;
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
        btn1Background.top=(canvas.getHeight()-(200+wallThickness))/2+fm.top-margin+550+20;
        btn1Background.right=(canvas.getWidth()/2)+margin+(textPaint.measureText(str_restart)/2)+10;
        btn1Background.bottom=((canvas.getHeight()-(200+wallThickness))/2)+fm.bottom+margin+550-10;

        btn2Background.left=(canvas.getWidth()/2)-margin-(textPaint.measureText(str_menu)/2)-10;
        btn2Background.top=btn1Background.top+200;
        btn2Background.right=(canvas.getWidth()/2)+margin+(textPaint.measureText(str_menu)/2)+10;
        btn2Background.bottom=btn1Background.bottom+200;

        canvas.drawRect(btn1Background,textPaint);
        canvas.drawRect(btn2Background,textPaint);

        textPaint.setColor(Color.WHITE);

        canvas.drawText(str_restart,(canvas.getWidth()/2)-(textPaint.measureText(str_restart)/2),((canvas.getHeight()-(200+wallThickness))/2)+550,textPaint);
        canvas.drawText(str_menu,(canvas.getWidth()/2)-(textPaint.measureText(str_menu)/2),((canvas.getHeight()-(200+wallThickness))/2)+750,textPaint);
    }

}
