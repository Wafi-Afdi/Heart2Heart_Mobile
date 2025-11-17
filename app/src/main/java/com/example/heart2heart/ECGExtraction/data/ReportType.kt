package com.example.heart2heart.ECGExtraction.data

enum class ReportType {
    TACHYCARDHIA,
    BRADYCARDHIA,
    ASYSTOLE,
    VFIB,
    AFIB,
    VT,
    UNKNOWN,
    NORMAL,
    SOS
}

fun ReportType.toText(): String {
    return when(this) {
        ReportType.TACHYCARDHIA -> "Tachycardia"
        ReportType.BRADYCARDHIA -> "Bradycardia"
        ReportType.ASYSTOLE -> "Asystole"
        ReportType.VFIB -> "Ventricular Fibrillation"
        ReportType.AFIB -> "Atrial Fibrillation"
        ReportType.VT -> "Ventricular Tachycardia"
        ReportType.UNKNOWN -> "Unknown"
        ReportType.NORMAL -> "Normal Rhythm"
        ReportType.SOS -> "SOS"
    }
}