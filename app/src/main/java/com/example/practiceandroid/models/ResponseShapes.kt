package com.example.practiceandroid.models

data class ResponseShapes(
    val imageDictionary: Map<String, String>,
    val shapes: List<Shape>
) {

    data class Shape(
        val borderColor: String?,
        val borderWidth: Int?,
        val color: String?,
        val cornerRadius: Int?,
        val fill: String?,
        val height: Int,
        val id: Int,
        val imageId: String?,
        val rotation: Float?,
        val startRotation: Int?,
        val text: List<Text>?,
        val textVerticalAlignment: String?,
        val type: String,
        val width: Int,
        val x: Float,
        val y: Float,
        val zIndex: Float
    ) {
        data class Text(
            val alignment: String,
            var text: List<Text>
        ) {
            data class Text(
                val fontColor: String,
                val fontSize: Int,
                var text: String,
                val textDecoration: String?,
                val type: String?
            )
        }
    }
}