package ru.lonelywh1te.kotlin_tasklist.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.lonelywh1te.kotlin_tasklist.databinding.FragmentFavouriteTasksBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.OnItemClickListener
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TaskAdapter
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.FavouriteTasksFragmentViewModel

class FavouriteTasksFragment : Fragment() {
    private lateinit var binding: FragmentFavouriteTasksBinding
    private lateinit var recycler: RecyclerView
    private lateinit var favouriteTasksFragmentViewModel: FavouriteTasksFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavouriteTasksBinding.inflate(layoutInflater, container, false)
        favouriteTasksFragmentViewModel = getViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TaskAdapter(OnItemClickListener(requireContext(), favouriteTasksFragmentViewModel))

        recycler = binding.rvTaskList
        recycler.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        favouriteTasksFragmentViewModel.favouriteTasks.observe(viewLifecycleOwner) { list ->
            adapter.updateList(list)
        }
    }

    override fun onResume() {
        super.onResume()
        favouriteTasksFragmentViewModel.getFavouriteTasks()
    }
}