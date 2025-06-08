package com.example.msbyroomlib.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.msbyroomlib.database.StudentDatabase
import com.example.msbyroomlib.entity.Student
import com.example.msbyroomlib.repository.StudentRepository
import kotlinx.coroutines.launch

class StudentViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: StudentRepository
    val allStudents: LiveData<List<Student>>

    init {
        val studentDao = StudentDatabase.getDatabase(application).studentDao()
        repository = StudentRepository(studentDao)
        allStudents = repository.getAllStudents()
    }

    fun insertStudent(student: Student) = viewModelScope.launch {
        repository.insertStudent(student)
    }

    fun updateStudent(student: Student) = viewModelScope.launch {
        repository.updateStudent(student)
    }

    fun deleteStudent(student: Student) = viewModelScope.launch {
        repository.deleteStudent(student)
    }

    suspend fun getStudentById(id: Int): Student? {
        return repository.getStudentById(id)
    }
}
