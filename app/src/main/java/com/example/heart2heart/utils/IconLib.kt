package com.example.heart2heart.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AlignEndHorizontal: ImageVector
    get() {
        if (_AlignEndHorizontal != null) return _AlignEndHorizontal!!

        _AlignEndHorizontal = ImageVector.Builder(
            name = "AlignEndHorizontal",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6f, 2f)
                horizontalLineTo(8f)
                arcTo(2f, 2f, 0f, false, true, 10f, 4f)
                verticalLineTo(16f)
                arcTo(2f, 2f, 0f, false, true, 8f, 18f)
                horizontalLineTo(6f)
                arcTo(2f, 2f, 0f, false, true, 4f, 16f)
                verticalLineTo(4f)
                arcTo(2f, 2f, 0f, false, true, 6f, 2f)
                close()
            }
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(16f, 9f)
                horizontalLineTo(18f)
                arcTo(2f, 2f, 0f, false, true, 20f, 11f)
                verticalLineTo(16f)
                arcTo(2f, 2f, 0f, false, true, 18f, 18f)
                horizontalLineTo(16f)
                arcTo(2f, 2f, 0f, false, true, 14f, 16f)
                verticalLineTo(11f)
                arcTo(2f, 2f, 0f, false, true, 16f, 9f)
                close()
            }
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(22f, 22f)
                horizontalLineTo(2f)
            }
        }.build()

        return _AlignEndHorizontal!!
    }

val Location_on: ImageVector
    get() {
        if (_Location_on != null) return _Location_on!!

        _Location_on = ImageVector.Builder(
            name = "Location_on",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                    fill = SolidColor(Color(0xFF000000))
            ) {
                moveTo(480f, 480f)
                quadToRelative(33f, 0f, 56.5f, -23.5f)
                reflectiveQuadTo(560f, 400f)
                reflectiveQuadToRelative(-23.5f, -56.5f)
                reflectiveQuadTo(480f, 320f)
                reflectiveQuadToRelative(-56.5f, 23.5f)
                reflectiveQuadTo(400f, 400f)
                reflectiveQuadToRelative(23.5f, 56.5f)
                reflectiveQuadTo(480f, 480f)
                moveToRelative(0f, 294f)
                quadToRelative(122f, -112f, 181f, -203.5f)
                reflectiveQuadTo(720f, 408f)
                quadToRelative(0f, -109f, -69.5f, -178.5f)
                reflectiveQuadTo(480f, 160f)
                reflectiveQuadToRelative(-170.5f, 69.5f)
                reflectiveQuadTo(240f, 408f)
                quadToRelative(0f, 71f, 59f, 162.5f)
                reflectiveQuadTo(480f, 774f)
                moveToRelative(0f, 106f)
                quadTo(319f, 743f, 239.5f, 625.5f)
                reflectiveQuadTo(160f, 408f)
                quadToRelative(0f, -150f, 96.5f, -239f)
                reflectiveQuadTo(480f, 80f)
                reflectiveQuadToRelative(223.5f, 89f)
                reflectiveQuadTo(800f, 408f)
                quadToRelative(0f, 100f, -79.5f, 217.5f)
                reflectiveQuadTo(480f, 880f)
                moveToRelative(0f, -480f)
            }
        }.build()

        return _Location_on!!
    }

