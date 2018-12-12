package com.lucas.floattextexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.lucas.fabtext.FabText
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_collapse.setOnClickListener {
            fabText.collapse()
        }

        btn_expand.setOnClickListener {
            fabText.expand()
        }

        btn_start_loading.setOnClickListener {
            fabText.startLoading()
        }

        btn_finish_loading.setOnClickListener {
            fabText.finishLoading()
        }

        fabText.onClickListener = {
            Toast.makeText(this, "iami", Toast.LENGTH_LONG).show()
        }
    }
}
