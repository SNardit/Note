package com.example.note.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.note.R
import com.example.note.viewmodel.BaseViewModel
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity<T, VS : BaseViewState<T>> : AppCompatActivity() {

    abstract val viewModel: BaseViewModel<T, VS>
    abstract val layoutRes: Int
    abstract val ui: ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ui.root)

        viewModel.getViewState().observe(this) { t ->
            t?.apply {
                data?.let { data -> renderData(data) }
                error?.let { error -> renderError(error) }
            }
        }
    }

    abstract fun renderData(data: T)

    protected open fun renderError(error: Throwable) {
        error.message?.let { message -> showError(message) }
    }

    protected fun showError(error: String) {
        Snackbar.make(ui.root, error, Snackbar.LENGTH_INDEFINITE).apply {
            setAction(R.string.snackbar_action) { dismiss() }
            show()
        }
    }
}