val Location_off: ImageVector
    get() {
        if (_Location_off != null) return _Location_off!!

        _Location_off = ImageVector.Builder(
            name = "Location_off",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000))
            ) {
                moveTo(560f, 400f)
                quadToRelative(0f, -33f, -23.5f, -56.5f)
                reflectiveQuadTo(480f, 320f)
                quadToRelative(-10f, 0f, -19f, 2f)
                reflectiveQuadToRelative(-17f, 7f)
                lineToRelative(107f, 107f)
                quadToRelative(5f, -8f, 7f, -17f)
                reflectiveQuadToRelative(2f, -19f)
                moveToRelative(168f, 213f)
                lineToRelative(-58f, -58f)
                quadToRelative(25f, -42f, 37.5f, -78.5f)
                reflectiveQuadTo(720f, 408f)
                quadToRelative(0f, -109f, -69.5f, -178.5f)
                reflectiveQuadTo(480f, 160f)
                quadToRelative(-44f, 0f, -82.5f, 13.5f)
                reflectiveQuadTo(328f, 213f)
                lineToRelative(-57f, -57f)
                quadToRelative(43f, -37f, 97f, -56.5f)
                reflectiveQuadTo(480f, 80f)
                quadToRelative(127f, 0f, 223.5f, 89f)
                reflectiveQuadTo(800f, 408f)
                quadToRelative(0f, 48f, -18f, 98.5f)
                reflectiveQuadTo(728f, 613f)
                moveToRelative(-157f, 71f)
                lineTo(244f, 357f)
                quadToRelative(-2f, 12f, -3f, 25f)
                reflectiveQuadToRelative(-1f, 26f)
                quadToRelative(0f, 71f, 59f, 162.5f)
                reflectiveQuadTo(480f, 774f)
                quadToRelative(26f, -23f, 48.5f, -45.5f)
                reflectiveQuadTo(571f, 684f)
                moveTo(819f, 932f)
                lineTo(627f, 740f)
                quadToRelative(-32f, 34f, -68f, 69f)
                reflectiveQuadToRelative(-79f, 71f)
                quadTo(319f, 743f, 239.5f, 625.5f)
                reflectiveQuadTo(160f, 408f)
                quadToRelative(0f, -32f, 5f, -61f)
                reflectiveQuadToRelative(14f, -55f)
                lineTo(27f, 140f)
                lineToRelative(57f, -57f)
                lineTo(876f, 875f)
                close()
                moveTo(499f, 384f)
            }
        }.build()

        return _Location_off!!
    }

val CpuChip: ImageVector
    get() {
        if (_CpuChip != null) return _CpuChip!!

        _CpuChip = ImageVector.Builder(
            name = "CpuChip",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF0F172A)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(8.25f, 3f)
                verticalLineTo(4.5f)
                moveTo(4.5f, 8.25f)
                horizontalLineTo(3f)
                moveTo(21f, 8.25f)
                horizontalLineTo(19.5f)
                moveTo(4.5f, 12f)
                horizontalLineTo(3f)
                moveTo(21f, 12f)
                horizontalLineTo(19.5f)
                moveTo(4.5f, 15.75f)
                horizontalLineTo(3f)
                moveTo(21f, 15.75f)
                horizontalLineTo(19.5f)
                moveTo(8.25f, 19.5f)
                verticalLineTo(21f)
                moveTo(12f, 3f)
                verticalLineTo(4.5f)
                moveTo(12f, 19.5f)
                verticalLineTo(21f)
                moveTo(15.75f, 3f)
                verticalLineTo(4.5f)
                moveTo(15.75f, 19.5f)
                verticalLineTo(21f)
                moveTo(6.75f, 19.5f)
                horizontalLineTo(17.25f)
                curveTo(18.4926f, 19.5f, 19.5f, 18.4926f, 19.5f, 17.25f)
                verticalLineTo(6.75f)
                curveTo(19.5f, 5.50736f, 18.4926f, 4.5f, 17.25f, 4.5f)
                horizontalLineTo(6.75f)
                curveTo(5.50736f, 4.5f, 4.5f, 5.50736f, 4.5f, 6.75f)
                verticalLineTo(17.25f)
                curveTo(4.5f, 18.4926f, 5.50736f, 19.5f, 6.75f, 19.5f)
                close()
                moveTo(7.5f, 7.5f)
                horizontalLineTo(16.5f)
                verticalLineTo(16.5f)
                horizontalLineTo(7.5f)
                verticalLineTo(7.5f)
                close()
            }
        }.build()

        return _CpuChip!!
    }

