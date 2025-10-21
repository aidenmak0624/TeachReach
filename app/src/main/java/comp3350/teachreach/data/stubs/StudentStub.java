package comp3350.teachreach.data.stubs;

import java.util.HashMap;
import java.util.Map;

import comp3350.teachreach.data.interfaces.IStudentPersistence;
import comp3350.teachreach.objects.Student;
import comp3350.teachreach.objects.interfaces.IStudent;

public
class StudentStub implements IStudentPersistence
{
    private static Map<Integer, IStudent> students       = null;
    private static int                    studentIDCount = 1;

    public
    StudentStub()
    {
        if (students == null) {
            students = new HashMap<>();
        }
    }

    @Override
    public
    IStudent storeStudent(IStudent newStudent)
    {
        return storeStudent(newStudent.getAccountID());
    }

    @Override
    public
    IStudent storeStudent(int accountID)
    {
        return StudentStub.students.put(StudentStub.studentIDCount,
                                        new Student(StudentStub.studentIDCount++,
                                                    accountID));
    }

    @Override
    public
    Map<Integer, IStudent> getStudents()
    {
        return StudentStub.students;
    }
}
