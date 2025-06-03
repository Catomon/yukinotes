package com.github.catomon.yukinotes.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomBar(
    noteSelected: Boolean,
    isShowConfirmDelete: Boolean,
    showRemoveConfirm: () -> Unit,
    removeNote: () -> Unit,
    cancelRemove: () -> Unit,
    createNote: () -> Unit,
    editNote: () -> Unit,
    modifier: Modifier = Modifier
) {

    val buttonSize = sizes.bottomBarSize
    AnimatedContent(noteSelected, modifier = modifier.background(Colors.bars)) {
        if (it) {
            AnimatedContent(isShowConfirmDelete && noteSelected) { showConfirm ->
                if (showConfirm) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.fillMaxSize()
                    ) {
                        CancelDeleteButton(
                            cancelRemove,
                            Modifier.height(buttonSize).weight(0.33f)
                        )
                        TrashcanImage(Modifier.height(buttonSize).weight(0.33f))
                        ConfirmDeleteButton(
                            removeNote,
                            Modifier.height(buttonSize).weight(0.33f)
                        )
                    }
                } else {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.fillMaxSize()
                    ) {
                        DeleteButton(
                            showRemoveConfirm,
                            Modifier.height(buttonSize).weight(0.33f)
                        )
                        CreateButton(createNote, Modifier.height(buttonSize).weight(0.33f))
                        EditButton(editNote, Modifier.height(buttonSize).weight(0.33f))
                    }
                }
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxSize()
            ) {
                CreateButton(createNote, Modifier.height(buttonSize).weight(0.33f))
            }
        }
    }
}

@Composable
fun BottomBar2(
    noteSelected: Boolean,
    isShowConfirmDelete: Boolean,
    showRemoveConfirm: () -> Unit,
    removeNote: () -> Unit,
    cancelRemove: () -> Unit,
    createNote: () -> Unit,
    modifier: Modifier = Modifier
) {

    val buttonSize = sizes.bottomBarSize
    AnimatedContent(noteSelected, modifier = modifier) {
        if (it) {
            AnimatedContent(isShowConfirmDelete && noteSelected) { showConfirm ->
                if (showConfirm) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.fillMaxSize()
                    ) {
                        CancelDeleteButton(
                            cancelRemove,
                            Modifier.height(buttonSize).weight(0.33f)
                        )
                        TrashcanImage(Modifier.height(buttonSize).weight(0.33f))
                        ConfirmDeleteButton(
                            removeNote,
                            Modifier.height(buttonSize).weight(0.33f)
                        )
                    }
                } else {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.fillMaxSize()
                    ) {
                        DeleteButton(
                            showRemoveConfirm,
                            Modifier.height(buttonSize).weight(0.50f)
                        )
                        CreateButton(createNote, Modifier.height(buttonSize).weight(0.50f))
                    }
                }
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxSize()
            ) {
//                CreateButton(createNote, Modifier.height(buttonSize).weight(0.33f))
            }
        }
    }
}


@Composable
private fun EditButton(
    editNote: () -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        painterResource(YukiIcons.editNote),
        contentDescription = "Edit Note",
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = editNote),
        tint = Colors.currentYukiTheme.surface
    )
}

@Composable
private fun CreateButton(
    createNote: () -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        painterResource(YukiIcons.createNote),
        contentDescription = "Create Note",
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = createNote),
        tint = Colors.currentYukiTheme.surface
    )
}

@Composable
private fun DeleteButton(
    showRemoveConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        painterResource(YukiIcons.deleteNote),
        contentDescription = "Delete Note",
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = showRemoveConfirm),
        tint = Colors.currentYukiTheme.surface
    )
}

@Composable
private fun ConfirmDeleteButton(
    removeNote: () -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        painterResource(YukiIcons.confirm),
        contentDescription = "Confirm Delete Note",
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = removeNote),
        tint = Colors.currentYukiTheme.surface
    )
}

@Composable
private fun TrashcanImage(
    modifier: Modifier = Modifier
) {
    Icon(
        painterResource(YukiIcons.confirmDeleteNote),
        contentDescription = "Trashcan",
        modifier = modifier.clip(RoundedCornerShape(10.dp)),
        tint = Colors.currentYukiTheme.surface
    )
}

@Composable
private fun CancelDeleteButton(
    cancelRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        painterResource(YukiIcons.cancel),
        contentDescription = "Cancel Delete Note",
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = cancelRemove),
        tint = Colors.currentYukiTheme.surface
    )
}