val Ecg_heart: ImageVector
    get() {
        if (_Ecg_heart != null) return _Ecg_heart!!

        _Ecg_heart = ImageVector.Builder(
            name = "Ecg_heart",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000))
            ) {
                moveTo(480f, 840f)
                quadToRelative(-18f, 0f, -34.5f, -6.5f)
                reflectiveQuadTo(416f, 814f)
                lineTo(148f, 545f)
                quadToRelative(-35f, -35f, -51.5f, -80f)
                reflectiveQuadTo(80f, 371f)
                quadToRelative(0f, -103f, 67f, -177f)
                reflectiveQuadToRelative(167f, -74f)
                quadToRelative(48f, 0f, 90.5f, 19f)
                reflectiveQuadToRelative(75.5f, 53f)
                quadToRelative(32f, -34f, 74.5f, -53f)
                reflectiveQuadToRelative(90.5f, -19f)
                quadToRelative(100f, 0f, 167.5f, 74f)
                reflectiveQuadTo(880f, 370f)
                quadToRelative(0f, 49f, -17f, 94f)
                reflectiveQuadToRelative(-51f, 80f)
                lineTo(543f, 814f)
                quadToRelative(-13f, 13f, -29f, 19.5f)
                reflectiveQuadToRelative(-34f, 6.5f)
                moveToRelative(40f, -520f)
                quadToRelative(10f, 0f, 19f, 5f)
                reflectiveQuadToRelative(14f, 13f)
                lineToRelative(68f, 102f)
                horizontalLineToRelative(166f)
                quadToRelative(7f, -17f, 10.5f, -34.5f)
                reflectiveQuadTo(801f, 370f)
                quadToRelative(-2f, -69f, -46f, -118.5f)
                reflectiveQuadTo(645f, 202f)
                quadToRelative(-31f, 0f, -59.5f, 12f)
                reflectiveQuadTo(536f, 249f)
                lineToRelative(-27f, 29f)
                quadToRelative(-5f, 6f, -13f, 9.5f)
                reflectiveQuadToRelative(-16f, 3.5f)
                reflectiveQuadToRelative(-16f, -3.5f)
                reflectiveQuadToRelative(-14f, -9.5f)
                lineToRelative(-27f, -29f)
                quadToRelative(-21f, -23f, -49f, -36f)
                reflectiveQuadToRelative(-60f, -13f)
                quadToRelative(-66f, 0f, -110f, 50.5f)
                reflectiveQuadTo(160f, 370f)
                quadToRelative(0f, 18f, 3f, 35.5f)
                reflectiveQuadToRelative(10f, 34.5f)
                horizontalLineToRelative(187f)
                quadToRelative(10f, 0f, 19f, 5f)
                reflectiveQuadToRelative(14f, 13f)
                lineToRelative(35f, 52f)
                lineToRelative(54f, -162f)
                quadToRelative(4f, -12f, 14.5f, -20f)
                reflectiveQuadToRelative(23.5f, -8f)
                moveToRelative(12f, 130f)
                lineToRelative(-54f, 162f)
                quadToRelative(-4f, 12f, -15f, 20f)
                reflectiveQuadToRelative(-24f, 8f)
                quadToRelative(-10f, 0f, -19f, -5f)
                reflectiveQuadToRelative(-14f, -13f)
                lineToRelative(-68f, -102f)
                horizontalLineTo(236f)
                lineToRelative(237f, 237f)
                quadToRelative(2f, 2f, 3.5f, 2.5f)
                reflectiveQuadToRelative(3.5f, 0.5f)
                reflectiveQuadToRelative(3.5f, -0.5f)
                reflectiveQuadToRelative(3.5f, -2.5f)
                lineToRelative(236f, -237f)
                horizontalLineTo(600f)
                quadToRelative(-10f, 0f, -19f, -5f)
                reflectiveQuadToRelative(-15f, -13f)
                close()
            }
        }.build()

        return _Ecg_heart!!
    }

