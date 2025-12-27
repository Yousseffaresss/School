
import java.util.Optional;

class Enrollment {
    int id;
    int studentId;
    int courseId;
    double grade; // -1 means not graded yet

    public Enrollment(int id, int studentId, int courseId, double grade) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.grade = grade;
    }

    public String toFileString() {
        return id + "|" + studentId + "|" + courseId + "|" + grade;
    }

    public static Enrollment fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        return new Enrollment(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]), Double.parseDouble(p[3]));
    }

    @Override
    public String toString() {
        String g = (grade < 0) ? "N/A" : String.format("%.2f", grade);
        return String.format("EnrollID:%d | StudentID:%d | CourseID:%d | Grade:%s", id, studentId, courseId, g);
    }
}
static void enrollmentsMenu() {
        while (true) {
            System.out.println("\n--- Enrollments Menu ---");
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. List Enrollments");
            System.out.println("3. Record/Update Grade");
            System.out.println("4. Unenroll (delete)");
            System.out.println("5. Back");
            System.out.print("Choose: ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1" -> enrollStudent();
                case "2" -> listEnrollments();
                case "3" -> recordGrade();
                case "4" -> deleteEnrollment();
                case "5" -> { return; }
                default -> System.out.println("Invalid.");
            }
        }
    }

    static void enrollStudent() {
        System.out.print("Student ID: ");
        int sid = readIntSafe();
        if (findStudentById(sid) == null) { System.out.println("No such student."); return; }
        System.out.print("Course ID: ");
        int cid = readIntSafe();
        if (findCourseById(cid) == null) { System.out.println("No such course."); return; }
        // avoid duplicate enrollment
        boolean exists = enrollments.stream().anyMatch(e -> e.studentId == sid && e.courseId == cid);
        if (exists) { System.out.println("Student already enrolled in this course."); return; }
        Enrollment en = new Enrollment(nextEnrollId++, sid, cid, -1.0);
        enrollments.add(en);
        System.out.println("Enrolled: " + en);
    }

    static void listEnrollments() {
        if (enrollments.isEmpty()) { System.out.println("No enrollments."); return; }
        for (Enrollment e : enrollments) {
            String sname = Optional.ofNullable(findStudentById(e.studentId)).map(st -> st.name).orElse("UnknownStudent");
            String cname = Optional.ofNullable(findCourseById(e.courseId)).map(co -> co.title).orElse("UnknownCourse");
            String g = (e.grade < 0) ? "N/A" : String.format("%.2f", e.grade);
            System.out.println(String.format("EnrollID:%d | %s(%d) -> %s(%d) | Grade:%s", e.id, sname, e.studentId, cname, e.courseId, g));
        }
    }

    static void recordGrade() {
        System.out.print("Enter Enrollment ID: ");
        int eid = readIntSafe();
        Enrollment en = enrollments.stream().filter(x -> x.id == eid).findFirst().orElse(null);
        if (en == null) { System.out.println("Not found."); return; }
        System.out.println("Current: " + en);
        System.out.print("Enter grade (0.0 - 100.0 or scale you use): ");
        double g = readDoubleSafe();
        en.grade = g;
        System.out.println("Grade recorded.");
    }

    static void deleteEnrollment() {
        System.out.print("Enter Enrollment ID to delete: ");
        int eid = readIntSafe();
        boolean removed = enrollments.removeIf(e -> e.id == eid);
        if (removed) System.out.println("Enrollment removed.");
        else System.out.println("Not found.");
    }