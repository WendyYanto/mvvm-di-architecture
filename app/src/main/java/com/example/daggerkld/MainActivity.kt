package com.example.daggerkld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.daggerkld.adapter.ProductListAdapter
import com.example.daggerkld.di.MyApplication
import com.example.daggerkld.viewmodel.MainViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var toastButton: Button
    private lateinit var productListRecycleView: RecyclerView
    private lateinit var productListAdapter: ProductListAdapter
    private lateinit var addProductTextView: TextView

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as MyApplication).applicationComponent.inject(this)
        this.init()
        viewModel.fetchData().observe(this, Observer {
            productListAdapter.notifyDataSetChanged()
        })
    }

    private fun init() {
        toastButton = findViewById(R.id.btn_show_toast)
        productListRecycleView = findViewById(R.id.rv_product_list)
        addProductTextView = findViewById(R.id.tv_add_product)

        toastButton.setOnClickListener {
            val value = addProductTextView.text.trim()
            if (value.isNotBlank()) {
                this.viewModel.addData(value.toString())
            }
            addProductTextView.text = null
        }

        productListRecycleView.layoutManager = LinearLayoutManager(this)

        productListAdapter = ProductListAdapter(
            this.viewModel.fetchData().value ?: ArrayList(),
            object : ProductListAdapter.ProductItemInterface {
                override fun click(index: Int) {
                    removeData(index)
                }
            })
        productListRecycleView.adapter = productListAdapter
    }

    private fun removeData(index: Int) {
        Toast.makeText(this, "Removing index $index", Toast.LENGTH_SHORT).show()
        this.viewModel.removeData(index)
    }

}
