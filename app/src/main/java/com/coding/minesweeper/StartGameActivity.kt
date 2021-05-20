package com.coding.minesweeper

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StartGameActivity : AppCompatActivity(),OnCellClickListener {
    lateinit var time:TextView
    lateinit var grid:RecyclerView
    lateinit var mine:TextView
    lateinit var restart:Button
    lateinit var flag:TextView
    var numberOfRow:Int = 0
    var numberOfMines:Int=0
    var second=0
    private lateinit var mineSweeperGame: MineSweeperGame
    private lateinit var mineGridRecyclerAdapter: MineGridRecyclerAdapter
    var numberOfColumn:Int=0
    private var timerStarted = false
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        var second=0
        val difficultyLevel=intent.getStringExtra(Constants.DIFFICULTY);
        if (difficultyLevel!=null){
            setNumberOfRowsMinesColumn(difficultyLevel)
        }else{
            numberOfColumn=intent.getIntExtra(Constants.COLUMN,15)
            numberOfMines=intent.getIntExtra(Constants.MINES,15)
            numberOfRow=intent.getIntExtra(Constants.ROWS,15)
        }
        timerStarted = false
        setContentView(R.layout.activity_start_game2)
        time=findViewById(R.id.times)
        grid=findViewById(R.id.recycle_view)
        mine=findViewById(R.id.mines)
        mineSweeperGame = MineSweeperGame(numberOfColumn, numberOfMines)
        mine.text=(String.format("%03d", mineSweeperGame.numberBombs- mineSweeperGame.flagCount));
        mineGridRecyclerAdapter = MineGridRecyclerAdapter(mineSweeperGame.getMineGrid().getCells(), this)
        grid.layoutManager = GridLayoutManager(this, numberOfColumn)
        grid.adapter=mineGridRecyclerAdapter
        countDownTimer=object:CountDownTimer(Constants.TIMER_LENGTH,1000){
            override fun onFinish() {
                mineSweeperGame.outOfTime()
                Toast.makeText(this@StartGameActivity, "Game Over: Timer Expired", Toast.LENGTH_SHORT).show()
                mineSweeperGame.getMineGrid().revealAllBombs()
                mineGridRecyclerAdapter.setCells(mineSweeperGame.getMineGrid().getCells())
            }
            override fun onTick(millisUntilFinished: Long) {
                second+=1
                time.text= second.toString()
            }
        }

        restart=findViewById(R.id.restart)
        restart.setOnClickListener {
            mineSweeperGame = MineSweeperGame(numberOfColumn, numberOfMines)
            mineGridRecyclerAdapter.setCells(mineSweeperGame.getMineGrid().getCells())
            timerStarted = false
            countDownTimer.cancel()
            second = 0
            mineGridRecyclerAdapter.notifyDataSetChanged()
            time.text=getString(R.string.default_count)
            mine.text=(String.format("%03d", mineSweeperGame.numberBombs- mineSweeperGame.flagCount));
        }

        flag=findViewById(R.id.activity_main_flag)
        flag.setOnClickListener {
            mineSweeperGame.toggleMode()
            if (mineSweeperGame.isFlagMode) {
                val border = GradientDrawable()
                border.setColor(-0x1)
                border.setStroke(1, -0x1000000)
                flag.background = border
            } else {
                val border = GradientDrawable()
                border.setColor(-0x1)
                flag.background = border
            }
        }


    }

    //initializing the number of ros,column and mines
    private fun setNumberOfRowsMinesColumn(difficultyLevel: String) {
        when(difficultyLevel){
            Constants.EASY->{
                numberOfRow=8
                numberOfColumn=8
                numberOfMines= 10
            }
            Constants.MEDIUM->{
                numberOfRow=10
                numberOfColumn=10
                numberOfMines= 40
            }
            Constants.HARD->{
                numberOfRow=12
                numberOfColumn=12
                numberOfMines= 80
            }
        }
    }

    override fun cellClick(cell: Cell?) {
        mineSweeperGame.handleCellClick(cell!!)
        mine.text=(String.format("%03d", mineSweeperGame.numberBombs- mineSweeperGame.flagCount));

        if (!timerStarted) {
            countDownTimer.start()
            timerStarted = true
        }

        if (mineSweeperGame.isGameOver) {
            UserSharedPreferences.initializeSharedPreferencesForLastGamePlayed(this).edit().putLong(Constants.LAST_PLAYED,System.currentTimeMillis()).apply()
            countDownTimer.cancel()
            Toast.makeText(applicationContext, getString(R.string.over), Toast.LENGTH_SHORT).show()
            mineSweeperGame.getMineGrid().revealAllBombs()
        }

        if (mineSweeperGame.isGameWon) {
            val bestTime= UserSharedPreferences.initializeSharedPreferencesForBestTime(this).getLong(Constants.BEST_TIME,0L)
            UserSharedPreferences.initializeSharedPreferencesForLastGamePlayed(this).edit().putLong(
                Constants.LAST_PLAYED,System.currentTimeMillis()).apply()
            if(second>bestTime){
                UserSharedPreferences.initializeSharedPreferencesForBestTime(this).edit().putLong(Constants.BEST_TIME,
                    second.toLong()
                ).apply()
            }
            countDownTimer.cancel()
            Toast.makeText(applicationContext, getString(R.string.won), Toast.LENGTH_SHORT).show()
            mineSweeperGame.getMineGrid().revealAllBombs()
        }

        mineGridRecyclerAdapter.setCells(mineSweeperGame.getMineGrid().getCells())
    }

    override fun onBackPressed() {
        val newActivity =Intent(this,MainActivity::class.java)
        newActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK//clearing activity from stack
        startActivity(newActivity)
        finish()
    }






}