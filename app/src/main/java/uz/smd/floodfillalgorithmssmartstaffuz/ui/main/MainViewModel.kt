package uz.smd.floodfillalgorithmssmartstaffuz.ui.main

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import uz.smd.floodfillalgorithmssmartstaffuz.model.FloodFillModel
import java.lang.Exception
import java.util.*

/**
 * Created by Siddikov Mukhriddin on 10/14/21
 */
class MainViewModel : ViewModel() {

    private var model = FloodFillModel()

    val executedBitmap=MutableLiveData<Pair<Bitmap,ImageView>>()
    val randomBitmaps=MutableLiveData<Array<Bitmap>>()

    fun executeFloodFilling(method: Int, view: ImageView, event: MotionEvent) {
        blockTry {
            val viewCoords = IntArray(2)
            view.getLocationOnScreen(viewCoords)

            val absX = event.rawX
            val absY = event.rawY

            val imgX = absX - viewCoords[0]
            val imgY = absY - viewCoords[1]

            val maxImgX = view.width
            val maxImgY = view.height

            var bitmap: Bitmap?
            try {
                bitmap = (view.drawable as BitmapDrawable).bitmap

                val maxX = bitmap.width
                val maxY = bitmap.height

                val x = (maxX * imgX / maxImgX.toFloat()).toInt()
                val y = (maxY * imgY / maxImgY.toFloat()).toInt()

                val color = bitmap.getPixel(x, y)
                val isBlack = color == Color.BLACK

                val replacementColor = if (isBlack) Color.WHITE else Color.BLACK

                model.useImage(bitmap)
                model.floodFill(method, x, y, color, replacementColor)
            } catch (e: TypeCastException) {
                e.printStackTrace()
                Log.i("TTT", "executeFloodFilling: exception occurred -> $e")
                bitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.RGB_565)
            }
            executedBitmap.postValue( Pair(bitmap!!,view))
        }
    }

    fun generateRandomBitmaps(x: Int, y: Int) {
        blockTry {

            val bmp1 = generateRandomBitmap(x, y)
            val bmp2 = generateRandomBitmap(x, y)
            randomBitmaps.postValue(arrayOf(bmp1, bmp2))
        }
    }

    private fun generateRandomBitmap(width: Int, height: Int): Bitmap {
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (i in 0 until width) {
            for (j in 0 until height) {
                val color = when ((0..1).random()) {
                    0 -> Color.BLACK
                    1 -> Color.WHITE
                    else -> Color.BLACK
                }
                bmp.setPixel(i, j, color)
            }
        }
        return bmp
    }

    private fun ClosedRange<Int>.random() = Random().nextInt((endInclusive + 1) - start) + start



    private fun blockTry(block:suspend()->Unit){
        viewModelScope.launch(Dispatchers.Default) {
            try {
                block.invoke()
            }catch (e: Exception){
                Log.e("TTT",e.toString())
            }catch (e:StackOverflowError){
                Log.e("TTT",e.toString())
            }
            catch (e:OutOfMemoryError){
                Log.e("TTT",e.toString())
            }
        }
    }

}