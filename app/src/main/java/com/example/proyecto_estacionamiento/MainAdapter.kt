package com.example.proyecto_estacionamiento
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.carros_row.view.*
import kotlinx.android.synthetic.main.carros_row.view.hora_entrada1
import kotlinx.android.synthetic.main.carros_row.view.hora_entrada2
import kotlinx.android.synthetic.main.carros_row.view.state_carro
import kotlinx.android.synthetic.main.carros_row_folio.view.*
import kotlin.collections.ArrayList


//Esta clase es para que los elementos se generen repetitivamente con datos especificos
class MainAdapter(var estacionamiento: Estacionamiento, val context: FragmentoBusqueda,
                  val type: Int, val activity: MainActivityReal): RecyclerView.Adapter<CustomViewHolder>(),Filterable, CustomViewHolder.checked {

    var list: List<Int> = arrayListOf()

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



        val carrosRow = if(type == 2){
            R.layout.carros_row_folio
        }else{
            R.layout.carros_row
        }


        val lineOfRow = layoutInflater.inflate(carrosRow,parent,false)
        return CustomViewHolder(lineOfRow, cosa = context, cosa2 = this, activity = activity )
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

                holder.view.matricula.text = if(autos.folio[0].toString() == ",") autos.folio.substring(1)
                else autos.folio
                holder.view.marca_modelo.text = autos.matricula

            }else{
                holder.view.folio2.text = if(autos.folio[0].toString() == ",") autos.folio.substring(1)
                else autos.folio
            }


            holder.view.hora_entrada1.text = autos.horaEntrada
            holder.view.hora_entrada2.text = "--:--"

            holder.estacionamiento = estacionamiento
            holder.carro = autos
            holder.position = position

            holder.cosa = context

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
        estacionamiento.oneSelected = if(state) estacionamiento.oneSelected.inc() else estacionamiento.oneSelected.dec()
        notifyDataSetChanged()
    }
}


class CustomViewHolder(var view: View, var estacionamiento: Estacionamiento? = null,
                       var carro: Automovil? = null, var position: Int? = null,
                       var cosa: FragmentoBusqueda? = null, var cosa2: MainAdapter? = null,
                       var hold: Boolean = false, var activity: MainActivityReal? = null): RecyclerView.ViewHolder(view){


    var activityCallBack : interfazLoca? = null
    var autoCallback: checked? = null

    interface checked{
        fun imageChecked(position: Int, state: Boolean)
    }

    interface interfazLoca {
        fun onCheckedBox(automovil : Automovil,state: Boolean)
    }

    private fun selection(){

        if(hold){

            activityCallBack?.onCheckedBox(carro!!,false)
            autoCallback?.imageChecked(position!!,false)

        }else{

            activityCallBack?.onCheckedBox(carro!!,true)
            autoCallback?.imageChecked(position!!,true)

        }

    }

    init {

        if(cosa != null){

            activityCallBack = cosa!!

        }

        if(cosa2 != null){

            autoCallback = cosa2!!

        }


        view.setOnClickListener{
            if(estacionamiento!!.oneSelected > 0){
                selection()
            }else{
                val intent = Intent(view.context,RegistroAutomovil::class.java)
                intent.putExtra("Auto",carro)
                intent.putExtra("estado","Salida")
                intent.putExtra("Estacionamiento",estacionamiento)
                intent.putExtra("index", position)
                view.context.startActivity(intent)
            }

        }

        view.setOnLongClickListener {

            selection()
            true
        }


        /*fun createTask(): TimerTask{
            val a = timerTask{

                activity?.runOnUiThread {

                    if(hold){

                        activityCallBack?.onCheckedBox(carro!!,false)
                        autoCallback?.imageChecked(position!!,false)

                    }else{

                        activityCallBack?.onCheckedBox(carro!!,true)
                        autoCallback?.imageChecked(position!!,true)

                    }
                }
            }
            return a
        }
        val timerTask: TimerTask = createTask()
    */

        /*val timer = Timer("click")

        view.setOnTouchListener { view, event ->

            if(event.action == MotionEvent.ACTION_DOWN) {
                this@CustomViewHolder.run {
                    timer.schedule(timerTask,500)}
                println("holi1")

            /*}else if(event.action == MotionEvent.ACTION_UP){
                this@CustomViewHolder.run{ timerTask.cancel()
                timer.purge()}
                println("holi2")

            }else if(event.action == MotionEvent.ACTION_CANCEL){
                this@CustomViewHolder.run{timerTask.cancel()
                    timer.purge()  }
                println("holi3")
            */}
                false
        }*/



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

                holder.view.matricula.text = if(autos.folio[0].toString() == ",") autos.folio.substring(1)
                else autos.folio
                holder.view.marca_modelo.text = autos.matricula

            }else{
                holder.view.folio2.text = if(autos.folio[0].toString() == ",") autos.folio.substring(1)
                else autos.folio
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