val ThumbsupFilled: ImageVector
    get() {
        if (_ThumbsupFilled != null) return _ThumbsupFilled!!

        _ThumbsupFilled = ImageVector.Builder(
            name = "ThumbsupFilled",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(10.54f, 2f)
                curveToRelative(0.289f, 0.001f, 0.57f, 0.088f, 0.81f, 0.25f)
                arcToRelative(1.38f, 1.38f, 0f, false, true, 0.45f, 1.69f)
                lineToRelative(-0.97f, 2.17f)
                horizontalLineToRelative(2.79f)
                arcToRelative(1.36f, 1.36f, 0f, false, true, 1.16f, 0.61f)
                arcToRelative(1.35f, 1.35f, 0f, false, true, 0.09f, 1.32f)
                curveToRelative(-0.67f, 1.45f, -1.87f, 4.07f, -2.27f, 5.17f)
                arcToRelative(1.38f, 1.38f, 0f, false, true, -1.29f, 0.9f)
                horizontalLineTo(2.38f)
                arcTo(1.4f, 1.4f, 0f, false, true, 1f, 12.71f)
                verticalLineTo(9.2f)
                arcToRelative(1.38f, 1.38f, 0f, false, true, 1.38f, -1.38f)
                horizontalLineToRelative(1.38f)
                lineTo(9.6f, 2.36f)
                arcToRelative(1.41f, 1.41f, 0f, false, true, 0.94f, -0.36f)
                close()
            }
        }.build()

        return _ThumbsupFilled!!
    }

val TriangleRight: ImageVector
    get() {
        if (_TriangleRight != null) return _TriangleRight!!

        _TriangleRight = ImageVector.Builder(
            name = "TriangleRight",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(5.56f, 14f)
                lineTo(5f, 13.587f)
                verticalLineTo(2.393f)
                lineTo(5.54f, 2f)
                lineTo(11f, 7.627f)
                verticalLineToRelative(0.827f)
                lineTo(5.56f, 14f)
                close()
            }
        }.build()

        return _TriangleRight!!
    }

