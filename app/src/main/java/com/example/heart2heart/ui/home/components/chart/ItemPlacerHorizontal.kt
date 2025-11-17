package com.example.heart2heart.ui.home.components.chart

import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.dimensions.HorizontalDimensions
import com.patrykandpatrick.vico.core.chart.draw.ChartDrawContext
import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout
import com.patrykandpatrick.vico.core.context.MeasureContext
import com.patrykandpatrick.vico.core.extension.half
import com.patrykandpatrick.vico.core.extension.round

class ItemPlacerHorizontal(
    private val spacing: Int,
    private val offset: Int,
    private val shiftExtremeTicks: Boolean = true,
    private val addExtremeLabelPadding: Boolean = false ,
): AxisItemPlacer.Horizontal {
    override fun getShiftExtremeTicks(context: ChartDrawContext): Boolean = shiftExtremeTicks

    override fun getAddFirstLabelPadding(context: MeasureContext) =
        context.horizontalLayout is HorizontalLayout.FullWidth && addExtremeLabelPadding && offset == 0

    override fun getAddLastLabelPadding(context: MeasureContext): Boolean {
        val chartValues = context.chartValuesProvider.getChartValues()
        return context.horizontalLayout is HorizontalLayout.FullWidth && addExtremeLabelPadding &&
                (chartValues.maxX - chartValues.minX - chartValues.xStep * offset) % (chartValues.xStep * spacing) == 0f
    }

    override fun getLabelValues(
        context: ChartDrawContext,
        visibleXRange: ClosedFloatingPointRange<Float>,
        fullXRange: ClosedFloatingPointRange<Float>
    ): List<Float> {
        val chartValues = context.chartValuesProvider.getChartValues()
        val remainder = ((visibleXRange.start - chartValues.minX) / chartValues.xStep - offset) % spacing
        val firstValue = visibleXRange.start + (spacing - remainder) % spacing * chartValues.xStep
        val minXOffset = chartValues.minX % chartValues.xStep
        val values = mutableListOf<Float>()
        var multiplier = -LABEL_OVERFLOW_SIZE
        var hasEndOverflow = false
        while (true) {
            var potentialValue = firstValue + multiplier++ * spacing * chartValues.xStep
            potentialValue = chartValues.xStep * ((potentialValue - minXOffset) / chartValues.xStep) + minXOffset
            if (potentialValue < chartValues.minX || potentialValue == fullXRange.start) continue
            if (potentialValue > chartValues.maxX || potentialValue == fullXRange.endInclusive) break
            values += potentialValue
            if (potentialValue > visibleXRange.endInclusive && hasEndOverflow.also { hasEndOverflow = true }) break
        }
        return values
    }

    override fun getMeasuredLabelValues(
        context: MeasureContext,
        horizontalDimensions: HorizontalDimensions,
        fullXRange: ClosedFloatingPointRange<Float>
    ): List<Float> {
        val chartValues = context.chartValuesProvider.getChartValues()
        return listOf(chartValues.minX, (chartValues.minX + chartValues.maxX).half, chartValues.maxX)
    }

    override fun getStartHorizontalAxisInset(
        context: MeasureContext,
        horizontalDimensions: HorizontalDimensions,
        tickThickness: Float
    ): Float {
        val tickSpace = if (shiftExtremeTicks) tickThickness else tickThickness.half
        return when (context.horizontalLayout) {
            is HorizontalLayout.Segmented -> tickSpace
            is HorizontalLayout.FullWidth -> (tickSpace - horizontalDimensions.unscalableStartPadding).coerceAtLeast(0f)
        }
    }

    override fun getEndHorizontalAxisInset(
        context: MeasureContext,
        horizontalDimensions: HorizontalDimensions,
        tickThickness: Float
    ): Float {
        val tickSpace = if (shiftExtremeTicks) tickThickness else tickThickness.half
        return when (context.horizontalLayout) {
            is HorizontalLayout.Segmented -> tickSpace
            is HorizontalLayout.FullWidth -> (tickSpace - horizontalDimensions.unscalableEndPadding).coerceAtLeast(0f)
        }
    }

    @Suppress("LoopWithTooManyJumpStatements")
    override fun getLineValues(
        context: ChartDrawContext,
        visibleXRange: ClosedFloatingPointRange<Float>,
        fullXRange: ClosedFloatingPointRange<Float>,
    ): List<Float>? {
        val chartValues = context.chartValuesProvider.getChartValues()
        return when (context.horizontalLayout) {
            is HorizontalLayout.Segmented -> {
                val remainder = (visibleXRange.start - fullXRange.start) % chartValues.xStep
                val firstValue = visibleXRange.start + (chartValues.xStep - remainder) % chartValues.xStep
                var multiplier = -TICK_OVERFLOW_SIZE
                val values = mutableListOf<Float>()
                while (true) {
                    val potentialValue = firstValue + multiplier++ * chartValues.xStep
                    if (potentialValue < fullXRange.start) continue
                    if (potentialValue > fullXRange.endInclusive) break
                    values += potentialValue
                    if (potentialValue > visibleXRange.endInclusive) break
                }
                values
            }
            is HorizontalLayout.FullWidth -> null
        }
    }

    private companion object {
        const val LABEL_OVERFLOW_SIZE = 2
        const val TICK_OVERFLOW_SIZE = 1
    }
}