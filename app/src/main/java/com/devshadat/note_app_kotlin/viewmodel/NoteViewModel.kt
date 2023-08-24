/*
 * Copyright (C) 2023 devshadat
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.devshadat.note_app_kotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Note
import com.example.inventory.data.NoteDao
import kotlinx.coroutines.launch

/**
 * View Model to keep a reference to the Inventory repository and an up-to-date list of all items.
 *
 */
class NoteViewModel(private val noteDao: NoteDao) : ViewModel() {

    // Cache all items form the database using LiveData.
    val allItems: LiveData<List<Note>> = noteDao.getNotes().asLiveData()

    /**
     * Returns true if stock is available to sell, false otherwise.
     *//*fun isStockAvailable(item: Note): Boolean {
        return (item.quantityInStock > 0)
    }*/

    /**
     * Updates an existing Item in the database.
     */
    fun updateNote(
        noteId: Int, noteTitle: String, noteDetail: String
    ) {
        val updateNote = getUpdatedItemEntry(noteId, noteTitle, noteDetail)
        updateNote(updateNote)
    }


    /**
     * Launching a new coroutine to update an item in a non-blocking way
     */
    private fun updateNote(note: Note) {
        viewModelScope.launch {
            noteDao.update(note)
        }
    }

    /**
     * Decreases the stock by one unit and updates the database.
     *//*fun sellItem(item: Note) {
        if (item.quantityInStock > 0) {
            // Decrease the quantity by 1
            val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
            updateNote(newItem)
        }
    }*/

    fun editNote(item: Note) {
        // Decrease the quantity by 1
        val newItem = item.copy()
        updateNote(newItem)
    }

    /**
     * Inserts the new Item into database.
     */
    fun addNewNote(noteTitle: String, noteDetail: String) {
        val newNote = getNewItemEntry(noteTitle, noteDetail)
        insertItem(newNote)
    }

    /**
     * Launching a new coroutine to insert an item in a non-blocking way
     */
    private fun insertItem(item: Note) {
        viewModelScope.launch {
            noteDao.insert(item)
        }
    }

    /**
     * Launching a new coroutine to delete an item in a non-blocking way
     */
    fun deleteItem(item: Note) {
        viewModelScope.launch {
            noteDao.delete(item)
        }
    }

    /**
     * Retrieve an item from the repository.
     */
    fun retrieveItem(id: Int): LiveData<Note> {
        return noteDao.getNote(id).asLiveData()
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    fun isEntryValid(noteTitle: String, noteDetail: String): Boolean {
        if (noteTitle.isBlank() || noteDetail.isBlank()) {
            return false
        }
        return true
    }

    /**
     * Returns an instance of the [Note] entity class with the item info entered by the user.
     * This will be used to add a new entry to the Inventory database.
     */
    private fun getNewItemEntry(noteTitle: String, noteDetail: String): Note {
        return Note(
            noteTitle = noteTitle, noteDetail = noteDetail
        )
    }

    /**
     * Called to update an existing entry in the Inventory database.
     * Returns an instance of the [Note] entity class with the item info updated by the user.
     */
    private fun getUpdatedItemEntry(
        itemId: Int,
        noteTitle: String,
        noteDetail: String,
    ): Note {
        return Note(
            id = itemId, noteTitle = noteTitle, noteDetail = noteDetail
        )
    }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class NoteViewModelFactory(private val itemDao: NoteDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return NoteViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

