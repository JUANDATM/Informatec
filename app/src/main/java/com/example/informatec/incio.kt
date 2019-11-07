package com.example.informatec

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_incio.*
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class incio : AppCompatActivity() {
    var wsConsultar : String = "http://localhost/Servicios/MostrarAlumno.php"//AGREGAMOS LA DIRECCIONDE DONDE SE VA A CONSUMIR EL SERVICIO WEB
    var hilo : ObtenerUnServicioWeb? = null
    var no :String= ""
    var nom :String= ""
    var carr :String= ""
    var fot :String= ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incio)
    }

    fun consultaXNoControl (v : View) { //CREAMOS UNA CLASE DONDE VERIFICAREMOS QUE EL CAMPO NECESARIO PARA BUSCAR EL ALUMNO  NO ESTE VACIA
        if(etNoControl.text.isEmpty()){
            Toast.makeText(this, "FALTA INGRESAR NUMERO DE CONTROL", Toast.LENGTH_SHORT).show();
            etNoControl.requestFocus()
        }else {
            val no = etNoControl.text.toString()
            hilo = ObtenerUnServicioWeb()
            hilo?.execute("Consulta", no,"","","")
        }
    }
    inner class ObtenerUnServicioWeb(): AsyncTask<String, String, String>(){

        override fun doInBackground(vararg params: String?): String {
            var Url : URL? = null
            var sResultado = ""
            try {
                val urlConn: HttpURLConnection
                val printout: DataOutputStream
                var input : DataOutputStream
                if (params[0].toString() == "Consulta") {//Aqui se decide que cuando dice Consulta en el param en en elemnto 0 se hara la cwsconsulta
                    Url = URL(wsConsultar)
                }
                urlConn = Url?.openConnection() as HttpURLConnection
                urlConn.doInput = true
                urlConn.doOutput = true
                urlConn.useCaches=false
                urlConn.setRequestProperty("Content-Type","Aplication/json")//TIPO DE DATO QUE RESIBIRA
                urlConn.setRequestProperty("Accept","aplication/json")
                urlConn.connect()
                //PREPARAR LOS DATOS A ENVIAR AL WEB SERVICE
                val jsonParam = JSONObject()
                jsonParam.put("NoControl",params[1])
                jsonParam.put("Carrera",params[2])
                jsonParam.put("Nombre",params[3])
                jsonParam.put("Foto",params[4])
                val os = urlConn.outputStream
                val writer = BufferedWriter(OutputStreamWriter(os,"UTF-8"))
                writer.write(jsonParam.toString())
                writer.flush()
                writer.close()
                val respuesta= urlConn.responseCode
                val result = StringBuilder()
                if (respuesta == HttpURLConnection.HTTP_OK){
                    val inStream : InputStream = urlConn.inputStream
                    val isReader = InputStreamReader(inStream)
                    val bReader = BufferedReader(isReader)
                    var tempStr : String?
                    while (true){
                        tempStr = bReader.readLine()
                        if (tempStr == null){
                            break
                        }
                        result.append(tempStr)
                    }
                    urlConn.disconnect()
                    sResultado = result.toString()
                }
            }catch (e: MalformedURLException){
                Log.d("JDTM",e.message)
            }catch (e: IOException){
                Log.d("JDTM",e.message)
            }catch (e: JSONException){
                Log.d("JDTM",e.message)
            }catch (e: Exception){
                Log.d("JDTM",e.message)
            }
            return sResultado
        }//Fin doInBackground    //Metodos que se van a utilizar
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val respuestaJSON = JSONObject(result)
                val resultJSON = respuestaJSON.getString("success")
                val msgJSON = respuestaJSON.getString("message")

                when {
                    resultJSON == "200" -> {
                        val alumnoJSON = respuestaJSON.getJSONArray("alumno")
                        if (alumnoJSON.length() >= 1) {
                            no = alumnoJSON.getJSONObject(0).getString("NoControl")
                            nom = alumnoJSON.getJSONObject(0).getString("Nombre")
                            carr = alumnoJSON.getJSONObject(0).getString("Carrera")
                            fot = alumnoJSON.getJSONObject(0).getString("Foto")
                            val  intent = Intent(this@incio,CredencialFragment::class.java)//se declara un intent de tipo intent y se llama a la actividad correspondiente
                            intent.putExtra(CredencialFragment.EXTRA_Nombre,nom)// se le envia la variable con los datos corespondientes en otra variable declarada en la otra actividad para poder recivir los datos
                            intent.putExtra(CredencialFragment.EXTRA_NoControl,no)
                            intent.putExtra(CredencialFragment.EXTRA_Carrera,carr)
                            intent.putExtra()
                            startActivity(intent)// Se inicia la actividad anteriormente llamada en el intent que se declaro
                        }
                    }
                }
            }catch (e: JSONException) {
                Log.d("JDTM", e.message)
            }catch (e: Exception) {
                Log.d("JDTM", e.message)
            }

        }//fin  onPostExecute //Metodos que se van a utilizar

    }
}