val SettingsIcon: ImageVector
    get() {
        if (_Settings != null) return _Settings!!

        _Settings = ImageVector.Builder(
            name = "Settings",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(6f, 9.5f)
                curveTo(6.93191f, 9.5f, 7.71496f, 10.1374f, 7.93699f, 11f)
                horizontalLineTo(13.5f)
                curveTo(13.7761f, 11f, 14f, 11.2239f, 14f, 11.5f)
                curveTo(14f, 11.7455f, 13.8231f, 11.9496f, 13.5899f, 11.9919f)
                lineTo(13.5f, 12f)
                lineTo(7.93673f, 12.001f)
                curveTo(7.71435f, 12.8631f, 6.93155f, 13.5f, 6f, 13.5f)
                curveTo(5.06845f, 13.5f, 4.28565f, 12.8631f, 4.06327f, 12.001f)
                lineTo(2.5f, 12f)
                curveTo(2.22386f, 12f, 2f, 11.7761f, 2f, 11.5f)
                curveTo(2f, 11.2545f, 2.17688f, 11.0504f, 2.41012f, 11.0081f)
                lineTo(2.5f, 11f)
                horizontalLineTo(4.06301f)
                curveTo(4.28504f, 10.1374f, 5.06809f, 9.5f, 6f, 9.5f)
                close()
                moveTo(6f, 10.5f)
                curveTo(5.44772f, 10.5f, 5f, 10.9477f, 5f, 11.5f)
                curveTo(5f, 12.0523f, 5.44772f, 12.5f, 6f, 12.5f)
                curveTo(6.55228f, 12.5f, 7f, 12.0523f, 7f, 11.5f)
                curveTo(7f, 10.9477f, 6.55228f, 10.5f, 6f, 10.5f)
                close()
                moveTo(10f, 2.5f)
                curveTo(10.9319f, 2.5f, 11.715f, 3.13738f, 11.937f, 3.99998f)
                lineTo(13.5f, 4f)
                curveTo(13.7761f, 4f, 14f, 4.22386f, 14f, 4.5f)
                curveTo(14f, 4.74546f, 13.8231f, 4.94961f, 13.5899f, 4.99194f)
                lineTo(13.5f, 5f)
                lineTo(11.9367f, 5.00102f)
                curveTo(11.7144f, 5.86312f, 10.9316f, 6.5f, 10f, 6.5f)
                curveTo(9.06845f, 6.5f, 8.28565f, 5.86312f, 8.06327f, 5.00102f)
                lineTo(2.5f, 5f)
                curveTo(2.22386f, 5f, 2f, 4.77614f, 2f, 4.5f)
                curveTo(2f, 4.25454f, 2.17688f, 4.05039f, 2.41012f, 4.00806f)
                lineTo(2.5f, 4f)
                lineTo(8.06301f, 3.99998f)
                curveTo(8.28504f, 3.13738f, 9.06809f, 2.5f, 10f, 2.5f)
                close()
                moveTo(10f, 3.5f)
                curveTo(9.44772f, 3.5f, 9f, 3.94772f, 9f, 4.5f)
                curveTo(9f, 5.05228f, 9.44772f, 5.5f, 10f, 5.5f)
                curveTo(10.5523f, 5.5f, 11f, 5.05228f, 11f, 4.5f)
                curveTo(11f, 3.94772f, 10.5523f, 3.5f, 10f, 3.5f)
                close()
            }
        }.build()

        return _Settings!!
    }

val Mail: ImageVector
    get() {
        if (_Mail != null) return _Mail!!

        _Mail = ImageVector.Builder(
            name = "Mail",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000))
            ) {
                moveTo(160f, 800f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(80f, 720f)
                verticalLineToRelative(-480f)
                quadToRelative(0f, -33f, 23.5f, -56.5f)
                reflectiveQuadTo(160f, 160f)
                horizontalLineToRelative(640f)
                quadToRelative(33f, 0f, 56.5f, 23.5f)
                reflectiveQuadTo(880f, 240f)
                verticalLineToRelative(480f)
                quadToRelative(0f, 33f, -23.5f, 56.5f)
                reflectiveQuadTo(800f, 800f)
                close()
                moveToRelative(320f, -280f)
                lineTo(160f, 320f)
                verticalLineToRelative(400f)
                horizontalLineToRelative(640f)
                verticalLineToRelative(-400f)
                close()
                moveToRelative(0f, -80f)
                lineToRelative(320f, -200f)
                horizontalLineTo(160f)
                close()
                moveTo(160f, 320f)
                verticalLineToRelative(-80f)
                verticalLineToRelative(480f)
                close()
            }
        }.build()

        return _Mail!!
    }

