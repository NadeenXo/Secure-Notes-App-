package com.example.securenotesapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.securenotesapp.ui.presentation.details_screen.NoteDetailsScreen
import com.example.securenotesapp.ui.presentation.details_screen.NoteDetailsViewModel
import com.example.securenotesapp.ui.presentation.list_screen.NotesListScreen
import com.example.securenotesapp.ui.presentation.list_screen.NotesListViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NotesList) {

        composable<NotesList> {
            val vm: NotesListViewModel = koinViewModel()

            NotesListScreen(
                viewModel = vm,
                onAdd = { navController.navigate(NoteDetails(noteId = -1L)) },
                onOpen = { id -> navController.navigate(NoteDetails(noteId = id)) }
            )
        }

        composable<NoteDetails> { backStackEntry ->
            val args = backStackEntry.toRoute<NoteDetails>()

            val vm: NoteDetailsViewModel = koinViewModel(
                key = "NoteDetailsViewModel_${args.noteId}",
                parameters = { parametersOf(args.noteId) }
            )

            NoteDetailsScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() }
            )
        }
    }
}