package com.example.proyecto_estacionamiento
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.carros_row.view.*
import kotlinx.android.synthetic.main.activity_main_real.*
import kotlinx.android.synthetic.main.carros_row.view.hora_entrada1
import kotlinx.android.synthetic.main.carros_row.view.hora_entrada2
import kotlinx.android.synthetic.main.carros_row.view.state_carro
import kotlinx.android.synthetic.main.carros_row_folio.view.*


//Esta clase es para que los elementos se generen repetitivamente con datos especificos
class MainAdapter(var estacionamiento: Estacionamiento,
                  val context: FragmentoBusqueda, val pasado: Pasado, val type: Int): RecyclerView.Adapter<CustomViewHolder>(),Filterable, CustomViewHolder.checked {


    val carros = if(estacionamiento.carros != null){

        estacionamiento.carros!!

    }else{

        mutableListOf()

    }

    var filterList: MutableList<Automovil> = carros

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val text: String = constraint.toString()
                if(text.isEmpty()) {

                    filterList = carros

                }else{

                    val returnList = ArrayList<Automovil>()
                    for(row in carros){
                        if(row.marca.toLowerCase().contains(text.toLowerCase()) ||
                            row.matricula.toLowerCase().contains(text.toLowerCase()) ||
                                    row.modelo.toLowerCase().contains(text.toLowerCase()) ||
                                    row.folio.toLowerCase().contains(text.toLowerCase())
                                )  returnList.add(row)
                    }

                    filterList = returnList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = filterList

                return filterResults

            }

            override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {

                filterList = filterResults?.values as MutableList<Automovil>
                notifyDataSetChanged()

            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)



        val carros_row = if(type == 2){
            R.layout.carros_row_folio
        }else{
            R.layout.carros_row
        }


        val lineOfRow = layoutInflater.inflate(carros_row,parent,false)
        return CustomViewHolder(lineOfRow, cosa = context, cosa2 = this)
    }

    override fun getItemCount(): Int {

        return if(filterList.isEmpty()){
            0
        }else filterList.size

    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        if(filterList.isNotEmpty()){

            val autos = filterList[position]

            if (autos.horaSalida!=""){ //Con esta condición verificamos si el auto se encuentra en el estacionamiento y si no es así (posee hora de salida) se marca con rojo
                holder.view.state_carro.setImageResource(R.drawable.bg_boton_redondo_rojo)
            }

            if(type == 0){
                holder.view.matricula.text = autos.matricula
                holder.view.marca_modelo.text = autos.marca + " " + autos.modelo
            }else if(type == 1){
                holder.view.matricula.text = autos.folio
                holder.view.marca_modelo.text = autos.matricula

            }else{
                holder.view.folio2.text = autos.folio
            }
            holder.view.hora_entrada1.text = autos.horaEntrada
            holder.view.hora_entrada2.text = "--:--"

            holder.estacionamiento = estacionamiento
            holder.carro = autos
            holder.position = position

            holder.cosa = context
            holder.pasado = pasado

            if(autos.checked){

                holder.view.state_carro.setImageResource(R.drawable.bg_boton_redondo_rojo)

            }else{

                holder.view.state_carro.setImageResource(R.drawable.bg_boton_redondo_azul)

            }

            holder.hold = autos.checked
        }
    }

    override fun imageChecked(position: Int, state: Boolean) {
        changeChecked(position, state)
    }

    private fun changeChecked(position: Int, state: Boolean){

        filterList[position].checked = state
        notifyDataSetChanged()
    }
}


class CustomViewHolder(var view: View, var estacionamiento: Estacionamiento? = null,
                       var carro: Automovil? = null, var position: Int? = null,
                       var cosa: FragmentoBusqueda? = null, var cosa2: MainAdapter? = null, var pasado: Pasado? = null,
                       var hold: Boolean = false): RecyclerView.ViewHolder(view){


    var activityCallBack : interfazLoca? = null
    var autoCallback: checked? = null

    interface checked{
        fun imageChecked(position: Int, state: Boolean)
    }

    interface interfazLoca {
        fun onCheckedBox(automovil : Automovil,state: Boolean)
    }


    init {

        if(cosa != null){

            activityCallBack = cosa!!

        }

        if(cosa2 != null){

            autoCallback = cosa2!!

        }

        view.setOnClickListener{
            val intent = Intent(view.context,RegistroAutomovil::class.java)
            intent.putExtra("Auto",carro)
            intent.putExtra("estado","Salida")
            intent.putExtra("Estacionamiento",estacionamiento)
            intent.putExtra("index", position)
            view.context.startActivity(intent)

        }
        view.setOnLongClickListener {
            if(hold){

                activityCallBack?.onCheckedBox(carro!!,false)
                autoCallback?.imageChecked(position!!,false)

            }else{

                activityCallBack?.onCheckedBox(carro!!,true)
                autoCallback?.imageChecked(position!!,true)

            }

            //Toast.makeText(view.context,"Holi", Toast.LENGTH_SHORT).show()
            true
        }


        /*view.setOnLongClickListener {
            fun click(): Boolean {
                view.setBackgroundColor(Color.parseColor("#5054B4"))
                return true
            }
            click()
        }*/ //Retomar esta parte de codigo cuando se pueda eliminar el arreglo


    }


}




class MainAdapter2(val pasado: Pasado, val type: Int)/*(Carros: ArrayList<Array>)*/: RecyclerView.Adapter<CustomViewHolder2>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder2 {
        val layoutInflater = LayoutInflater.from(parent.context)
        val carros_row = if(type == 2){
            R.layout.carros_row_folio
        }else{
            R.layout.carros_row
        }
        val lineOfRow = layoutInflater.inflate(carros_row,parent,false)
        return CustomViewHolder2(lineOfRow)
    }

    override fun getItemCount(): Int {
        return if(pasado.carros != null){
            pasado.carros!!.size
        }else 0
    }

    override fun onBindViewHolder(holder: CustomViewHolder2, position: Int) {
        if(pasado.carros != null){
            val autos = pasado.carros!![position]

            if (autos.horaSalida!=""){ //Con esta condición verificamos si el auto se encuentra en el estacionamiento y si no es así (posee hora de salida) se marca con rojo
                holder.view.state_carro.setImageResource(R.drawable.bg_boton_redondo_rojo)
            }

            if(type == 0){
                holder.view.matricula.text = autos.matricula
                holder.view.marca_modelo.text = autos.marca + " " + autos.modelo
            }else if(type == 1){
                holder.view.matricula.text = autos.folio
                holder.view.marca_modelo.text = autos.matricula

            }else{
                holder.view.folio2.text = autos.folio
            }
            holder.view.hora_entrada1.text = autos.horaEntrada
            holder.view.hora_entrada2.text = autos.horaSalida


            holder.carro = autos
            holder.position = position


        }

    }
}


class CustomViewHolder2(var view: View, var carro: Automovil? = null,
                        var position: Int? = null, var cosa: FragmentoBusqueda? = null): RecyclerView.ViewHolder(view){
    init {

        view.setOnClickListener{
            val intent = Intent(view.context,RegistroAutomovil::class.java)
            intent.putExtra("Auto",carro)
            intent.putExtra("estado","Chequeo")
            view.context.startActivity(intent)

        }

    }


}
