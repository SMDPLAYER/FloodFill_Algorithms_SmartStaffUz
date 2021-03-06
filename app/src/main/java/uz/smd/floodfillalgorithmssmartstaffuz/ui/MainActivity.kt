package uz.smd.floodfillalgorithmssmartstaffuz.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isGone
import androidx.lifecycle.MutableLiveData
import uz.smd.floodfillalgorithmssmartstaffuz.R
import uz.smd.floodfillalgorithmssmartstaffuz.databinding.MainActivityBinding
import uz.smd.floodfillalgorithmssmartstaffuz.ui.main.MainScreen

/**
 * Created by Siddikov Mukhriddin on 10/14/21
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

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