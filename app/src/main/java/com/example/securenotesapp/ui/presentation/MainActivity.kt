package com.example.securenotesapp.ui.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.securenotesapp.AppGraph
import com.example.securenotesapp.domain.NotesRepository
import com.example.securenotesapp.ui.navigation.NoteDetails
import com.example.securenotesapp.ui.navigation.NotesList
import com.example.securenotesapp.ui.presentation.details_screen.NoteDetailsScreen
import com.example.securenotesapp.ui.presentation.details_screen.NoteDetailsViewModel
import com.example.securenotesapp.ui.presentation.list_screen.NotesListScreen
import com.example.securenotesapp.ui.presentation.list_screen.NotesListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNav()
        }
    }
}

@Composable
fun AppNav() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Single repo instance for the whole app
    val repository: NotesRepository = remember {
        AppGraph.provideRepository(context)
    }

    NavHost(navController = navController, startDestination = NotesList) {

        composable<NotesList> {
            val vm: NotesListViewModel = viewModel(
                factory = NotesListVmFactory(repository)
            )

            NotesListScreen(
                viewModel = vm,
                onAdd = { navController.navigate(NoteDetails(noteId = -1L)) },
                onOpen = { id -> navController.navigate(NoteDetails(noteId = id)) },
            )
        }

        composable<NoteDetails> { backStackEntry ->
            val args = backStackEntry.toRoute<NoteDetails>()

            val vm: NoteDetailsViewModel = viewModel(
                key = "NoteDetailsViewModel_${args.noteId}",
                factory = NoteDetailsVmFactory(
                    noteId = args.noteId,
                    repository = repository
                )
            )

            NoteDetailsScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

/** ---------- Factories (no DI framework needed) ---------- */

private class NotesListVmFactory(
    private val repository: NotesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotesListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

private class NoteDetailsVmFactory(
    private val noteId: Long,
    private val repository: NotesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteDetailsViewModel(noteId = noteId, repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
