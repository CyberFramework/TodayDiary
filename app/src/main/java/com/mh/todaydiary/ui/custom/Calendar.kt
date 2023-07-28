package com.mh.todaydiary.ui.custom

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarWithAdjacentMonths(today: LocalDate = LocalDate.now()) {
    val pageState = rememberPagerState(Int.MAX_VALUE / 2)
    val scope = rememberCoroutineScope()

    Column {
        CalendarHeader(
            displayMonth = today.plusMonths(pageState.currentPage - (Int.MAX_VALUE / 2).toLong()),
            onPrev = { scope.launch { pageState.scrollToPage(pageState.currentPage - 1) } },
            onNext = { scope.launch { pageState.scrollToPage(pageState.currentPage + 1) } }
        )

        HorizontalPager(
            pageCount = Int.MAX_VALUE,
            beyondBoundsPageCount = 1,
            state = pageState,
        ) {
            CalendarDays(monthToDisplay = today.plusMonths(it - (Int.MAX_VALUE / 2).toLong()))
        }
    }
}

@Composable
fun CalendarHeader(displayMonth: LocalDate, onPrev: () -> Unit, onNext: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Show previous month button
        IconButton(onClick = onPrev) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Previous Month")
        }

        // Show current month and year
        Text(
            text = displayMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
            fontSize = 20.sp
        )

        // Show next month button
        IconButton(onClick = onNext) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Next Month")
        }
    }
}

@Composable
fun CalendarDays(monthToDisplay: LocalDate) {
    val today = LocalDate.now()
    val startDate = monthToDisplay.withDayOfMonth(1)
    val prevMonth = monthToDisplay.minusMonths(1)
    val nextMonth = monthToDisplay.plusMonths(1)
    val daysInPrevMonth = prevMonth.lengthOfMonth()
    val daysInCurrentMonth = monthToDisplay.lengthOfMonth()
    val firstDayOfWeek = startDate.dayOfWeek.value % 7

    LazyVerticalGrid(
        modifier = Modifier.padding(horizontal = 16.dp),
        columns = GridCells.Fixed(7)
    ) {
        // Create a list of dates to display in the calendar
        items(42) { index ->
            val dateToDisplay = when {
                index < firstDayOfWeek -> prevMonth.withDayOfMonth(daysInPrevMonth - firstDayOfWeek + index + 1)
                index < firstDayOfWeek + daysInCurrentMonth -> startDate.withDayOfMonth(index - firstDayOfWeek + 1)
                else -> nextMonth.withDayOfMonth(index - firstDayOfWeek - daysInCurrentMonth + 1)
            }

            // Determine whether the date is in the current month or not
            val isCurrentMonth = dateToDisplay.month == monthToDisplay.month

            // Set the background color for the current date
            val backgroundColor = if (isCurrentMonth && dateToDisplay.dayOfMonth == today.dayOfMonth && dateToDisplay.month == today.month)
                MaterialTheme.colorScheme.primary else Color.Transparent

            // Show the date in the calendar
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(2.dp)
                    .background(color = backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dateToDisplay.dayOfMonth.toString(),
                    fontSize = 16.sp,
                    color = if (isCurrentMonth) Color.Black else Color.Gray
                )
            }
        }
    }
}

@Preview
@Composable
fun CalendarPreview() {
    val currentDate = LocalDate.now()
    CalendarWithAdjacentMonths(currentDate)
}
