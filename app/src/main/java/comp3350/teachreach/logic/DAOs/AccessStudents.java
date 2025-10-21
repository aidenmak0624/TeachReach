package comp3350.teachreach.logic.DAOs;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;

import comp3350.teachreach.application.Server;
import comp3350.teachreach.data.interfaces.IStudentPersistence;
import comp3350.teachreach.logic.exceptions.DataAccessException;
import comp3350.teachreach.objects.interfaces.IStudent;

public class AccessStudents {
    private static IStudentPersistence studentPersistence;
    private static Map<Integer, IStudent> students = null;

    public AccessStudents() {
        studentPersistence = Server.getStudentDataAccess();
        students = studentPersistence.getStudents();
    }

    public AccessStudents(final IStudentPersistence studentPersistence) {
        AccessStudents.studentPersistence = studentPersistence;
        students = studentPersistence.getStudents();
    }

    public Map<Integer, IStudent> getStudents() {
        if (students == null) {
            students = studentPersistence.getStudents();
        }
        return Collections.unmodifiableMap(students);
    }

    public IStudent getStudentByAccountID(int studentAccountID) {
        if (students == null) {
            students = studentPersistence.getStudents();
        }
        return AccessStudents.students
                .values()
                .stream()
                .filter(s -> s.getAccountID() == studentAccountID)
                .findFirst()
                .orElseThrow(() -> new DataAccessException("Account not found!",
                        new NoSuchElementException()));
    }

    public IStudent getStudentByStudentID(int studentID) {
        if (students == null) {
            students = studentPersistence.getStudents();
        }
        IStudent resultStudent = AccessStudents.students.values()
                .stream()
                .filter(s -> s.getStudentID() == studentID)
                .findFirst()
                .orElseThrow(() -> new DataAccessException("Account not found!",
                        new NoSuchElementException()));
        if (resultStudent == null) {
            throw new DataAccessException("Student not found!",
                    new NoSuchElementException());
        }
        return resultStudent;
    }

    public IStudent insertStudent(IStudent newStudent) {
        try {
            newStudent = studentPersistence.storeStudent(newStudent);
            students = studentPersistence.getStudents();
            return newStudent;
        } catch (final Exception e) {
            throw new DataAccessException("Failed to insert a new student!", e);
        }
    }
}