val Key: ImageVector
    get() {
        if (_Key != null) return _Key!!

        _Key = ImageVector.Builder(
            name = "Key",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(11.351f, 1.091f)
                arcToRelative(4.528f, 4.528f, 0f, false, true, 3.44f, 3.16f)
                curveToRelative(0.215f, 0.724f, 0.247f, 1.49f, 0.093f, 2.23f)
                arcToRelative(4.583f, 4.583f, 0f, false, true, -4.437f, 3.6f)
                curveToRelative(-0.438f, 0f, -0.874f, -0.063f, -1.293f, -0.19f)
                lineToRelative(-0.8f, 0.938f)
                lineToRelative(-0.379f, 0.175f)
                horizontalLineTo(7f)
                verticalLineToRelative(1.5f)
                lineToRelative(-0.5f, 0.5f)
                horizontalLineTo(5f)
                verticalLineToRelative(1.5f)
                lineToRelative(-0.5f, 0.5f)
                horizontalLineToRelative(-3f)
                lineToRelative(-0.5f, -0.5f)
                verticalLineToRelative(-2.307f)
                lineToRelative(0.146f, -0.353f)
                lineTo(6.12f, 6.87f)
                arcToRelative(4.464f, 4.464f, 0f, false, true, -0.2f, -1.405f)
                arcToRelative(4.528f, 4.528f, 0f, false, true, 5.431f, -4.375f)
                close()
                moveToRelative(1.318f, 7.2f)
                arcToRelative(3.568f, 3.568f, 0f, false, false, 1.239f, -2.005f)
                lineToRelative(0.004f, 0.005f)
                arcTo(3.543f, 3.543f, 0f, false, false, 9.72f, 2.08f)
                arcToRelative(3.576f, 3.576f, 0f, false, false, -2.8f, 3.4f)
                curveToRelative(-0.01f, 0.456f, 0.07f, 0.908f, 0.239f, 1.33f)
                lineToRelative(-0.11f, 0.543f)
                lineTo(2f, 12.404f)
                verticalLineToRelative(1.6f)
                horizontalLineToRelative(2f)
                verticalLineToRelative(-1.5f)
                lineToRelative(0.5f, -0.5f)
                horizontalLineTo(6f)
                verticalLineToRelative(-1.5f)
                lineToRelative(0.5f, -0.5f)
                horizontalLineToRelative(1.245f)
                lineToRelative(0.876f, -1.016f)
                lineToRelative(0.561f, -0.14f)
                arcToRelative(3.47f, 3.47f, 0f, false, false, 1.269f, 0.238f)
                arcToRelative(3.568f, 3.568f, 0f, false, false, 2.218f, -0.795f)
                close()
                moveToRelative(-0.838f, -2.732f)
                arcToRelative(1f, 1f, 0f, true, false, -1.662f, -1.11f)
                arcToRelative(1f, 1f, 0f, false, false, 1.662f, 1.11f)
                close()
            }
        }.build()

        return _Key!!
    }

val Eye: ImageVector
    get() {
        if (_Eye != null) return _Eye!!

        _Eye = ImageVector.Builder(
            name = "Eye",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF0F172A)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(2.03555f, 12.3224f)
                curveTo(1.96647f, 12.1151f, 1.9664f, 11.8907f, 2.03536f, 11.6834f)
                curveTo(3.42372f, 7.50972f, 7.36079f, 4.5f, 12.0008f, 4.5f)
                curveTo(16.6387f, 4.5f, 20.5742f, 7.50692f, 21.9643f, 11.6776f)
                curveTo(22.0334f, 11.8849f, 22.0335f, 12.1093f, 21.9645f, 12.3166f)
                curveTo(20.5761f, 16.4903f, 16.6391f, 19.5f, 11.9991f, 19.5f)
                curveTo(7.36119f, 19.5f, 3.42564f, 16.4931f, 2.03555f, 12.3224f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF0F172A)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(15f, 12f)
                curveTo(15f, 13.6569f, 13.6569f, 15f, 12f, 15f)
                curveTo(10.3431f, 15f, 9f, 13.6569f, 9f, 12f)
                curveTo(9f, 10.3431f, 10.3431f, 9f, 12f, 9f)
                curveTo(13.6569f, 9f, 15f, 10.3431f, 15f, 12f)
                close()
            }
        }.build()

        return _Eye!!
    }

