package com.example.msbyroomlib.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.msbyroomlib.entity.Student

@Dao
interface StudentDao {
    @Query("SELECT * FROM students ORDER BY id DESC")
    fun getAllStudents(): LiveData<List<Student>>

    @Insert
    suspend fun insertStudent(student: Student)

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("SELECT * FROM students WHERE id = :id")
    suspend fun getStudentById(id: Int): Student?
}
