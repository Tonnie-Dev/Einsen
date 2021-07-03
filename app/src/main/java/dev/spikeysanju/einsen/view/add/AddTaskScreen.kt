package dev.spikeysanju.einsen.view.add

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.spikeysanju.einsen.R
import dev.spikeysanju.einsen.components.*
import dev.spikeysanju.einsen.model.Task
import dev.spikeysanju.einsen.navigation.MainActions
import dev.spikeysanju.einsen.ui.theme.typography
import dev.spikeysanju.einsen.utils.EmojiViewState
import dev.spikeysanju.einsen.utils.showToast
import dev.spikeysanju.einsen.view.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun AddTaskScreen(viewModel: MainViewModel, actions: MainActions) {
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    var emojiState by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    var urgencyState by remember { mutableStateOf(0F) }
    var importanceState by remember { mutableStateOf(0F) }
    val stepCount by remember { mutableStateOf(5) }
    val result = viewModel.emoji.collectAsState().value

    ModalBottomSheetLayout(sheetState = bottomSheetState, sheetContent = {

        LazyVerticalGrid(
            cells = GridCells.Adaptive(minSize = 60.dp)
        ) {
            // get all emoji
            viewModel.getAllEmoji(context)
            // parse emoji into ViewStates
            when (result) {
                EmojiViewState.Empty -> {
                    item {
                        Message(title = "Empty")
                    }
                }
                is EmojiViewState.Error -> {
                    item {
                        Message("Error ${result.exception}")
                    }
                }
                EmojiViewState.Loading -> {
                    item {
                        Message("Loading")
                    }
                }
                is EmojiViewState.Success -> {
                    items(result.emojiItem) { emoji ->
                        EmojiPlaceHolderSmall(emoji = emoji.emoji, onSelect = {
                            scope.launch {
                                emojiState = it
                                bottomSheetState.hide()
                            }
                        })
                    }
                }
            }

        }

    }) {
        Scaffold(topBar = {
            TopBarWithBack(title = stringResource(R.string.text_addTask), actions.upPress)
        }) {

            LazyColumn(state = listState) {

                // Emoji
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    EmojiPlaceHolder(emoji = emojiState, onTap = {
                        scope.launch {
                            bottomSheetState.show()
                        }
                    })
                }

                // Title
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    InputTextField(title = stringResource(R.string.text_title), value = title) {
                        title = it
                    }
                }

                // Description
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    InputTextField(
                        title = stringResource(R.string.text_description),
                        value = description
                    ) {
                        description = it
                    }
                }

                // Category
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    InputTextField(
                        title = stringResource(R.string.text_category),
                        value = category
                    ) {
                        category = it
                    }
                }

                // Urgency
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.text_urgency),
                            style = typography.subtitle1,
                            color = MaterialTheme.colors.onPrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        StepSlider(stepCount = stepCount, value = urgencyState) {
                            urgencyState = it
                        }
                    }
                }

                // Importance
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.text_importance),
                            style = typography.subtitle1,
                            color = MaterialTheme.colors.onPrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        StepSlider(stepCount = stepCount, value = importanceState) {
                            importanceState = it
                        }
                    }
                }

                // Save Task
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    PrimaryButton(title = stringResource(R.string.text_save_task)) {
                        val task = Task(
                            title = title,
                            description = description,
                            category = category,
                            emoji = emojiState,
                            urgency = urgencyState,
                            importance = importanceState,
                            due = "18/12/2021",
                            isCompleted = true
                        )

                        when {
                            title.isEmpty() -> showToast(context, "Title is Empty!")
                            description.isEmpty() -> showToast(context, "Description is Empty!")
                            category.isEmpty() -> showToast(context, "Category is Empty!")
                            else -> viewModel.insertTask(task).run {
                                showToast(context, "Task Added Successfully!")
                            }
                        }
                    }
                }
            }

        }

    }
}

@Composable
private fun BottomSheetTitle() {
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        text = stringResource(R.string.tetxt_choose_emoji),
        style = typography.h5,
        textAlign = TextAlign.Start,
        color = MaterialTheme.colors.onPrimary
    )
    Spacer(modifier = Modifier.height(16.dp))
}

