package com.example.proyecto_estacionamiento
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_registro_automovil.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import android.widget.Toast




class RegistroAutomovil : AppCompatActivity() {

    lateinit var matricula: TextView
    lateinit var marcaAutoCompletar: AutoCompleteTextView
    lateinit var modeloAutoCompletar: AutoCompleteTextView
    lateinit var marca: TextView
    lateinit var modelo: TextView
    lateinit var enHora: TextView
    lateinit var saHora: TextView
    lateinit var colorAutocompletar: AutoCompleteTextView
    lateinit var color: TextView
    lateinit var folio: TextView
    lateinit var tipo: TextView
    lateinit var salida: Button
    var carro: Button? = null

    var camioneta: Button? = null
    var fondo1: ImageView? = null
    var fondo2: ImageView? = null

    var tipoVehiculo : Boolean=false
    var agregarMarmod : ImageView? = null


    var todosModelos : String = ""
    var cam: Boolean = false


    lateinit var registro : Button //Boton abajo de la pantalla para concluir cambios
    lateinit var hora : Date
    var entradaMili : Long? = null
    val dbHandler = MindOrksDBOpenHelper(this, null)


    var VehiColor = mutableListOf("Rojo","Azul","Amarillo","verde","Blanco","Negro","Plata","Gris","Beige","Cafe","Rosa","Violeta")
    var marcas: MutableList<String>  = mutableListOf(
                         "Audi","BMW","Chevrolet","Chrysler","Corvette","Dodge","Fiat","Ford","GMC","Honda","Hummer","Hyundai","Isuzu","Jaguar","Jeep","Kia","Land-rover","Mazda","Mercedes-benz","Mini",
                         "Mitsubishi","Nissan","Pontiac","Porsche","Renault","Smart","Suzuki","Toyota","Volkswagen","Volvo"
    )
    var nuevoArray = marcas
    var sqlpruebaa:sqlprueba = sqlprueba(this,null,null,1)
    var g : MutableList<String> = mutableListOf("")
    var modelos : MutableList<String> = mutableListOf(
                          "80,a4,a6,s6,coupe,s2,rs2,a8,cabriolet,s8,a3,s4,tt,s3,allroad-quattro,rs4,a2,rs6,q7,r8,a5,s5,v8,200,100,90,tts,q5,a4-allroad-quattro,tt-rs,rs5,al,a7,rs3,q3,a6-allroad-quattro,s7,sq5",
                          "serie-3,serie-5,compact,serie-7,serie-8,z3,z4,z8,x5,serie-6,x3,serie-1,z1,x6,x1",
                          "Corvette,Blazer,Astro,Nubira,Evanda,Trans Sport,Camaro,Matiz,Alero,Tahoe,Tacuma,Trailblazer,Kalos,Aveo,Lacetti,Epica,Captiva,Hhr,Cruze,Spark,Orlando,Volt,Malibu",
                          "Vision,300m,Grand Voyager,Viper,Neon,Voyager,Stratus,Sebring,Sebring 200c,New Yorker,Pt Cruiser,Crossfire,300c,Le Baron,Saratoga",
                          "Corvette",
                          "Viper,Caliber,Nitro,Avenger,Journey",
                          "Croma,Cinquecento,Seicento,Punto,Grande Punto,Panda,Tipo,Coupe,Uno,Ulysse,Tempra,Marea,Barchetta,Bravo,Stilo,Brava,Palio Weekend,600,Multipla,Idea,Sedici,Linea,500,Fiorino,Ducato,Doblo Cargo,Doblo,Strada,Regata,Talento,Argenta,Ritmo,Punto Classic,Qubo,Punto Evo,500c,Freemont,Panda Classic,500l",
                          "Maverick,Escort,Focus,Mondeo,Scorpio,Fiesta,Probe,Explorer,Galaxy,Ka,Puma,Cougar,Focus Cmax,Fusion,Streetka,Cmax,Smax,Transit,Courier,Ranger,Sierra,Orion,Pick Up,Capri,Granada,Kuga,Grand Cmax,Bmax,Tourneo Custom",
                          "GMC",
                          "Accord,Civic,Crx,Prelude,Nsx,Legend,Crv,Hrv,Logo,S2000,Stream,Jazz,Frv,Concerto,Insight,Crz",
                          "H2,H3,H3t",
                          "Lantra,Sonata,Elantra,Accent,Scoupe,Coupe,Atos,H1,Atos Prime,Xg,Trajet,Santa Fe,Terracan,Matrix,Getz,Tucson,I30,Pony,Grandeur,I10,I800,Sonata Fl,Ix55,I20,Ix35,Ix20,Genesis,I40,Veloster",
                          "Trooper,Pick Up,D Max,Rodeo,Dmax,Trroper",
                          "Serie Xj,Serie Xk,Xj,Stype,Xf,Xtype",
                          "Wrangler,Cherokee,Grand Cherokee,Commander,Compass,Wrangler Unlimited,Patriot",
                          "Sportage,Sephia,Sephia Ii,Pride,Clarus,Shuma,Carnival,Joice,Magentis,Carens,Rio,Cerato,Sorento,Opirus,Picanto,Ceed,Ceed Sporty Wagon,Proceed,K2500 Frontier,K2500,Soul,Venga,Optima,Ceed Sportswagon",
                          "Range Rover,Defender,Discovery,Freelander,Range Rover Sport,Discovery 4,Range Rover Evoque",
                          "Xedos 6,626,121,Xedos 9,323,Mx3,Rx7,Mx5,Mazda3,Mpv,Demio,Premacy,Tribute,Mazda6,Mazda2,Rx8,Mazda5,Cx7,Serie B,B2500,Bt50,Mx6,929,Cx5",
                          "Clase C,Clase E,Clase Sl,Clase S,Clase Cl,Clase G,Clase Slk,Clase V,Viano,Clase Clk,Clase A,Clase M,Vaneo,Slklasse,Slr Mclaren,Clase Cls,Clase R,Clase Gl,Clase B,100d,140d,180d,Sprinter,Vito,Transporter,280,220,200,190,600,400,Clase Sl R129,300,500,420,260,230,Clase Clc,Clase Glk,Sls Amg",
                          "Mini",
                          "Montero,Galant,Colt,Space Wagon,Space Runner,Space Gear,3000 Gt,Carisma,Eclipse,Space Star,Montero Sport,Montero Io,Outlander,Lancer,Grandis,L200,Canter,300 Gt,Asx,Imiev",
                          "Terrano Ii,Versa,Tsuru,Terrano,Micra,Sunny,Primera,Serena,Patrol,Maxima Qx,200 Sx,300 Zx,Patrol Gr,100 Nx,Almera,Pathfinder,Almera Tino,Xtrail,350z,Murano,Note,Qashqai,Tiida,Vanette,Trade,Vanette Cargo,Pickup,Navara,Cabstar E,Cabstar,Maxima,Camion,Prairie,Bluebird,Np300 Pick Up,Qashqai2,Pixo,Gtr,370z,Cube,Juke,Leaf,Evalia",
                          "Trans Sport,Firebird,Trans Am",
                          "911,Boxster,Cayenne,Carrera Gt,Cayman,928,968,944,924,Panamera,918",
                          "Megane,Safrane,Laguna,Clio,Twingo,Nevada,Espace,Spider,Scenic,Grand Espace,Avantime,Vel Satis,Grand Scenic,Clio Campus,Modus,Express,Trafic,Master,Kangoo,Mascott,Master Propulsion,Maxity,R19,R25,R5,R21,R4,Alpine,Fuego,R18,R11,R9,R6,Grand Modus,Kangoo Combi,Koleos,Fluence,Wind,Latitude,Grand Kangoo Combi",
                          "Smart,Citycoupe,Fortwo,Cabrio,Crossblade,Roadster,Forfour",
                          "Maruti,Swift,Vitara,Baleno,Samurai,Alto,Wagon R,Jimny,Grand Vitara,Ignis,Liana,Grand Vitara Xl7,Sx4,Splash,Kizashi",
                          "Carina E,4runner,Camry,Rav4,Celica,Supra,Paseo,Land Cruiser 80,Land Cruiser 100,Land Cruiser,Land Cruiser 90,Corolla,Auris,Avensis,Picnic,Yaris,Yaris Verso,Mr2,Previa,Prius,Avensis Verso,Corolla Verso,Corolla Sedan,Aygo,Hilux,Dyna,Land Cruiser 200,Verso,Iq,Urban Cruiser,Gt86",
                          "Passat,Golf,Vento,Polo,Corrado,Sharan,Lupo,Bora,Jetta,New Beetle,Phaeton,Touareg,Touran,Multivan,Caddy,Golf Plus,Fox,Eos,Caravelle,Tiguan,Transporter,Lt,Taro,Crafter,California,Santana,Scirocco,Passat Cc,Amarok,Beetle,Up,Cc",
                          "440,850,S70,V70,V70 Classic,940,480,460,960,S90,V90,Classic,S40,V40,V50,V70 Xc,Xc70,C70,S80,S60,Xc90,C30,780,760,740,240,360,340,Xc60,V60,V40 Cross Country"
    )

