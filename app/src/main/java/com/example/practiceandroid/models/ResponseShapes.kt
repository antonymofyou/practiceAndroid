package com.example.practiceandroid.models

data class ResponseShapes(
    val imageDictionary: ImageDictionary,
    val shapes: List<Shape>
) {
    data class ImageDictionary(
        val `1`: String
    )

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
        val x: Int,
        val y: Int,
        val zIndex: Float
    ) {
        data class Text(
            val alignment: String,
            val text: List<Text>
        ) {
            data class Text(
                val fontColor: String,
                val fontSize: Int,
                val text: String,
                val textDecoration: String?,
                val type: String?
            )
        }
    }
}