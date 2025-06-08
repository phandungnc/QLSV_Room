package com.example.msbyroomlib.repository

import androidx.lifecycle.LiveData
import com.example.msbyroomlib.dao.StudentDao
import com.example.msbyroomlib.entity.Student

class StudentRepository(private val studentDao: StudentDao) {
    
    fun getAllStudents(): LiveData<List<Student>> {
        return studentDao.getAllStudents()
    }

    suspend fun insertStudent(student: Student) {
        studentDao.insertStudent(student)
    }

    suspend fun updateStudent(student: Student) {
        studentDao.updateStudent(student)
    }

    suspend fun deleteStudent(student: Student) {
        studentDao.deleteStudent(student)
    }

    suspend fun getStudentById(id: Int): Student? {
        return studentDao.getStudentById(id)
    }
}
