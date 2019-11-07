package com.example.informatec


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_credencial.*

class CredencialFragment : Fragment() {
    companion object {//se declara el companion object para tener acceso a las variables que deseamos traer
         val EXTRA_Nombre = "extraNombre" // se declara las variables donde se va a recivir los datos desde la otra actividad
         val EXTRA_NoControl = "extraNoControl"
        val EXTRA_Carrera = "extraCarrera"




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_credencial, container, false)

        val intent = activity?.intent
        if (intent != null && intent.hasExtra(EXTRA_Nombre)) {
            var nombre: String = intent.getStringExtra(EXTRA_Nombre)
            tvNombre.text = nombre
        }

        return view

    }
}
