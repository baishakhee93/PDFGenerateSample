package com.shakhee.pdfgenerateusingjson

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shakhee.pdfgeneratesample.R

class EmployeeListAdapter( private val  employeeList: List<EmployeeModel>): RecyclerView.Adapter<EmployeeListAdapter.ViewHolder>() {
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }


    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val EmployeeModel = employeeList.get(position)
        holder.userId.text = EmployeeModel.userId
        holder.name.text = EmployeeModel.name
        holder.department.text = EmployeeModel.department
        holder.role.text = EmployeeModel.role
        holder.designation.text = EmployeeModel.designation


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {

        return employeeList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val userId: TextView = itemView.findViewById(R.id.userId)
        val name: TextView = itemView.findViewById(R.id.name)
        val department: TextView = itemView.findViewById(R.id.department)
        val role: TextView = itemView.findViewById(R.id.role)
        val designation: TextView = itemView.findViewById(R.id.designation)
    }

}