    var nuevoArrayModelos = modelos
    override fun onCreate(savedInstanceState: Bundle?) {
        val botonVerde: Drawable = ContextCompat.getDrawable(this,R.drawable.bg_boton_redondo_verde)!!
        val botonAzul: Drawable = ContextCompat.getDrawable(this,R.drawable.bg_boton_redondo_azul)!!

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_automovil)

        matricula = this.findViewById(R.id.matricula2)
        marca = findViewById(R.id.matricula)
        marcaAutoCompletar= findViewById(R.id.matricula)
        modeloAutoCompletar= findViewById(R.id.modelo)
        colorAutocompletar = findViewById(R.id.color)
        fondo1  = findViewById(R.id.imagenCarro)
        fondo2  = findViewById(R.id.imagen)


        var adapterColor : ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,VehiColor)
        this.colorAutocompletar.setAdapter(adapterColor)

        modelo = findViewById(R.id.modelo)
        registro = findViewById(R.id.nuevo_registro)
        salida = findViewById(R.id.salida)
        enHora = findViewById(R.id.enhora)
        saHora = findViewById(R.id.sahora)
        color = findViewById(R.id.color)
        folio = findViewById(R.id.folio)
        carro = findViewById(R.id.btnCarro) as Button
        camioneta = findViewById(R.id.btnCamioneta) as Button
        agregarMarmod = findViewById(R.id.imageView) as ImageView
        var adapterrMarcas : ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,nuevoArray)
        this.marcaAutoCompletar.setAdapter(adapterrMarcas)
        var sql  = sqlLite(this,"AUTOMOVIL.BD",null,1)

        agregarMarcass()
        agregarModeloss()


        carro!!.setOnClickListener{
            fondo1?.setColorFilter(Color.BLUE)
            fondo2?.setColorFilter(Color.GRAY)
            tipoVehiculo=false



        }
        camioneta!!.setOnClickListener{
            fondo1?.setColorFilter(Color.GRAY)
            fondo2?.setColorFilter(Color.BLUE)
            tipoVehiculo=true
        }


        val entrada = intent.getStringExtra("estado")

        val dbHandler = MindOrksDBOpenHelper(this, null)

        matricula.setOnClickListener{

        }
        agregarMarmod!!.setOnClickListener{
            if (modelo.text.isEmpty() || marca.text.isEmpty()){
                Toast.makeText(this, "Campos marca y modelo no completados", Toast.LENGTH_LONG).show()
            }
            else{
                val mensaje = sqlpruebaa.guardar(marca.text.toString(),modelo.text.toString())
                Toast.makeText(applicationContext, mensaje, Toast.LENGTH_SHORT).show()
                agregarMarcass()
                agregarModeloss()
            }
        }


        val actionBar = supportActionBar //Declaramos la barrra superior para su uso
        actionBar?.setDisplayHomeAsUpEnabled(true) //Activamos el icono de regreso de actividad


        if(entrada == "Registro") {
            val folioRecibido: String = intent.getStringExtra("codigo")

            var numeroDeFolioRecibido : String = folioRecibido.substring(1).toInt().toString()
            var letra = folioRecibido[0]
            var nuevoNumero: Int = numeroDeFolioRecibido.toInt()+1

            if(nuevoNumero > 3){
                if(letra == "Z".single()){
                    letra = "A".single()
                }else{
                    ++letra
                }

                nuevoNumero = 1
                numeroDeFolioRecibido="1"
            }

            val folioInsertar: String = when(numeroDeFolioRecibido.length){

                1 -> letra+"000"+nuevoNumero.toString()
                2 -> letra+"00"+nuevoNumero.toString()
                3 -> letra+"0"+nuevoNumero.toString()
                else -> "Error"
            }

            folio.text = folioInsertar

            //Al entrar significa que se hará un registro


            //Ocultamos el reloj de salida, dado que solo se pondra la hora actual
            saHora.visibility = View.GONE
            reloj2.visibility = View.GONE
            salida.visibility = View.GONE


            makeEnabled(true)
            folio.isEnabled = true

            //Se pone la hora actual en el textView
            enHora.text = getHoraActual("HH:mm")

            //Se ajusta el nombre del boton
            actionBar?.title = "Registro de automovil"


            registro.setOnClickListener {

                //Obtenemos el texto de los EditText
                val texts = getText()

                //Toast.makeText(this, mat + "Added to database", Toast.LENGTH_LONG).show()


                if (texts[0].equals("") || texts[1].equals("") || texts[2].equals("")) {


                    Toast.makeText(this@RegistroAutomovil, "Faltan Campos por completar", Toast.LENGTH_SHORT).show()


                } else {

                    hora = Date()
                    var horaEntrada = getHoraActual("HH:mm")
                    entradaMili = hora.time

                    //Verificamos que el folio que vamos a usar sea el esperado, si fue modificado, se le agrega una coma al inicio para saber
                    // que es de un socio o valet parking
                    val folioFinal : String = if(texts[6] != folioInsertar){
                        ","+texts[6]
                    }else{
                        dbHandler.upDateFolio(texts[6])
                        texts[6]
                    }

                    val automovil = Automovil(texts[0],texts[1],texts[2],texts[3],"",texts[5],tipoVehiculo,folioFinal)
                    dbHandler.addFields(automovil,true)

                    //checamos que cuando mandemos llamar la lista con automoviles esta ya tenga registrado a un automovil
                    // de otra forma este arreglo se define como el primero

                    intent()

                }
            }
            modelo.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    mandarModelos()
                    autocompletaMarca()
                    //SAVE THE DATA


                } else{
                    mandarModelos()
                }
            }
            marca.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    //SAVE THE DATA
                    autocompletaMarca()
                    if(marca.text.isEmpty()){
                        for(num2 in 0..nuevoArray.size-1){
                            todosModelos=todosModelos+","+nuevoArrayModelos.get(num2)

                        }
                        val todosModelosSeparados = todosModelos.split(",")
                        todosModelos=""
                        var adapterr : ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,todosModelosSeparados)
                        this.modeloAutoCompletar.setAdapter(adapterr)
                    }
                } else{
                    mandarModelos()
                }
            }

        }else{ //Si entra aquí tendrá una salida o esta checando información

            val automovil = intent.getParcelableExtra<Automovil>("Auto")
            registro.background = botonVerde

            registro.text = "Editar"
            setTexts(automovil)


            if(automovil.horaSalida != ""){
                saHora.text = automovil.horaSalida
                salida.visibility = View.GONE
                actionBar?.title = "Detalles de automovil"

            }else{

                saHora.text = "--:--"
                actionBar?.title = "Salida de automovil"

            }

            salida.setOnClickListener {
                val texts = getText()
                val automovilPantalla = Automovil(texts[0],texts[1],texts[2],texts[3],"",texts[5],cam,texts[6])

                hora = Date()

                val horaSalida = getHoraActual("HH:mm")

                entradaMili = hora.time

                exitParking(automovilPantalla,horaSalida,dbHandler)

                intent()

            }
            if(entrada == "Salida"){

                registro.setOnClickListener {

                    if(salida.visibility == View.VISIBLE){

                        makeEnabled(true)
                        salida.visibility = View.GONE
                        registro.background = botonAzul
                        registro.text = "Confirmar"

                    }else{
                        registro.text = "Editar"
                        registro.background = botonVerde
                        makeEnabled(false)
                        val texts = getText()
                        val automovilPantalla = Automovil(texts[0],texts[1],texts[2],texts[3],"",texts[5],cam,texts[6])

                        update(automovilPantalla,automovil,true,dbHandler)

                        salida.visibility = View.VISIBLE

                    }
                }

            }else{

                registro.setOnClickListener {
                    var g = dbHandler.llenarArrayMarca()

                    if (registro.background == botonVerde){
                        makeEnabled(true)
                        registro.background = botonAzul
                        registro.text = "Confirmar"
                    }else{
                        makeEnabled(false)
                        registro.text = "Editar"
                        registro.background = botonVerde
                        val texts = getText()
                        val automovilPantalla = Automovil(texts[0],texts[1],texts[2],texts[3],"",texts[5],cam,texts[6])
                        update(automovilPantalla,automovil,false,dbHandler)




                    }
                }

            }

        }
    }

    override fun onBackPressed() {
        val botonVerde: Drawable = ContextCompat.getDrawable(this,R.drawable.bg_boton_redondo_verde)!!
        val botonAzul: Drawable = ContextCompat.getDrawable(this,R.drawable.bg_boton_redondo_azul)!!


        when(intent.getStringExtra("estado")){
            "Registro" -> super.onBackPressed()

            "Salida" -> {
                if(salida.visibility == View.VISIBLE){
                    refreshActivity()
                    super.onBackPressed()
                }else{
                    registro.text = "Editar"
                    registro.background = botonVerde
                    makeEnabled(false)
                    salida.visibility = View.VISIBLE
                }
            }

            "Chequeo" -> {
                if(salida.background == botonAzul){
                    salida.background = botonVerde
                }else{
                    refreshActivity()
                    super.onBackPressed()
                }

            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun refreshActivity(){
        val intent = Intent(this,MainActivityReal::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    private fun getText(): List<String>{
        val mat = matricula.text.toString()
        val mar = marca.text.toString()
        val mod = modelo.text.toString()
        val horae = enHora.text.toString()
        val horas = saHora.text.toString()
        val colo = colorAutocompletar.text.toString()
        val foli = folio.text.toString()

        return(listOf(mat,mar,mod,horae,horas,colo,foli))
    }

    private fun setTexts(automovilToSet: Automovil){


        matricula.text = automovilToSet.matricula
        marca.text = automovilToSet.marca
        modelo.text = automovilToSet.modelo
        enHora.text = automovilToSet.horaEntrada
        color.text = automovilToSet.color
        folio.text = if(automovilToSet.folio[0] == ",".single()){
            automovilToSet.folio.substring(1)
        }else{
            automovilToSet.folio
        }


    }

    private fun update(automovil1: Automovil ,automovil2:Automovil ,bol:Boolean, dbHandler: MindOrksDBOpenHelper){

        dbHandler.modify(automovil1,automovil2,bol)
    }

    private fun makeEnabled(bol: Boolean){
        marca.isEnabled = bol
        matricula.isEnabled = bol
        modelo.isEnabled = bol
        colorAutocompletar.isEnabled = bol
    }

    private fun getHoraActual(strFormato: String): String {
        val objCalendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat(strFormato)
        return simpleDateFormat.format(objCalendar.time)

    }

    fun imprimirArray(array : ArrayList<Automovil>){

        for (elemento in array){
            Toast.makeText(this@RegistroAutomovil, elemento.matricula+elemento.marca+elemento.modelo+elemento.horaEntrada+elemento.horaSalida, Toast.LENGTH_SHORT).show()
        }

    }

    private fun intent(){

        val intent = Intent(applicationContext,MainActivityReal::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun exitParking(automovil:Automovil, horaSalida: String, dbHandler: MindOrksDBOpenHelper){
        //val numeroIDSQLite = intent.getIntExtra("NumeroDeSQLite",Int.MAX_VALUE)

        dbHandler.dropElement(automovil) //retiramos el automovil de entradas

        automovil.horaSalida = horaSalida

        dbHandler.addFields(automovil,false) //ponemos el automovil en salida


    }

    fun mandarModelos (){
        for(num in 0..nuevoArray.size-1){
            var textoPosicion:String = nuevoArray.get(num)
            var textoMarca:String = marca.text.toString()
            var comparacion = textoPosicion.compareTo(textoMarca)
            if(comparacion==0){
                var mod:String=nuevoArrayModelos.get(num)
                var modelosCorre = mod.split(",")
                //var a1 : MutableList<String>  = modelosCorre.toMutableList()
                //aqui
                /*
                var nuevosModelos = dbHandler.llenarArrayModelo()
                for(num3 in 0..nuevosModelos.size-1){
                    a1.add(num3.toString())
                }*/
                var adapterr : ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,g)
                this.modeloAutoCompletar.setAdapter(adapterr)
                adapterr = ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,modelosCorre)
                this.modeloAutoCompletar.setAdapter(adapterr)
                modelosCorre=g

            }
        }
        if(marca.text.isEmpty()){
            for(num2 in 0..nuevoArrayModelos.size-1){
                todosModelos=todosModelos+","+nuevoArrayModelos.get(num2)
            }
            var todosModelosSeparados  = todosModelos.split(",")
            todosModelos=""
            //var a2 : MutableList<String>  = todosModelosSeparados.toMutableList()
            //aqui
            /*
            var nuevosModelos = dbHandler.llenarArrayModelo()
            for(num3 in 0..nuevosModelos.size-1){
                a2.add(num3.toString())
            }*/
            var adapterrr : ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,g)
            this.modeloAutoCompletar.setAdapter(adapterrr)
            adapterrr = ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,todosModelosSeparados)
            this.modeloAutoCompletar.setAdapter(adapterrr)
            todosModelosSeparados=g
        }

    }

    fun autocompletaMarca(){
        for(num3 in 0..nuevoArrayModelos.size-1){
            val todosModelos = nuevoArrayModelos.get(num3)
            val todosModelosSeparados = todosModelos.split(",")
            for(num4 in 0..todosModelosSeparados.size-1){
                var textoPosicion:String = todosModelosSeparados[num4]
                var textoModelo:String = modelo.text.toString()
                var comparacion = textoModelo.compareTo(textoPosicion)
                if(comparacion==0){
                    marca.text = nuevoArray.get(num3)
                }
            }
        }
    }

    fun cerrarteclado() {
        val view : View? = currentFocus
        if(view != null){
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

    }
    fun agregarMarcass (){
        nuevoArray = marcas
        var nuevosMarcas = sqlpruebaa.llenar_marca()
        for(num3 in 0..nuevosMarcas.size-1){
            nuevoArray.add(nuevosMarcas.get(num3).toString())
        }
        //var adapterrMarcas : ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,nuevoArray)
        //this.marcaAutoCompletar.setAdapter(adapterrMarcas)
    }
    fun agregarModeloss (){
        nuevoArrayModelos = modelos

        var nuevosModelos = sqlpruebaa.llenar_modelo()

        for(num3 in 0..nuevosModelos.size-1){
            nuevoArrayModelos.add(nuevosModelos.get(num3).toString())
        }
        //var adapterrMarcas : ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,nuevoArrayModelos)
        //this.modeloAutoCompletar.setAdapter(adapterrMarcas)
    }



}