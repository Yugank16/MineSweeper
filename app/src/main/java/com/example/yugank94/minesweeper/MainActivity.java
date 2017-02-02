package com.example.yugank94.minesweeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MyButton buttons[][];
    TextView scoreboard;
    LinearLayout rows[], mainlayout, board;
    boolean gameover, initialize;
    int num_open,diff=1;
    // game is won wheen all non mines are opened i.e num_open == row*col - mines;
    public final static int EMPTY=0;
    public boolean WIN=false;
    int row=9, col=9, mines=20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_layout);

        mainlayout= (LinearLayout) findViewById(R.id.mine_lay);
        scoreboard =(TextView) findViewById(R.id.score);
        setupboard();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.gamemenu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id= item.getItemId();

        switch(id)
        {
            case R.id.newgame:
            {
                resetboard();
                break;
            }

            case R.id.begin:
            {
                if(diff==1)
                    resetboard();
                else {
                    diff=1;
                    setupboard();
                }
                break;
            }

            case R.id.inter:
            {
                if(diff==2)
                    resetboard();
                else {
                    diff=2;
                    setupboard();
                }
                break;
            }

            case R.id.expert:
            {
                if(diff==3)
                    resetboard();
                else {
                    diff=3;
                    setupboard();
                }
                break;
            }
        }
        return true;
        }

    private void resetboard() {
        gameover= false;
        initialize= false;
        num_open=0;
        scoreboard.setText("");
        int i,j;
        for(i=0;i<row; i++)
        {
            for(j=0;j<col;j++)
            {

                buttons[i][j].setText("");
                buttons[i][j].setPadding(5,5,5,5);
                buttons[i][j].isMine= false;
                buttons[i][j].isOpen= false;
                buttons[i][j].count= EMPTY;
                buttons[i][j].isFirst= false;
//                buttons[i][j].setBackgroundColor(Color.TRANSPARENT);
                buttons[i][j].setBackgroundResource(R.drawable.dialog_bg);
            }
        }


    }


    private void setupboard() {
        int weight;
        gameover= false;
        initialize= false;
        num_open=0;
        mainlayout.removeView(board);
        scoreboard.setText("");
        board= new LinearLayout(this);
        if(diff==1) {
            weight = 3;
            row=9;
            col=9;
            mines=20;
        }
        else if(diff==2)
        {
            weight =4;
            row=13;
            col=13;
            mines=40;
        }
        else
        {
            weight =4;
            row=18;
            col=18;
            mines=99;
        }

        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,weight);
        params.setMargins(50,30,30,30);
        board.setLayoutParams(params);
        board.setBackgroundColor(getResources().getColor(R.color.board_bg));
        board.setOrientation(LinearLayout.VERTICAL);
        mainlayout.addView(board);
        int i,j;
        //for rows
        rows= new LinearLayout[row];
        buttons= new MyButton[row][col];
        for(i=0;i<row;i++)
        {
            rows[i] =new LinearLayout(this);
            LinearLayout.LayoutParams parameters= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1);
            rows[i].setLayoutParams(parameters);
            rows[i].setOrientation(LinearLayout.HORIZONTAL);
            board.addView(rows[i]);
        }


        for(i=0;i<row; i++)
        {
            for(j=0;j<col;j++)
            {
                buttons[i][j]= new MyButton(this);
                LinearLayout.LayoutParams parameters= new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,1);
                parameters.setMargins(2,2,2,2);
                buttons[i][j].setLayoutParams(parameters);
                buttons[i][j].setText("");
                buttons[i][j].setPadding(5,5,5,5);
                buttons[i][j].isMine= false;
                buttons[i][j].isOpen= false;
                buttons[i][j].count= EMPTY;
                buttons[i][j].isFirst= false;
//                buttons[i][j].setBackgroundColor(Color.TRANSPARENT);
                buttons[i][j].setBackgroundResource(R.drawable.dialog_bg);
                buttons[i][j].setOnClickListener(this);
                buttons[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        MyButton b= (MyButton)v;
                        b.setBackgroundResource(R.drawable.flag);
                        //b.setBackgroundColor(R.color.black);
                        return true;
                    }
                });
                rows[i].addView(buttons[i][j]);
            }
        }

    }



    @Override
    public void onClick(View v) {

        MyButton b= (MyButton) v;

        if(gameover || WIN)
            return;

        else if(!initialize)    //bcz first is always safe
        {
            initialize= true;
            b.isOpen= true;
            b.setBackgroundResource(R.drawable.texture);
            //b.isMine=true;
            b.isFirst= true;
            num_open++;
            plant_mines();  //after first we set up mines
            b.setText(b.count+"");
            scoreboard.setText(num_open*10+"");
            return;
        }

        else if(initialize)
        {
            if(b.isOpen)
            {
                Toast.makeText(this,"Already opened",Toast.LENGTH_SHORT).show();
                return;
            }


            else if(b.isMine)
            {
                Toast.makeText(this,"You Just Bombed a mine",Toast.LENGTH_LONG).show();
                gameover= true;
                WIN= false;
                for(int i=0 ;i< row; i++)
                    for(int j=0; j<col; j++)
                    {
                        if(buttons[i][j].isMine && !buttons[i][j].isOpen)
                        {
                            buttons[i][j].setBackgroundResource(R.drawable.mine);

                        }
                    }

                for(int i=0 ;i< row; i++)
                    for(int j=0; j<col; j++)
                    {
                        if(!buttons[i][j].isMine && !buttons[i][j].isOpen)
                        {
                            buttons[i][j].setText(buttons[i][j].count+" ");
                        }
                    }

                return;
            }

            else
            {
                b.isOpen= true;
                b.setBackgroundResource(R.drawable.texture);
                num_open++;
                b.setText(b.count+"");
                scoreboard.setText(num_open*10+"");
                if(isWin())
                {
                    WIN= true;
                    Toast.makeText(this,"Yipee!! U Won",Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private boolean isWin() {

        if(num_open == (row*col)-mines)
            return true;

        return false;
    }

    private void plant_mines() {

        Random r= new Random();
        int i,j,k=0,l;
        while(k<mines)
        {

            i= r.nextInt(row);
            j= r.nextInt(col);


            if(!buttons[i][j].isMine && !buttons[i][j].isFirst) {
                buttons[i][j].isMine = true;
                k++;  //increment in bombs
//            int arr[][]= new int[2][8];
                int arr[][] = {{-1, -1, -1, 0, 1, 1, 1, 0}, {-1, 0, 1, 1, 1, 0, -1, -1}};   //to access adjacent cells

                l = 0;
                while (l < 8) {
                    if ((i + arr[0][l] >= 0) && (i + arr[0][l] < row) && (j + arr[1][l] >= 0) && (j + arr[1][l] < col) && buttons[i + arr[0][l]][j + arr[1][l]].isMine == false) {
                        buttons[i + arr[0][l]][j + arr[1][l]].count ++;
                    }
                    l++;
                }

            }

        }


    }
}
