/*
 * Copyright (C) 2023 devshadat
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.devshadat.note_app_kotlin

import android.app.Application
import com.example.inventory.data.NoteDatabase


class NoteApplication : Application() {
    // Using by lazy so the database is only created when needed
    // rather than when the application starts
    val database: NoteDatabase by lazy { NoteDatabase.getDatabase(this) }
}