/*
 * Copyright (C) 2023 devshadat
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.devshadat.note_app_kotlin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devshadat.note_app_kotlin.NoteApplication
import com.devshadat.note_app_kotlin.R
import com.devshadat.note_app_kotlin.databinding.FragmentNoteDetailBinding
import com.devshadat.note_app_kotlin.viewmodel.NoteViewModel
import com.devshadat.note_app_kotlin.viewmodel.NoteViewModelFactory
import com.example.inventory.data.Note
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * [NoteDetailFragment] displays the details of the selected item.
 */
class NoteDetailFragment : Fragment() {
    private val navigationArgs: NoteDetailFragmentArgs by navArgs()
    lateinit var item: Note

    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(
            (activity?.application as NoteApplication).database.noteDao()
        )
    }

    private var _binding: FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Binds views with the passed in item data.
     */
    private fun bind(item: Note) {
        binding.apply {
            itemName.text = item.noteTitle
            itemDetail.text = item.noteDetail

            // TODO
            /* sellItem.setOnClickListener { viewModel.sellItem(item) }
             deleteItem.setOnClickListener { showConfirmationDialog() }
             editItem.setOnClickListener { editItem() }*/

            editItem.setOnClickListener { editItem() }
            deleteItem.setOnClickListener { showConfirmationDialog() }
        }
    }

    /**
     * Navigate to the Edit item screen.
     */
    private fun editItem() {
        val action = NoteDetailFragmentDirections.actionNoteDetailFragmentToAddNoteFragment(
            getString(R.string.edit_fragment_title), item.id
        )
        this.findNavController().navigate(action)
    }

    /**
     * Displays an alert dialog to get the user's confirmation before deleting the item.
     */
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext()).setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question)).setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteItem()
            }.show()
    }

    /**
     * Deletes the current item and navigates to the list fragment.
     */
    private fun deleteItem() {
        viewModel.deleteItem(item)
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.noteId
        // Retrieve the item details using the itemId.
        // Attach an observer on the data (instead of polling for changes) and only update the
        // the UI when the data actually changes.
        viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
            item = selectedItem
            bind(item)
        }
    }

    /**
     * Called when fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
