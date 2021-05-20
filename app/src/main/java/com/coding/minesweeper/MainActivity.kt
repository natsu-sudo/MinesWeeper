package com.coding.minesweeper

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private lateinit var lastGame:TextView
    lateinit var bestTime:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rows: EditText =findViewById(R.id.number_of_row)
        val column: EditText =findViewById(R.id.number_of_column)
        val mines: EditText =findViewById(R.id.number_of_mines)
        lastGame=findViewById(R.id.last_game)
        lastGame.text= lastGame()
        bestTime=findViewById(R.id.best_time)
        bestTime.text=bestTime()
        val radioGroup = findViewById<RadioGroup>(R.id.radio_group).apply {
            clearCheck()
            setOnCheckedChangeListener(object: RadioGroup.OnCheckedChangeListener{
                override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                    rows.visibility= View.GONE
                    column.visibility=View.GONE
                    mines.visibility=View.GONE
                }

            })
        }

        val startButton=findViewById<Button>(R.id.start)
        startButton.setOnClickListener {
            if(radioGroup.checkedRadioButtonId!=-1){//if any Radio button is selected ie East,Medium ,Hard start the Game
                val radioButton=findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
                val newActivity=Intent(this,StartGameActivity::class.java).apply {
                    putExtra(Constants.DIFFICULTY,radioButton.text.toString())

                }
                startActivity(newActivity)
            }else if (radioGroup.checkedRadioButtonId==-1 && !isEditTextVisible(rows,column,mines)){//if User tries to start the game without selecting
                //Radio button there should a Toast
                Toast.makeText(this,R.string.select_difficulty,Toast.LENGTH_SHORT).show()
            }else if(radioGroup.checkedRadioButtonId==-1 && isEditTextVisible(rows, column, mines)){

                    if (rows.text.isNotEmpty() && mines.text.isNotEmpty() && column.text.isNotEmpty()){//checking for text is not empty
                        if (rows.text.toString().toInt()>12 && column.text.toString().toInt()>12){//checking for row and Colmn should not be greater than 12
                            rows.error=getString(R.string.should_be_less_12)
                            column.error=getString(R.string.should_be_less_12)
                        } else if (rows.text.toString().toInt()!=column.text.toString().toInt()){//checking rows and colmn should be equal
                            rows.error=getString(R.string.rows_and_column)
                            column.error=getString(R.string.rows_and_column)
                        }else if (rows.text.toString().toInt()*column.text.toString().toInt()<mines.text.toString().toInt()){//checking mine should be less than grid
                            mines.error= String.format(getString(R.string.mine_should_be),rows.text.toString().toInt()*column.text.toString().toInt())

                        }else if(mines.text.toString().toInt()<=0 ){
                            mines.error=getString(R.string.more_than)
                        }else{
                            val newActivity:Intent=Intent(this,StartGameActivity::class.java).apply {
                                putExtra(Constants.COLUMN,column.text.toString().toInt())
                                putExtra(Constants.ROWS,rows.text.toString().toInt())
                                putExtra(Constants.MINES,mines.text.toString().toInt())
                            }
                            startActivity(newActivity)
                            newActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            finish()
                        }

                    }else{
                        Toast.makeText(this,R.string.please_enter,Toast.LENGTH_SHORT).show()
                    }
            }
        }

        val customBoard=findViewById<Button>(R.id.custom_board)
        customBoard.setOnClickListener {
            radioGroup.clearCheck()
            rows.visibility= View.VISIBLE
            column.visibility=View.VISIBLE
            mines.visibility=View.VISIBLE
        }
    }

    //this function make edit text visible if user want to create custom board
    private fun isEditTextVisible(
        rows: EditText,
        column: EditText,
        mines: EditText
    ) =rows.isVisible && column.isVisible && mines.isVisible

    //this function return the last game played in Seconds
    private fun lastGame(): String {
        val lastPlayed:Long =System.currentTimeMillis()-UserSharedPreferences.initializeSharedPreferencesForLastGamePlayed(this).getLong(Constants.LAST_PLAYED,System.currentTimeMillis())
        return "${(lastPlayed/1000)} s"
    }
    //this function return N/A if best time not available
    private fun bestTime(): String {
        val bestTime=UserSharedPreferences.initializeSharedPreferencesForBestTime(this).getLong(Constants.BEST_TIME,0L)
        if (bestTime==0L){
            return "N/A"
        }
        return "$bestTime s"
    }




}