val EyeSlash: ImageVector
    get() {
        if (_EyeSlash != null) return _EyeSlash!!

        _EyeSlash = ImageVector.Builder(
            name = "EyeSlash",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF0F172A)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(3.97993f, 8.22257f)
                curveTo(3.05683f, 9.31382f, 2.35242f, 10.596f, 1.93436f, 12.0015f)
                curveTo(3.22565f, 16.338f, 7.24311f, 19.5f, 11.9991f, 19.5f)
                curveTo(12.9917f, 19.5f, 13.9521f, 19.3623f, 14.8623f, 19.1049f)
                moveTo(6.22763f, 6.22763f)
                curveTo(7.88389f, 5.13558f, 9.86771f, 4.5f, 12f, 4.5f)
                curveTo(16.756f, 4.5f, 20.7734f, 7.66205f, 22.0647f, 11.9985f)
                curveTo(21.3528f, 14.3919f, 19.8106f, 16.4277f, 17.772f, 17.772f)
                moveTo(6.22763f, 6.22763f)
                lineTo(3f, 3f)
                moveTo(6.22763f, 6.22763f)
                lineTo(9.87868f, 9.87868f)
                moveTo(17.772f, 17.772f)
                lineTo(21f, 21f)
                moveTo(17.772f, 17.772f)
                lineTo(14.1213f, 14.1213f)
                moveTo(14.1213f, 14.1213f)
                curveTo(14.6642f, 13.5784f, 15f, 12.8284f, 15f, 12f)
                curveTo(15f, 10.3431f, 13.6569f, 9f, 12f, 9f)
                curveTo(11.1716f, 9f, 10.4216f, 9.33579f, 9.87868f, 9.87868f)
                moveTo(14.1213f, 14.1213f)
                lineTo(9.87868f, 9.87868f)
            }
        }.build()

        return _EyeSlash!!
    }


val PersonCircle: ImageVector
    get() {
        if (_PersonCircle != null) return _PersonCircle!!

        _PersonCircle = ImageVector.Builder(
            name = "PersonCircle",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(11f, 6f)
                arcToRelative(3f, 3f, 0f, true, true, -6f, 0f)
                arcToRelative(3f, 3f, 0f, false, true, 6f, 0f)
            }
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(0f, 8f)
                arcToRelative(8f, 8f, 0f, true, true, 16f, 0f)
                arcTo(8f, 8f, 0f, false, true, 0f, 8f)
                moveToRelative(8f, -7f)
                arcToRelative(7f, 7f, 0f, false, false, -5.468f, 11.37f)
                curveTo(3.242f, 11.226f, 4.805f, 10f, 8f, 10f)
                reflectiveCurveToRelative(4.757f, 1.225f, 5.468f, 2.37f)
                arcTo(7f, 7f, 0f, false, false, 8f, 1f)
            }
        }.build()

        return _PersonCircle!!
    }

val Phone_android: ImageVector
    get() {
        if (_Phone_android != null) return _Phone_android!!

        _Phone_android = ImageVector.Builder(
            name = "Phone_android",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000))
            ) {
                moveTo(400f, 800f)
                horizontalLineToRelative(160f)
                verticalLineToRelative(-40f)
                horizontalLineTo(400f)
                close()
                moveTo(280f, 920f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(200f, 840f)
                verticalLineToRelative(-720f)
                quadToRelative(0f, -33f, 23.5f, -56.5f)
                reflectiveQuadTo(280f, 40f)
                horizontalLineToRelative(400f)
                quadToRelative(33f, 0f, 56.5f, 23.5f)
                reflectiveQuadTo(760f, 120f)
                verticalLineToRelative(720f)
                quadToRelative(0f, 33f, -23.5f, 56.5f)
                reflectiveQuadTo(680f, 920f)
                close()
                moveToRelative(0f, -200f)
                verticalLineToRelative(120f)
                horizontalLineToRelative(400f)
                verticalLineToRelative(-120f)
                close()
                moveToRelative(0f, -80f)
                horizontalLineToRelative(400f)
                verticalLineToRelative(-400f)
                horizontalLineTo(280f)
                close()
                moveToRelative(0f, -480f)
                horizontalLineToRelative(400f)
                verticalLineToRelative(-40f)
                horizontalLineTo(280f)
                close()
                moveToRelative(0f, 560f)
                verticalLineToRelative(120f)
                close()
                moveToRelative(0f, -560f)
                verticalLineToRelative(-40f)
                close()
            }
        }.build()

        return _Phone_android!!
    }

