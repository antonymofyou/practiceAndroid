package com.example.practiceandroid.viewModels

import android.app.Application
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class PdfViewerViewModel(application: Application) : AndroidViewModel(application) {

    private val _bitmap = MutableLiveData<Bitmap?>()
    val bitmap: LiveData<Bitmap?> = _bitmap

    private lateinit var pdfRenderer: PdfRenderer
    private var currentPage: PdfRenderer.Page? = null
    private var currentPageIndex = 0
    private var pageCount = 0

    fun loadPdf(pdfFileName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val context = getApplication<Application>().applicationContext
            val file = File(context.cacheDir, pdfFileName)
            context.assets.open(pdfFileName).use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            val parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            pdfRenderer = PdfRenderer(parcelFileDescriptor)
            pageCount = pdfRenderer.pageCount
            showPage(0)
        }
    }

    private fun showPage(index: Int) {
        pdfRenderer.let { renderer ->
            currentPage?.close()
            currentPage = renderer.openPage(index)
            currentPage?.let { page ->
                val width = getApplication<Application>().resources.displayMetrics.densityDpi / 72 * page.width
                val height = getApplication<Application>().resources.displayMetrics.densityDpi / 72 * page.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                page.render(bmp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                _bitmap.postValue(bmp)
            }
        }
    }

    fun nextPage() {
        if (currentPageIndex < pageCount - 1) {
            currentPageIndex++
            showPage(currentPageIndex)
        }
    }

    fun previousPage() {
        if (currentPageIndex > 0) {
            currentPageIndex--
            showPage(currentPageIndex)
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentPage?.close()
        pdfRenderer.close()
    }
}