package com.example.note.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.note.model.Note
import com.example.note.model.NoteResults
import com.example.note.model.Repository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<Repository>()
    private val noteLiveData = MutableLiveData<NoteResults>()
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        every { mockRepository.getNotes() } returns noteLiveData
        viewModel = MainViewModel(mockRepository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `should call getNotes once`() {
        verify(exactly = 1) { mockRepository.getNotes() }
    }

    @Test
    fun `should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")

        viewModel.getViewState().observeForever { result = it?.error }
        noteLiveData.value = NoteResults.Error(testData)

        assertEquals(result, testData)
    }

    @Test
    fun `should return notes`() {
        var result: List<Note>? = null
        val testData = listOf(Note(id = "1"), Note(id = "2"))

        viewModel.getViewState().observeForever { result = it?.data }
        noteLiveData.value = NoteResults.Success(testData)

        assertEquals(result, testData)
    }

    @Test
    fun `should remove observer`() {
        viewModel.onCleared()

        assertFalse(noteLiveData.hasObservers())
    }
}