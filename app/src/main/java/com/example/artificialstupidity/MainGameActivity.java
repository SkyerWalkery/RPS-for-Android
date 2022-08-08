package com.example.artificialstupidity;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;


enum Result{
    WIN, DRAW, LOSE
}

interface Choice {
    Result compete(Choice against);
    Result eval(Rock against);
    Result eval(Paper against);
    Result eval(Scissors against);
    String getString(MainGameActivity activity);
}

class Rock implements Choice{
    @Override
    public Result compete(Choice against){
        return against.eval(this);
    }
    @Override
    public Result eval(Rock against){
        return Result.DRAW;
    }
    @Override
    public Result eval(Paper against){
        return Result.WIN;
    }
    @Override
    public Result eval(Scissors against){
        return Result.LOSE;
    }
    @Override
    public String getString(MainGameActivity activity) {
        return activity.getString(R.string.rock);
    }
}

class Paper implements Choice{
    @Override
    public Result compete(Choice against){
        return against.eval(this);
    }
    @Override
    public Result eval(Rock against){
        return Result.LOSE;
    }
    @Override
    public Result eval(Paper against){
        return Result.DRAW;
    }
    @Override
    public Result eval(Scissors against){
        return Result.WIN;
    }
    @Override
    public String getString(MainGameActivity activity) {
        return activity.getString(R.string.paper);
    }
}

class Scissors implements Choice{
    @Override
    public Result compete(Choice against){
        return against.eval(this);
    }
    @Override
    public Result eval(Rock against){
        return Result.WIN;
    }
    @Override
    public Result eval(Paper against){
        return Result.LOSE;
    }
    @Override
    public Result eval(Scissors against){
        return Result.DRAW;
    }
    @Override
    public String getString(MainGameActivity activity) {
        return activity.getString(R.string.scissors);
    }
}


public class MainGameActivity extends AppCompatActivity {

    private static final Random rand = new Random();
    private int total_rounds = 0;
    private int winning_cnt = 0;
    private int draw_cnt = 0;
    private int losing_cnt = 0;

    private ImageView ai_state_iv;
    private TextView game_info_tv;
    private ImageButton[] player_choose_btns;
    private Button new_round_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        ai_state_iv = (ImageView) findViewById(R.id.ai_state);
        game_info_tv = (TextView) findViewById(R.id.game_info);
        player_choose_btns = new ImageButton[]{
                (ImageButton) findViewById(R.id.player_choose_rock),
                (ImageButton) findViewById(R.id.player_choose_paper),
                (ImageButton) findViewById(R.id.player_choose_scissors)
        };
        new_round_btn = (Button) findViewById(R.id.new_round);
        // new round button is gone by default
        new_round_btn.setVisibility(View.GONE);

        updateTextInfo(null);
    }

    public void playerChoose(View view){
        // parse player's choose, according to which view user click on
        Choice player_choice;
        int visible_button_id;
        int view_id = view.getId();
        if(view_id == R.id.player_choose_rock) {
            player_choice = new Rock();
            visible_button_id = 0;
        }
        else if(view_id == R.id.player_choose_paper) {
            player_choice = new Paper();
            visible_button_id = 1;
        }
        else {
            player_choice = new Scissors();
            visible_button_id = 2;
        }

        // get ai's choice
        Choice ai_choice = getAINextChoice();
        Result res = player_choice.compete(ai_choice);
        handleResult(res);

        updateTextInfo(player_choice);
        updateAIInfo(ai_choice);
        for(ImageButton btn: player_choose_btns)
            btn.setVisibility(View.GONE);
        //player_choose_btns[visible_button_id].setVisibility(View.VISIBLE);
        new_round_btn.setVisibility(View.VISIBLE);
    }

    public void startNewRound(View view){
        new_round_btn.setVisibility(View.GONE);
        for(ImageButton btn: player_choose_btns)
            btn.setVisibility(View.VISIBLE);
        updateAIInfo(null);
    }

    private static Choice getAINextChoice(){
        switch (rand.nextInt(3)){
            case 0: return new Rock();
            case 1: return new Paper();
            default: return new Scissors();
        }
    }

    /*
    * handle result of a round
    * the result is from player's view
    * for example: Rock(player), Scissors(AI) -> Result.WIN
     */
    private void handleResult(Result result){
        switch (result){
            case WIN:    winning_cnt++;  break;
            case DRAW:   draw_cnt++;     break;
            case LOSE:   losing_cnt++;   break;
            default: break;
        }
        total_rounds++;
    }

    /*
    * update TextView of winning/losing/draw count...
     */
    private void updateTextInfo(Choice player_choice){
        @SuppressLint("StringFormatMatches")
        String game_info_text = getString(
                R.string.game_info_tv,
                winning_cnt,
                draw_cnt,
                losing_cnt,
                total_rounds == 0 ? 0.0 : (double)winning_cnt / total_rounds * 100,
                player_choice == null ? "" : player_choice.getString(this)
        );
        Spanned styledText = Html.fromHtml(game_info_text, FROM_HTML_MODE_LEGACY);
        game_info_tv.setText(styledText);
        // game_info_tv.setText(game_info_text);
    }

    private void updateAIInfo(Choice ai_choice){
        Resources res = getResources();
        Drawable drawable;

        if(ai_choice == null){
            drawable = ResourcesCompat.getDrawable(res, R.drawable.thinking, null);
            ai_state_iv.setImageDrawable(drawable);
            return;
        }

        Class<? extends Choice> ai_choice_class = ai_choice.getClass();
        int drawable_id;
        if(ai_choice_class == Rock.class)
            drawable_id = R.drawable.rock;
        else if(ai_choice_class == Paper.class)
            drawable_id = R.drawable.paper;
        else
            drawable_id = R.drawable.scissors;

        drawable = ResourcesCompat.getDrawable(res, drawable_id, null);
        ai_state_iv.setImageDrawable(drawable);
    }
}