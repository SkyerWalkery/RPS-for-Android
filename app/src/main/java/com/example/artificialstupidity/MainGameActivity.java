package com.example.artificialstupidity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;


enum Result{
    WIN, DRAW, LOSE;
}

interface Choice {
    Result compete(Choice against);
    Result eval(Rock against);
    Result eval(Paper against);
    Result eval(Scissors against);
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
    @NonNull
    @Override
    public String toString() {return "Rock";}
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
    @NonNull
    @Override
    public String toString() {return "Paper";}
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
    @NonNull
    @Override
    public String toString() {return "Scissors";}
}


public class MainGameActivity extends AppCompatActivity {

    private static final Random rand = new Random();
    private int total_rounds = 0;
    private int winning_cnt = 0;

    private TextView ai_state_tv;
    private TextView winning_cnt_tv;
    private TextView winning_rate_tv;
    private Button[] player_choose_btns;
    private Button new_round_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        ai_state_tv = (TextView) findViewById(R.id.ai_state);
        winning_cnt_tv = (TextView) findViewById(R.id.winning_cnt);
        winning_rate_tv = (TextView) findViewById(R.id.winning_rate);
        player_choose_btns = new Button[]{
                (Button) findViewById(R.id.player_choose_rock),
                (Button) findViewById(R.id.player_choose_paper),
                (Button) findViewById(R.id.player_choose_scissors)
        };
        new_round_btn = (Button) findViewById(R.id.new_round);
        // new round button is gone by default
        new_round_btn.setVisibility(View.GONE);

        String init_succ_cnt_text = getString(
                R.string.winning_cnt_tv,
                0
        );
        String init_succ_rate_text = getString(
                R.string.winning_rate_tv,
                0.0
        );
        winning_cnt_tv.setText(init_succ_cnt_text);
        winning_rate_tv.setText(init_succ_rate_text);
    }

    public void playerChoose(View view){
        // parse player's choose, according to which view user click on
        Choice player_choice;
        int view_id = view.getId();
        if(view_id == R.id.player_choose_paper)
            player_choice = new Paper();
        else if(view_id == R.id.player_choose_rock)
            player_choice = new Rock();
        else
            player_choice =new Scissors();

        // get ai's choice
        Choice ai_choice = getAINextChoice();
        String new_ai_text = "AI: " + ai_choice;
        ai_state_tv.setText(new_ai_text);
        Result res = player_choice.compete(ai_choice);
        handleResult(res);
    }

    public void startNewRound(View view){
        new_round_btn.setVisibility(View.GONE);
        for(Button btn: player_choose_btns)
            btn.setVisibility(View.VISIBLE);
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
        if(result == Result.WIN)
            ++winning_cnt;
        ++total_rounds;
        String new_succ_cnt_text = getString(
                R.string.winning_cnt_tv,
                winning_cnt
        );
        String new_succ_rate_text = getString(
                R.string.winning_rate_tv,
                (double)winning_cnt / total_rounds * 100
        );
        winning_cnt_tv.setText(new_succ_cnt_text);
        winning_rate_tv.setText(new_succ_rate_text);

        for(Button btn: player_choose_btns)
            btn.setVisibility(View.GONE);
        new_round_btn.setVisibility(View.VISIBLE);
    }
}