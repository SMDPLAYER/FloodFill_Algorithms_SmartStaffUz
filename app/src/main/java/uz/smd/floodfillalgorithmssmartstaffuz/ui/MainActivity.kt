package uz.smd.floodfillalgorithmssmartstaffuz.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isGone
import androidx.lifecycle.MutableLiveData
import uz.smd.floodfillalgorithmssmartstaffuz.R
import uz.smd.floodfillalgorithmssmartstaffuz.databinding.MainActivityBinding
import uz.smd.floodfillalgorithmssmartstaffuz.ui.main.MainScreen

class MainActivity : AppCompatActivity() {
    var binding: MainActivityBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainScreen())
                .commitNow()
        }
        handleLoad.observe(this, {
            binding?.vProgress?.isGone = !it
        })
    }

    companion object {
        val handleLoad = MutableLiveData<Boolean>()
    }
}