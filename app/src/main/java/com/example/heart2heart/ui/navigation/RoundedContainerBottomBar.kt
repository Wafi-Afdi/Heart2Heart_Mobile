package com.example.heart2heart.ui.navigation

import androidx.compose.foundation.shape.GenericShape

fun menuBarShape() = GenericShape { size, _ ->
    reset()

    moveTo(0f, 0f)

    val width = 150f
    val height = 90f

    val point1 = 75f
    val point2 = 85f

    lineTo(size.width / 2 - width, 0f)

    cubicTo(
        size.width / 2 - point1, 0f,
        size.width / 2 - point2, height,
        size.width / 2, height
    )

    cubicTo(
        size.width / 2 + point2, height,
        size.width / 2 + point1, 0f,
        size.width / 2 + width, 0f
    )

    lineTo(size.width / 2 + width, 0f)

    lineTo(size.width, 0f)
    lineTo(size.width, size.height)
    lineTo(0f, size.height)

    close()
}