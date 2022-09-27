package gt.uvg.pokelist.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import gt.uvg.pokelist.R
import gt.uvg.pokelist.databinding.FragmentMainBinding
import gt.uvg.pokelist.model.Pokemon
import gt.uvg.pokelist.repository.PokemonRepository
import gt.uvg.pokelist.repository.Repository
import gt.uvg.pokelist.utils.Constants

class MainFragment: Fragment() {
    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private lateinit var viewModel: MainViewModel

    private lateinit var adapter: PokemonListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.myResponse.observe( this, Observer { response ->
            val myResults = response.results
            for (i in 0 until Constants.RETRIEVED) {
                myResults[i] = Pokemon(i+1, myResults[i].name)
            }
            Log.d("show", myResults.toString())
            adapter.updateList(myResults)
            adapter.notifyDataSetChanged()
            binding.recyclerView.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        adapter = PokemonListAdapter(emptyList())
        recyclerView.adapter = adapter
        viewModel.getPost()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}