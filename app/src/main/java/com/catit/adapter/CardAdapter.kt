package com.catit.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.catit.R
import com.catit.model.Card
import com.catit.sql.SQLHandler
import com.catit.view.FormActivity
import com.catit.view.HomeActivity
import com.catit.view.ViewActivity

class CardAdapter(private val context: Context) : RecyclerView.Adapter<CardAdapter.Holder>() {

  private val data = ArrayList<Card>()

  override fun onCreateViewHolder(parent: ViewGroup, p1: Int): Holder {
    val layout = LayoutInflater.from(parent.context).inflate(R.layout.card_model, parent, false)
    return Holder(layout)
  }

  override fun onBindViewHolder(holder: Holder, position: Int) {
    holder.title.text = data[position].title
    holder.description.text = data[position].description
    holder.date.text = data[position].date
    holder.deleteButton.setOnClickListener {
      val builder = AlertDialog.Builder(context)
      builder.setTitle("Delete")
      builder.setTitle("Are you sure you want to delete this card?")
      builder.setPositiveButton("Yes") { _, _ ->
        val id = data[position].id
        val sql = SQLHandler(context)
        sql.delete(id)
        data.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, data.size)
      }
      builder.setNegativeButton("No") { _, _ ->
        // Do nothing
      }
      builder.show()
    }
    holder.viewButton.setOnClickListener {
      val id = data[position].id
      val intent = Intent(context, ViewActivity::class.java)
      intent.putExtra("id", id)
      context.startActivity(intent)
    }
    holder.editButton.setOnClickListener {
      val id = data[position].id
      val intent = Intent(context, FormActivity::class.java)
      intent.putExtra("id", id)
      intent.putExtra("isAdd", false)
      context.startActivity(intent)
    }
  }

  override fun getItemCount() = data.size

  fun addItem(item: Card) {
    data.add(0, item)

    this.notifyDataSetChanged()
    this.notifyItemRangeInserted(0, data.size)
  }

  fun clear() {
    data.clear()
    this.notifyDataSetChanged()
    this.notifyItemRangeInserted(0, data.size)
  }

  class Holder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.findViewById(R.id.textViewTitle)
    val description: TextView = view.findViewById(R.id.textViewDesc)
    val date: TextView = view.findViewById(R.id.textViewDate)
    val deleteButton: Button = view.findViewById(R.id.buttonDelete)
    val viewButton: Button = view.findViewById(R.id.buttonView)
    val editButton: Button = view.findViewById(R.id.buttonEdit)
  }

}