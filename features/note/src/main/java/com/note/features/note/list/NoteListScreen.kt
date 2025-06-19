package com.note.features.note.list

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.note.features.core.ui.Dimens
import com.note.features.dynamic_background.DynamicBackgroundContainer
import com.note.features.note.R
import com.note.features.note.list.model.NoteUi
import com.note.libraries.note.domain.usecase.GetNoteUseCase.SortOption

@Composable
fun NoteListScreen(
    viewModel: NoteListViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val pagingItems = viewModel.notesFlow.collectAsLazyPagingItems()
    val gridState = rememberLazyGridState()
    var showAddNoteDialog by remember { mutableStateOf(false) }
    var noteToShowDetails by remember { mutableStateOf<NoteUi?>(null) }

    NoteListContent(
        state = state,
        notesItems = pagingItems,
        gridCells = GridCells.Fixed(getGridCell()),
        gridState = gridState,
        onSearchQueryChange = { query ->
            viewModel.onEvent(NoteListEvent.SetSearchQuery(query))
        },
        onToggleSortDirection = {
            viewModel.onEvent(NoteListEvent.ToggleSortDirection)
        },
        onAddNoteClick = {
            showAddNoteDialog = true
        },
        onNoteClick = { note ->
            noteToShowDetails = note
        },
        onToggleNoteCompletion = { note ->
            viewModel.onEvent(NoteListEvent.ToggleNoteCompletion(note))
        },
    )

    // Dialogs
    if (showAddNoteDialog) {
        AddNoteDialog(
            onDismiss = { showAddNoteDialog = false },
            onSave = { title, content ->
                viewModel.onEvent(NoteListEvent.CreateNote(title, content))
                showAddNoteDialog = false
            },
        )
    }

    noteToShowDetails?.let {
        NoteDetailDialog(
            note = it, onDismiss = { noteToShowDetails = null },
            onDelete = {
                viewModel.onEvent(NoteListEvent.DeleteNote(it))
                noteToShowDetails = null
            },
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
private fun getGridCell(): Int {
    val numberOfCellOnPhone = 1
    val numberOfCellOnMediumTablet = 2
    val numberOfCellOnBigTablet = 3

    val activity = LocalActivity.current ?: return numberOfCellOnPhone
    val windowSizeClass = calculateWindowSizeClass(activity)

    return when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> numberOfCellOnPhone
        WindowWidthSizeClass.Medium -> numberOfCellOnMediumTablet
        else -> numberOfCellOnBigTablet
    }
}

@Composable
private fun NoteListContent(
    state: NoteListState,
    notesItems: LazyPagingItems<NoteUi>,
    gridCells: GridCells,
    gridState: LazyGridState,
    onSearchQueryChange: (String) -> Unit,
    onToggleSortDirection: () -> Unit,
    onAddNoteClick: () -> Unit,
    onNoteClick: (NoteUi) -> Unit,
    onToggleNoteCompletion: (NoteUi) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Row {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        onSearchQueryChange(it)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(Dimens.mediumPadding),
                    placeholder = { Text(stringResource(R.string.search_notes)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search_notes)
                        )
                    },
                    shape = RoundedCornerShape(Dimens.mediumCornerRadius),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    ),
                    singleLine = true
                )

                IconButton(
                    onClick = onToggleSortDirection,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = if (state.sortOption is SortOption.OldestFirst) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.sort_notes)
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNoteClick, containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_note),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        content = { paddingValues ->
            NotesGridContent(
                notesItems = notesItems,
                gridCells = gridCells,
                gridState = gridState,
                contentPadding = paddingValues,
                onNoteClick = onNoteClick,
                onToggleNoteCompletion = onToggleNoteCompletion
            )
        },
    )
}

@Composable
private fun NotesGridContent(
    notesItems: LazyPagingItems<NoteUi>,
    gridCells: GridCells,
    gridState: LazyGridState,
    contentPadding: PaddingValues,
    onNoteClick: (NoteUi) -> Unit,
    onToggleNoteCompletion: (NoteUi) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        // Content with items
        LazyVerticalGrid(
            columns = gridCells,
            contentPadding = PaddingValues(Dimens.mediumPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.largeSpacing),
            horizontalArrangement = Arrangement.spacedBy(Dimens.largeSpacing),
            modifier = Modifier.fillMaxSize(),
            state = gridState
        ) {
            items(
                count = notesItems.itemCount,
                key = { index -> notesItems[index]?.id ?: index }) { index ->
                notesItems[index]?.let { noteUi ->
                    NoteItem(
                        note = noteUi,
                        onClick = { onNoteClick(noteUi) },
                        onToggleCompletion = { onToggleNoteCompletion(noteUi) },
                    )
                }
            }

            // Append loading state
            if (notesItems.loadState.append is LoadState.Loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.mediumPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        // Loading state for the first load
        if (notesItems.loadState.refresh is LoadState.Loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        // Empty state
        else if (notesItems.itemCount == 0 && notesItems.loadState.refresh is LoadState.NotLoading) {
            EmptyState(modifier = Modifier.fillMaxSize())
        }

        // Error state for the first load
        else if (notesItems.loadState.refresh is LoadState.Error) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.common_error),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.empty_notes_message),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun AddNoteDialog(
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_note)) },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.title)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                    )
                )
                Spacer(modifier = Modifier.height(Dimens.smallSpacing))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text(stringResource(R.string.content)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 5,
                    shape = RoundedCornerShape(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(title, content) }, enabled = title.isNotBlank()
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    )
}

@Composable
fun NoteDetailDialog(
    note: NoteUi,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(note.title, style = MaterialTheme.typography.headlineSmall)
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete)
                    )
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (note.content.isNotBlank()) {
                    Text(
                        text = note.content,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = Dimens.smallPadding)
                    )
                } else {
                    Text(
                        text = stringResource(R.string.no_description),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = Dimens.smallPadding)
                    )
                }

                Spacer(Modifier.height(Dimens.largePadding))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (note.isCompleted) stringResource(R.string.completed)
                        else stringResource(R.string.not_completed),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = note.createdAt,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.close))
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}

@Composable
fun NoteItem(
    note: NoteUi,
    onClick: () -> Unit,
    onToggleCompletion: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.elevation),
        shape = RoundedCornerShape(Dimens.mediumCornerRadius)
    ) {
        DynamicBackgroundContainer {
            Row(
                modifier = Modifier.padding(Dimens.mediumPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (note.content.isNotBlank()) {
                        Spacer(modifier = Modifier.height(Dimens.smallSpacing))
                        Text(
                            text = note.content,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Spacer(modifier = Modifier.height(Dimens.smallSpacing))
                        Text(
                            text = stringResource(R.string.no_description),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimens.smallSpacing))
                    Text(
                        text = note.createdAt,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }

                Checkbox(checked = note.isCompleted, onCheckedChange = { onToggleCompletion() })
            }
        }
    }
}
