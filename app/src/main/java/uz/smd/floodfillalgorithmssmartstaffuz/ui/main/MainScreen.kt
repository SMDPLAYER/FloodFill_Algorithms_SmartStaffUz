package uz.smd.floodfillalgorithmssmartstaffuz.ui.main

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.smd.floodfillalgorithmssmartstaffuz.R
import uz.smd.floodfillalgorithmssmartstaffuz.databinding.ScreenMainBinding
import uz.smd.floodfillalgorithmssmartstaffuz.ui.MainActivity.Companion.handleLoad
import uz.smd.floodfillalgorithmssmartstaffuz.utils.setBitmapAnim

/**
 * Created by Siddikov Mukhriddin on 10/14/21
 */
class MainScreen : Fragment(R.layout.screen_main) {
    private val viewModel: MainViewModel by viewModels()
    private val binding by viewBinding(ScreenMainBinding::bind)

    private val sizes = Point(64, 64)
    private var floodFillSpeed = 100
    private var firstFloodFillMethod = 0
    private var secondFloodFillMethod = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createDialog()
        if (savedInstanceState == null)
            setAlgorithmImage(sizes)
        handleClicks()
        createSpinnerItemsListeners()
        createSeekBarValueListener()
        handleData()
    }

    @SuppressLint("FragmentLiveDataObserve")
    fun handleData() {
        viewModel.executedBitmap.observe(this, {
            showResult(it.first, it.second)
        })

        viewModel.randomBitmaps.observe(this, {
            showResults(it)
            handleLoad.value = false
        })
    }

    fun handleClicks() {
        binding.apply {
            generateNoiseButton.setOnClickListener { setAlgorithmImage(sizes) }
            firstAlgorithmImage.setOnTouchListener(::firstAlgorithmImageTouch)
            secondAlgorithmImage.setOnTouchListener(::secondAlgorithmImageTouch)
            sizeButton.setOnClickListener { dialog?.show() }
        }
    }

    fun firstAlgorithmImageTouch(view: View, event: MotionEvent): Boolean {
        if ((view as ImageView).drawable != null && event.action == MotionEvent.ACTION_DOWN)
            viewModel.executeFloodFilling(firstFloodFillMethod, binding.firstAlgorithmImage, event)
        return true
    }

    fun secondAlgorithmImageTouch(view: View, event: MotionEvent): Boolean {
        if ((view as ImageView).drawable != null && event.action == MotionEvent.ACTION_DOWN)
            viewModel.executeFloodFilling(
                secondFloodFillMethod,
                binding.secondAlgorithmImage,
                event
            )
        return true
    }

    fun showResult(bitmap: Bitmap, view: ImageView) {
        val emptyBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        if (bitmap.sameAs(emptyBitmap)) return

        view.setBitmapAnim(bitmap, (100 - floodFillSpeed * 1L))
    }

    fun setAlgorithmImage(point: Point) {
        handleLoad.value = true
        binding.firstAlgorithmImage.setImageDrawable(null)
        binding.secondAlgorithmImage.setImageDrawable(null)

        viewModel.generateRandomBitmaps(point.x, point.y)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialog = null
    }

    var dialog: AlertDialog? = null

    fun createDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_size, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialog = dialogBuilder.create()
        dialog?.setView(dialogView)

        val cancelButton = dialogView.findViewById<Button>(R.id.cancel_button)
        val okButton = dialogView.findViewById<Button>(R.id.ok_button)

        val width = dialogView.findViewById<EditText>(R.id.edit_width)
        val height = dialogView.findViewById<EditText>(R.id.edit_height)

        okButton.isEnabled = false

        width.doAfterTextChanged {
            okButton.isEnabled = (width.text.toString() != "") && (height.text.toString() != "")
        }
        height.doAfterTextChanged {
            okButton.isEnabled = (width.text.toString() != "") && (height.text.toString() != "")
        }

        okButton.setOnClickListener {
            sizes.x = if (width.text.toString().isNullOrEmpty()) sizes.x
            else width.text.toString().toInt()
            sizes.y = if (height.text.toString().isNullOrEmpty()) sizes.y
            else height.text.toString().toInt()

            width.hint = width.text
            height.hint = height.text

            dialog?.dismiss()
        }
        cancelButton.setOnClickListener { dialog?.dismiss() }
    }


    private fun createSpinnerItemsListeners() {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.algorithms_array, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.firstAlgorithmSpinner.adapter = adapter
        binding.secondAlgorithmSpinner.adapter = adapter


        binding.firstAlgorithmSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    firstFloodFillMethod = p2
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        binding.secondAlgorithmSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    secondFloodFillMethod = p2
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }

    private fun createSeekBarValueListener() {
        binding.speedSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                floodFillSpeed = p1
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }


    fun showResults(bitmaps: Array<Bitmap>) {
        binding.firstAlgorithmImage.setImageBitmap(bitmaps[0])
        binding.secondAlgorithmImage.setImageBitmap(bitmaps[1])
    }
}