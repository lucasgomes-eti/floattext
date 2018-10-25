package com.lucas.fabtext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.transition.TransitionManager
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.widget.ImageViewCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

open class FabText : LinearLayout {

    private var fabTextView: TextView? = null
    private var fabImageView: ImageView? = null
    private var fabContainer: LinearLayout? = null

    private var textValue: String? = null
    private var imageDrawable: Int? = null
    private var textColorValue: Int? = null
    private var imageTint: Int? = null
    private var backgroundColor: String? = null

    constructor(context: Context) : super(context) {
        initializeView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        getValues(context, attrs)
        initializeView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        getValues(context, attrs)
        initializeView(context)
    }

    private fun initializeView(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.fab_text, this)
    }

    private fun getValues(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FabText)
        textValue = typedArray.getString(R.styleable.FabText_text)
        imageDrawable = typedArray.getResourceId(R.styleable.FabText_icon, R.drawable.ic_android_black_24dp)
        textColorValue = typedArray.getColor(R.styleable.FabText_text_color, ContextCompat.getColor(context, android.R.color.black))
        imageTint = typedArray.getColor(R.styleable.FabText_icon_color, ContextCompat.getColor(context, android.R.color.black))
        backgroundColor = typedArray.getString(R.styleable.FabText_color)
        typedArray.recycle()
    }

    fun Context.createVectorCompatDrawable(drawableId: Int) =
        DrawableCompat.wrap(VectorDrawableCompat.create(this.resources, drawableId, this.theme) as Drawable)

    override fun onFinishInflate() {
        super.onFinishInflate()

        fabTextView = findViewById(R.id.text_fab)
        fabImageView = findViewById(R.id.image_fab)
        fabContainer = findViewById(R.id.fab_container)

        fabTextView?.text = textValue
        fabTextView?.setTextColor(textColorValue!!)



        fabImageView?.setImageDrawable(context.createVectorCompatDrawable(imageDrawable!!))

        ImageViewCompat.setImageTintList(fabImageView!!, ColorStateList.valueOf(imageTint!!))

        fabContainer?.background?.setColorFilter(Color.parseColor(if (backgroundColor.isNullOrEmpty()) {
            "#ffffff"
        } else {
            backgroundColor
        }), PorterDuff.Mode.SRC_ATOP)

        if (textValue.isNullOrEmpty()) {

            fabTextView?.visibility = GONE

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                fabContainer?.background = ContextCompat.getDrawable(context, R.drawable.fab_bg)
            } else {
                @Suppress("DEPRECATION")
                fabContainer?.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.fab_bg))
            }

            fabImageView?.setPadding(8, 0, 0, 0)
        }
    }

    fun collapse() {
        TransitionManager.beginDelayedTransition(this)
        fabTextView?.visibility = GONE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            fabContainer?.background = ContextCompat.getDrawable(context, R.drawable.fab_bg)
        } else {
            @Suppress("DEPRECATION")
            fabContainer?.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.fab_bg))
        }

        fabImageView?.setPadding(8, 0, 0, 0)
    }

    fun expand() {
        if (textValue?.isNotEmpty() == true) {
            TransitionManager.beginDelayedTransition(this)
            fabTextView?.visibility = VISIBLE

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                fabContainer?.background = ContextCompat.getDrawable(context, R.drawable.fab_text_bg)
            } else {
                @Suppress("DEPRECATION")
                fabContainer?.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.fab_text_bg))
            }

            fabImageView?.setPadding(0, 0, 0, 0)
        }
    }
}