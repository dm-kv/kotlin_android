package ru.netology.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import kotlin.getValue
import ru.netology.nmedia.utils.StringArg

class NewPostFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(layoutInflater)

        binding.edit.setText(arguments?.contentArg)

        binding.ok.setOnClickListener {
            viewModel.saveContent(binding.edit.text.toString())
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }
        return binding.root
    }

    companion object {
        var Bundle.contentArg by StringArg
    }
}




