import dao.Dao;
import dao.CourseDao;
import dao.CourseMaterialDao;
import dao.StudentDao;
import dao.TeacherDao;
import models.*;

import java.time.LocalDate;
import java.util.List;

public class App {
    public static void main(String[] args) {
        try {
            runExamples();
        } finally {
            Dao.closeEntityManagerFactory();
        }
    }

    private static void runExamples() {
        try (TeacherDao teacherDao = new TeacherDao();
             CourseDao courseDao = new CourseDao();
             StudentDao studentDao = new StudentDao();
             CourseMaterialDao materialDao = new CourseMaterialDao()) {

            // TAREA 1 Y TAREA 2: CRUD de Teacher y find() de Course
            System.out.println("\n========== TAREA 1 Y TAREA 2 ==========");

            // Create (Crear)
            Teacher teacher1 = new Teacher();
            teacher1.setFirstName("Eider");
            teacher1.setLastName("Arbaiza");
            teacherDao.create(teacher1);

            Teacher teacherBorrar = new Teacher();
            teacherBorrar.setFirstName("Profesor");
            teacherBorrar.setLastName("Temporal");
            teacherDao.create(teacherBorrar);

            // Find (Buscar)
            Teacher teacherLeido = teacherDao.find(teacher1.getId());
            System.out.println("Teacher: " + teacherLeido.getFirstName() + " " + teacherLeido.getLastName());

            // Update (Modificar)
            teacherLeido.setFirstName("Eider Modificado");
            teacherDao.update(teacherLeido);

            // Delete (Borrar)
            teacherDao.delete(teacherBorrar);

            // Restaurar nombre
            teacher1.setFirstName("Eider");
            teacherDao.update(teacher1);

            // Tarea 1: find() de Course trae su Teacher
            Course course1 = new Course();
            course1.setTitle("Acceso a Datos");
            course1.setTeacher(courseDao.getEntityManager().getReference(Teacher.class, teacher1.getId()));
            courseDao.create(course1);

            Course courseEncontrado = courseDao.find(course1.getId());
            System.out.println("Curso: " + courseEncontrado.getTitle() + " | Profe: " + courseEncontrado.getTeacher().getLastName());


            // TAREA 3: Error al guardar curso sin profesor
            System.out.println("\n========== TAREA 3 ==========");
            try {
                Course courseSinTeacher = new Course();
                courseSinTeacher.setTitle("Curso sin profe");
                courseDao.create(courseSinTeacher); // Da error por optional = false
            } catch (Exception e) {
                System.out.println("Error capturado (falta profesor).");
            }


            // TAREA 4: Guardado en cascada (CascadeType.PERSIST)
            System.out.println("\n========== TAREA 4 ==========");
            Teacher teacherPDF = new Teacher();
            teacherPDF.setFirstName("Eider");
            teacherPDF.setLastName("Arbaiza");

            Course course2 = new Course();
            course2.setTitle("Programación de Servicios y Procesos");
            course2.setTeacher(teacherPDF);
            courseDao.create(course2); // Guarda el profesor automáticamente


            // TAREA 5: One-to-One con CourseMaterial
            System.out.println("\n========== TAREA 5 ==========");
            CourseMaterial material = new CourseMaterial();
            material.setUrl("https://egibide.org/material-psp");
            material.setCourse(course2);
            materialDao.create(material);

            CourseMaterial matEncontrado = materialDao.find(material.getId());
            System.out.println("Material: " + matEncontrado.getUrl() + " | Curso: " + matEncontrado.getCourse().getTitle());


            // TAREA 6: Many-to-Many (Estudiantes a Curso)
            System.out.println("\n========== TAREA 6 ==========");
            Student student1 = new Student();
            student1.setFirstName("Jon");
            student1.setLastName("García");
            student1.setBirthDate(LocalDate.of(2002, 3, 15));
            student1.setWantsNewsletter(true);

            Student student2 = new Student();
            student2.setFirstName("Miren");
            student2.setLastName("Zubizarreta");
            student2.setBirthDate(LocalDate.of(2001, 7, 20));
            student2.setWantsNewsletter(false);

            studentDao.create(student1);
            studentDao.create(student2);

            // Añadir alumnos al curso
            course1.addStudent(student1);
            course1.addStudent(student2);
            courseDao.update(course1);

            Course cursoConAlumnos = courseDao.find(course1.getId());
            System.out.println("El curso tiene " + cursoConAlumnos.getStudents().size() + " alumnos.");


            // TAREA 7: Inversa de Many-to-Many (Cursos a Estudiante)
            System.out.println("\n========== TAREA 7 ==========");
            Student student3 = new Student();
            student3.setFirstName("Aitor");
            student3.setLastName("Etxeberria");
            student3.setBirthDate(LocalDate.of(2000, 11, 5));
            student3.setWantsNewsletter(true);
            studentDao.create(student3);

            // Actualizar la relación desde el lado propietario (Course).
            course1.addStudent(student3);
            course2.addStudent(student3);
            courseDao.update(course1);
            courseDao.update(course2);

            Student alumnoLeido = studentDao.find(student3.getId());
            System.out.println("El alumno " + alumnoLeido.getFirstName() + " tiene " + alumnoLeido.getCourses().size() + " cursos.");


            // TAREA 8: JPQL Cursos por Apellido de Profesor
            System.out.println("\n========== TAREA 8 ==========");
            List<Course> cursosProfesor = courseDao.listCoursesByTeacherLastName("Arbaiza");
            for (Course c : cursosProfesor) {
                System.out.println("Curso de Arbaiza: " + c.getTitle());
            }


            // TAREA 9: JPQL Estudiantes por ID de Curso
            System.out.println("\n========== TAREA 9 ==========");
            List<Student> alumnosPorCurso = studentDao.listStudentsByCourseId(course1.getId());
            for (Student s : alumnosPorCurso) {
                System.out.println("Alumno del curso: " + s.getFirstName() + " " + s.getLastName());
            }
        }
    }
}