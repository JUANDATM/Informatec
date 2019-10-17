package com.example.informatec

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFragment(AvisosFragment())
        home_BottomNav.setOnNavigationItemSelectedListener {menuItem ->
            when(menuItem.itemId ){
                R.id.avisosmenu -> {
                    setFragment(AvisosFragment())

                }
                R.id.credencialmenu -> {
                    setFragment(CredencialFragment())


                }

            }
            true
        }

    }
    fun setFragment(fragment: Fragment){
        val fragmentTransation=supportFragmentManager.beginTransaction()
        fragmentTransation.replace(R.id.frame_home,fragment)
        fragmentTransation.commit()
    }


}
