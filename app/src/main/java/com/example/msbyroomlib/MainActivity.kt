package com.example.msbyroomlib

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.msbyroomlib.adapter.StudentAdapter
import com.example.msbyroomlib.entity.Student
import com.example.msbyroomlib.viewmodel.StudentViewModel

class MainActivity : AppCompatActivity() {
    
    private lateinit var etName: EditText
    private lateinit var etStudentId: EditText
    private lateinit var btnAddStudent: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var studentViewModel: StudentViewModel
    private lateinit var studentAdapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        initViews()
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        studentViewModel = ViewModelProvider(this)[StudentViewModel::class.java]
        
        setupRecyclerView()
        
        setupClickListeners()
        
        observeStudents()
    }
    
    private fun initViews() {
        etName = findViewById(R.id.etName)
        etStudentId = findViewById(R.id.etStudentId)
        btnAddStudent = findViewById(R.id.btnAddStudent)
        recyclerView = findViewById(R.id.recyclerView)
    }
    
    private fun setupRecyclerView() {
        studentAdapter = StudentAdapter(
            onEditClick = { student -> showEditDialog(student) },
            onDeleteClick = { student -> showDeleteConfirmDialog(student) }
        )
        
        recyclerView.apply {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
    
    private fun setupClickListeners() {
        btnAddStudent.setOnClickListener {
            addStudent()
        }
    }
    
    private fun observeStudents() {
        studentViewModel.allStudents.observe(this) { students ->
            studentAdapter.submitList(students)
        }
    }
    
    private fun addStudent() {
        val name = etName.text.toString().trim()
        val studentId = etStudentId.text.toString().trim()
        
        if (name.isNotEmpty() && studentId.isNotEmpty()) {
            val student = Student(
                name = name,
                studentId = studentId
            )
            
            studentViewModel.insertStudent(student)
            clearInputFields()
            Toast.makeText(this, "Đã thêm sinh viên thành công", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun clearInputFields() {
        etName.setText("")
        etStudentId.setText("")
    }
    
    private fun showSuccessMessage() {
        Toast.makeText(this, "Thêm sinh viên thành công", Toast.LENGTH_SHORT).show()
    }
    
    private fun showEditDialog(student: Student) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_student, null)
        
        val etEditName = dialogView.findViewById<EditText>(R.id.etEditName)
        val etEditStudentId = dialogView.findViewById<EditText>(R.id.etEditStudentId)
        
        etEditName.setText(student.name)
        etEditStudentId.setText(student.studentId)
        
        AlertDialog.Builder(this)
            .setTitle("Sửa thông tin sinh viên")
            .setView(dialogView)
            .setPositiveButton("Cập nhật") { _, _ ->
                updateStudent(student, etEditName, etEditStudentId)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
    
    private fun updateStudent(
        student: Student, 
        etEditName: EditText, 
        etEditStudentId: EditText
    ) {
        val name = etEditName.text.toString().trim()
        val studentId = etEditStudentId.text.toString().trim()
        
        if (name.isNotEmpty() && studentId.isNotEmpty()) {
            val updatedStudent = student.copy(
                name = name,
                studentId = studentId
            )
            
            studentViewModel.updateStudent(updatedStudent)
            Toast.makeText(this, "Cập nhật sinh viên thành công", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun showDeleteConfirmDialog(student: Student) {
        AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa sinh viên ${student.name}?")
            .setPositiveButton("Xóa") { _, _ ->
                studentViewModel.deleteStudent(student)
                Toast.makeText(this, "Đã xóa sinh viên", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}