val CrossCircled: ImageVector
    get() {
        if (_CrossCircled != null) return _CrossCircled!!

        _CrossCircled = ImageVector.Builder(
            name = "CrossCircled",
            defaultWidth = 15.dp,
            defaultHeight = 15.dp,
            viewportWidth = 15f,
            viewportHeight = 15f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(0.877075f, 7.49988f)
                curveTo(0.877075f, 3.84219f, 3.84222f, 0.877045f, 7.49991f, 0.877045f)
                curveTo(11.1576f, 0.877045f, 14.1227f, 3.84219f, 14.1227f, 7.49988f)
                curveTo(14.1227f, 11.1575f, 11.1576f, 14.1227f, 7.49991f, 14.1227f)
                curveTo(3.84222f, 14.1227f, 0.877075f, 11.1575f, 0.877075f, 7.49988f)
                close()
                moveTo(7.49991f, 1.82704f)
                curveTo(4.36689f, 1.82704f, 1.82708f, 4.36686f, 1.82708f, 7.49988f)
                curveTo(1.82708f, 10.6329f, 4.36689f, 13.1727f, 7.49991f, 13.1727f)
                curveTo(10.6329f, 13.1727f, 13.1727f, 10.6329f, 13.1727f, 7.49988f)
                curveTo(13.1727f, 4.36686f, 10.6329f, 1.82704f, 7.49991f, 1.82704f)
                close()
                moveTo(9.85358f, 5.14644f)
                curveTo(10.0488f, 5.3417f, 10.0488f, 5.65829f, 9.85358f, 5.85355f)
                lineTo(8.20713f, 7.49999f)
                lineTo(9.85358f, 9.14644f)
                curveTo(10.0488f, 9.3417f, 10.0488f, 9.65829f, 9.85358f, 9.85355f)
                curveTo(9.65832f, 10.0488f, 9.34173f, 10.0488f, 9.14647f, 9.85355f)
                lineTo(7.50002f, 8.2071f)
                lineTo(5.85358f, 9.85355f)
                curveTo(5.65832f, 10.0488f, 5.34173f, 10.0488f, 5.14647f, 9.85355f)
                curveTo(4.95121f, 9.65829f, 4.95121f, 9.3417f, 5.14647f, 9.14644f)
                lineTo(6.79292f, 7.49999f)
                lineTo(5.14647f, 5.85355f)
                curveTo(4.95121f, 5.65829f, 4.95121f, 5.3417f, 5.14647f, 5.14644f)
                curveTo(5.34173f, 4.95118f, 5.65832f, 4.95118f, 5.85358f, 5.14644f)
                lineTo(7.50002f, 6.79289f)
                lineTo(9.14647f, 5.14644f)
                curveTo(9.34173f, 4.95118f, 9.65832f, 4.95118f, 9.85358f, 5.14644f)
                close()
            }
        }.build()

        return _CrossCircled!!
    }





private var _Phone_android: ImageVector? = null
private var _PersonCircle: ImageVector? = null
private var _CrossCircled: ImageVector? = null

private var _EyeSlash: ImageVector? = null


private var _Eye: ImageVector? = null


private var _Key: ImageVector? = null



private var _Mail: ImageVector? = null

private var _Settings: ImageVector? = null
private var _TriangleRight: ImageVector? = null

private var _ThumbsupFilled: ImageVector? = null
private var _Ecg_heart: ImageVector? = null
private var _CpuChip: ImageVector? = null
private var _AlignEndHorizontal: ImageVector? = null
private var _Location_on: ImageVector? = null
private var _Location_off: ImageVector? = null

