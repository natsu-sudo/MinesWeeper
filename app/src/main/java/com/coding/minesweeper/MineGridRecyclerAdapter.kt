package com.coding.minesweeper

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class MineGridRecyclerAdapter(private var cells: List<Cell>, private val listener: OnCellClickListener) :
    RecyclerView.Adapter<MineGridRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_cell, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cells[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemId(position: Int): Long {
        return cells[position].hashCode().toLong()
    }

    override fun getItemCount(): Int {
        return cells.size
    }
    //Setting the List or cell grid again if user start the game again
    fun setCells(cells: List<Cell>) {
        this.cells = cells
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var valueTextView: TextView = itemView.findViewById(R.id.item_cell_value)
        fun bind(cell: Cell) {
            itemView.setBackgroundColor(Color.GRAY)
            itemView.setOnClickListener {
                listener.cellClick(cell) }
            if (cell.isRevealed) {
                when (cell.value) {
                    Constants.BOMB -> {
                        valueTextView.setText(R.string.bomb)
                    }
                    Constants.BLANK -> {
                        valueTextView.text = ""
                        itemView.setBackgroundColor(Color.WHITE)
                    }
                    else -> {
                        when (cell.value) {
                            1 -> {
                                valueTextView.setTextColor(Color.BLUE)
                            }
                            2 -> {
                                valueTextView.setTextColor(Color.GREEN)
                            }
                            3 -> {
                                valueTextView.setTextColor(Color.RED)
                            }
                        }
                        valueTextView.setText(cell.value.toString())
                    }
                }
            } else if (cell.isFlagged) {
                valueTextView.setText(R.string.flag)
            }
        }

    }

}
