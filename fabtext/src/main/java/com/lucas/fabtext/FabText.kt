package com.lucas.fabtext

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator.REVERSE
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.transition.TransitionManager
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ImageViewCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
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

    var onClickListener: (View) -> Unit = {}

    val animatorSet = AnimatorSet()

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

        ViewCompat.setElevation(this, context.resources.getDimension(R.dimen.fab_rest_elevation))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            stateListAnimator = AnimatorInflater.loadStateListAnimator(context, R.anim.fab_state_list_anim)
            isFocusable = true
            isClickable = true
        }

        setBackgroundResource(R.drawable.fab_text_bg)

        fabImageView?.setImageDrawable(context.createVectorCompatDrawable(imageDrawable!!))

        ImageViewCompat.setImageTintList(fabImageView!!, ColorStateList.valueOf(imageTint!!))

        fabContainer?.background?.setColorFilter(Color.parseColor(if (backgroundColor.isNullOrEmpty()) {
            "#ffffff"
        } else {
            backgroundColor
        }), PorterDuff.Mode.SRC_ATOP)

        if (textValue.isNullOrEmpty()) {

            fabTextView?.visibility = GONE

            fabContainer?.setBackgroundResource(R.drawable.fab_bg)

            fabContainer?.background?.setColorFilter(Color.parseColor(if (backgroundColor.isNullOrEmpty()) {
                "#ffffff"
            } else {
                backgroundColor
            }), PorterDuff.Mode.SRC_ATOP)
        }

        fabContainer?.setOnClickListener {
            try {
                onClickListener.invoke(it)
            } catch (e: UninitializedPropertyAccessException) {
                e.printStackTrace()
            }
        }
    }

    fun collapse() {
        TransitionManager.beginDelayedTransition(this)
        fabTextView?.visibility = GONE

        Handler().postDelayed({
            TransitionManager.beginDelayedTransition(this)
            fabContainer?.setBackgroundResource(R.drawable.fab_bg)

            fabContainer?.background?.setColorFilter(Color.parseColor(if (backgroundColor.isNullOrEmpty()) {
                "#ffffff"
            } else {
                backgroundColor
            }), PorterDuff.Mode.SRC_ATOP)
        }, 200)

    }

    fun expand() {
        if (textValue?.isNotEmpty() == true) {
            TransitionManager.beginDelayedTransition(this)
            fabTextView?.visibility = VISIBLE

            fabContainer?.setBackgroundResource(R.drawable.fab_text_bg)

            fabContainer?.background?.setColorFilter(Color.parseColor(if (backgroundColor.isNullOrEmpty()) {
                "#ffffff"
            } else {
                backgroundColor
            }), PorterDuff.Mode.SRC_ATOP)
        }
    }

    fun startLoading() {
        fabImageView?.setImageResource(R.drawable.dual_ring)
        fabContainer?.isEnabled = false

        val rotationAnimator = ObjectAnimator
            .ofFloat(fabImageView, View.ROTATION, 360f)
            .setDuration(1000)

        rotationAnimator.repeatCount = ObjectAnimator.INFINITE
        rotationAnimator.repeatMode = ObjectAnimator.RESTART/REVERSE

        animatorSet.play(rotationAnimator)
        animatorSet.start()
    }

    fun finishLoading() {
        fabImageView?.setImageResource(imageDrawable!!)
        fabContainer?.isEnabled = true
        animatorSet.end()
    }
}